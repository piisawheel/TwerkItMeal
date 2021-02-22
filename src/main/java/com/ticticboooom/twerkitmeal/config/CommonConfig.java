package com.ticticboooom.twerkitmeal.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    public final ForgeConfigSpec.BooleanValue showParticles;
    public final ForgeConfigSpec.BooleanValue useWhitelist;
    public final ForgeConfigSpec.ConfigValue<List<String>> blackList;
    public final ForgeConfigSpec.ConfigValue<List<String>> whitelist;
    public final ForgeConfigSpec.IntValue minCrouchesToApplyBonemeal;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        List<String> defaultBlackList = new ArrayList<>();
        defaultBlackList.add("minecraft:netherrack");
        defaultBlackList.add("minecraft:warped_nylium");
        defaultBlackList.add("minecraft:crimson_nylium");
        defaultBlackList.add("minecraft:tall_grass");
        defaultBlackList.add("botanypots");
        defaultBlackList.add("gaiadimension");
        List<String> defaultWhiteList = new ArrayList<>();
        defaultWhiteList.add("minecraft:dirt");
        defaultWhiteList.add("minecraft:grass_block");
        defaultWhiteList.add("minecraft:bamboo_sapling");
        defaultWhiteList.add("minecraft:bamboo");
        defaultWhiteList.add("minecraft:grass_block");
        showParticles = builder.comment("Whether to show particles or not when crouching to grow things")
                .define("showParticles", true);
        useWhitelist = builder.comment("Whitelist Or Blacklist (exclusively 1 or the other).")
                .define("useWhiteList", true);
        blackList = builder.comment("Growables to disable crouching on")
                .define("blacklist", defaultBlackList);
        whitelist = builder.comment("Growables to enable crouching on")
                .define("whitelist", defaultWhiteList);
        minCrouchesToApplyBonemeal = builder.comment("The minimum number of crouches before the bonemeal is applied.\nThere is a 50% chance any allowed item will be bonemealed, within a 5x5 square around the player.\nThere is a 10% chance dirt will spawn grass\nA 5% chance grass will spawn bamboo\nA 5% chance to bonemeal any particular grass block(tall grass and flowers).")
                .defineInRange("minCrouchesToApplyBonemeal", 1, 0, Integer.MAX_VALUE);
    }
}
