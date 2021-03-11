package com.ticticboooom.twerkitmeal.helper;

import com.ticticboooom.twerkitmeal.config.TwerkConfig;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;

import java.util.ArrayList;
import java.util.List;

public class FilterListHelper {
    public static boolean shouldAllow(String key, Block block) {
    	//if we are looking at saplings only
        if (TwerkConfig.saplingsOnly){
        	//if the block matches the saplings list, return true (allow)
        	//or false if it doesn't match.
            return BlockTags.SAPLINGS.contains(block);
        }

    	if (TwerkConfig.useWhitelist) {
    		//run through whitelist.
            List<String> variations = new ArrayList<>();
            // entire block RL
            variations.add(key);
            // mod id from RL of block
            variations.add(key.substring(0, key.indexOf(":")));
            for (String listed : TwerkConfig.whitelist) {
                if (variations.contains(listed)){
                    return true;
                }
            }
            //not on whitelist not allowed.
            return false;
    	} else if (TwerkConfig.blackList.size() > 0) {
    		//using blacklist instead of whitelist.
            List<String> variations = new ArrayList<>();
            // entire block RL
            variations.add(key);
            // mod id from RL of block
            variations.add(key.substring(0, key.indexOf(":")));
            for (String listed : TwerkConfig.blackList) {
                if (variations.contains(listed)) {
                	//blacklisted
                    return false;
                }
            }
    	}
    	//not blacklisted, or no list at all: full throttle!
    	return true;
    }
}
