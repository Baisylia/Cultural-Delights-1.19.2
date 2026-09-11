package com.baisylia.culturaldelights.util;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> HEAT_SOURCES = tag("heat_sources");
        public static final TagKey<Block> HEAT_CONDUCTORS = tag("heat_conductors");
        public static final TagKey<Block> COLD_SOURCES = tag("cold_sources");
        public static final TagKey<Block> COLD_CONDUCTORS = tag("cold_conductors");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, name));
        }

        private static TagKey<Block> commonTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BOWL_FOODS = commonTag("rollmatout/bowl_foods");
        public static final TagKey<Item> BOTTLE_FOODS = commonTag("rollmatout/bottle_foods");
        public static final TagKey<Item> BUCKET_FOODS = commonTag("rollmatout/bucket_foods");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, name));
        }

        private static TagKey<Item> commonTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
