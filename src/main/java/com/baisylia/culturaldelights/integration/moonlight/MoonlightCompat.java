package com.baisylia.culturaldelights.integration.moonlight;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import net.mehvahdjukaar.moonlight.api.client.TextureCache;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MoonlightCompat {

    public static void registerWoodTypes() {
        WoodTypeRegistry.INSTANCE.addSimpleFinder(CulturalDelights.MOD_ID, "beanstalk")
                .planks("beanstalk_planks")
                .log("beanstalk")
                .bambooLike(true);
    }

    public static void registerLogTextures() {
        registerLogTextures(ModBlocks.BEANSTALK.get(), "beanstalk");
        registerLogTextures(ModBlocks.STRIPPED_BEANSTALK.get(), "stripped_beanstalk");
    }

    private static void registerLogTextures(Block block, String name) {
        TextureCache.registerSpecialTextureForBlock(block, "_side", ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "block/" + name + "_side"));
        TextureCache.registerSpecialTextureForBlock(block, "_top", ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "block/" + name + "_top"));
    }
}
