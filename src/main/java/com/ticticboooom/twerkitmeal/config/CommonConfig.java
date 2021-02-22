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
        defaultBlackList.add("minecraft:grass_block");
        defaultBlackList.add("minecraft:warped_nylium");
        defaultBlackList.add("minecraft:crimson_nylium");
        defaultBlackList.add("minecraft:tall_grass");
        defaultBlackList.add("botanypots");
        defaultBlackList.add("gaiadimension");
        showParticles = builder.comment("Whether to show particles or not when crouching to grow things")
                .define("showParticles", true);
        useWhitelist = builder.comment("whether to enable the whitelist this does not disable the blacklist")
                .define("useWhiteList", false);
        blackList = builder.comment("growables to disable crouching on")
                .define("blacklist", defaultBlackList);
        whitelist = builder.comment("growables to enable crouching on (only works if 'useWhiteList' is true and will give exclisivity to those growables) ")
                .define("whitelist", new ArrayList<>());
        minCrouchesToApplyBonemeal = builder.comment("the minimum number of crouches before the bonemeal is applied (bonemeal is applied randomly so this will not be exact)")
                .defineInRange("minCrouchesToApplyBonemeal", 5, 0, Integer.MAX_VALUE);
    }
}
