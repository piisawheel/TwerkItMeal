package com.ticticboooom.twerkitmeal;

import com.ticticboooom.twerkitmeal.config.CommonConfig;
import com.ticticboooom.twerkitmeal.config.TwerkConfig;
import com.ticticboooom.twerkitmeal.helper.FilterListHelper;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;


@Mod(TwerkItMeal.MOD_ID)
public class TwerkItMeal {
	public static final String MOD_ID = "twerkitmeal";

	static final ForgeConfigSpec commonSpec;
	public static final CommonConfig COMMON_CONFIG;

	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		commonSpec = specPair.getRight();
		COMMON_CONFIG = specPair.getLeft();
	}

	public TwerkItMeal() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec, "twerk-config.toml");
		MinecraftForge.EVENT_BUS.register(new RegistryEvents());
	}


	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		private final Map<UUID, Integer> crouchCount = new HashMap<>();
		private final Map<UUID, Boolean> prevSneaking = new HashMap<>();
		private final Map<UUID, Integer> playerDistance = new HashMap<>();

		@SubscribeEvent
		public void onTwerk(TickEvent.PlayerTickEvent event) {
			if (event.player.world.isRemote) {
				return;
			}
			UUID uuid = PlayerEntity.getUUID(event.player.getGameProfile());
			if (!crouchCount.containsKey(uuid)){
				crouchCount.put(uuid, 0);
				prevSneaking.put(uuid, event.player.isSneaking());
				playerDistance.put(uuid, 0);
			}

			ServerWorld world = (ServerWorld) event.player.world;

			if (event.player.isSprinting() && world.getRandom().nextDouble() <= TwerkConfig.sprintGrowChance){
				triggerGrowth(event, uuid);
			}

			boolean wasPlayerSneaking = prevSneaking.get(uuid);
			int playerCrouchCount = crouchCount.get(uuid);
			if (!event.player.isSneaking()) {
				prevSneaking.put(uuid, false);
				return;
			}
			if (wasPlayerSneaking && event.player.isSneaking()) {
				return;
			} else if (!wasPlayerSneaking && event.player.isSneaking()) {
				prevSneaking.put(uuid, true);
				crouchCount.put(uuid, ++playerCrouchCount);
			}

			//50 percent chance removed and exchanged for a per block chance in config.
			if (playerCrouchCount >= TwerkConfig.minCrouchesToApplyBonemeal) {
				triggerGrowth(event, uuid);
			}
		}

		private void triggerGrowth(TickEvent.PlayerTickEvent event, UUID uuid) {
			double nextRandom = 0;

			crouchCount.put(uuid, 0);
			List<BlockPos> growables = getNearestBlocks(event.player.world, event.player.getPosition());
			for (BlockPos growablePos : growables) {
				BlockState blockState = event.player.world.getBlockState(growablePos);
				nextRandom = event.player.world.getRandom().nextDouble();
				boolean triggerParticles = false;
				if (blockState.hasProperty(CropsBlock.AGE)) {
					//nested because we don't want to drop into a different block if the chance doesnt happen.
					if (nextRandom <= TwerkConfig.chance) { 
						int growth = blockState.get(CropsBlock.AGE);
						event.player.world.setBlockState(growablePos, blockState.with(CropsBlock.AGE, growth < 7 ? growth + 1 : 7));
						triggerParticles = true;
					}
				} else if (blockState.getBlock() instanceof IGrowable) {
					if (blockState.getBlock() == Blocks.GRASS_BLOCK) {
						BlockPos above = growablePos.up();
						BlockState aboveState = event.player.world.getBlockState(above);
						//5 percent chance to add bamboo to empty grass block
						//5 percent (other side of random tick) to apply bonemeal to grass.
						if (aboveState.getBlock() == Blocks.AIR && nextRandom <= 0.05) {
							if (TwerkConfig.spawnBamboo) {
								event.player.world.setBlockState(above, Blocks.BAMBOO_SAPLING.getDefaultState());
								triggerParticles = true;
							}
						} else if (nextRandom >= 0.95) {
							BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), event.player.world, growablePos, event.player);
							triggerParticles = true;
						}
					} else if (nextRandom <= TwerkConfig.chance) {
						BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), event.player.world, growablePos, event.player);
						triggerParticles = true;
					}
					//10 percent chance to turn dirt into grass.
				} else if (blockState.getBlock() == Blocks.DIRT && nextRandom <= 0.1) {
					event.player.world.setBlockState(growablePos, Blocks.GRASS_BLOCK.getDefaultState());
					triggerParticles = true;
				}
				//only trigger particles if something actually grows.  Should significantly reduce the number of particles that shows.
				if (triggerParticles) {
					((ServerWorld)event.player.world).spawnParticle((ServerPlayerEntity) event.player, ParticleTypes.HAPPY_VILLAGER, false, growablePos.getX() + event.player.world.rand.nextDouble(), growablePos.getY() + event.player.world.rand.nextDouble(), growablePos.getZ() + event.player.world.rand.nextDouble(), 10, 0, 0, 0, 3);
				}
			}
		}
		private List<BlockPos> getNearestBlocks(World world, BlockPos pos) {
			List<BlockPos> list = new ArrayList<>();
			int range = TwerkConfig.effectRadius;
			int height = TwerkConfig.rangeVertical;
			for (int x = -range; x <= range; x++)
				for (int y = -height; y <= height; y++)
					for (int z = -range; z <= range; z++) {
						Block block = world.getBlockState(new BlockPos(x + pos.getX(), y + pos.getY(), z + pos.getZ())).getBlock();
						if (block instanceof IGrowable || block == Blocks.DIRT) {
							//performance
							//by filtering the block list when we gather the blocks, instead of when we actually do the growth check,
							//we will loop the same amount of times _here,_ but will have less to loop through in the calling function.
							if (FilterListHelper.shouldAllow(block.getRegistryName().toString(), block)) {
								list.add(new BlockPos(x + pos.getX(), y + pos.getY(), z + pos.getZ()));
							}
						}
					}
			return list;
		}
	}
}