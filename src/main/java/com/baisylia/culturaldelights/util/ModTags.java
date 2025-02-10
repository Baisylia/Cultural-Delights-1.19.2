package com.baisylia.culturaldelights.util;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> BOWL_FOODS = forgeTag("rollmatout/bowl_foods");
        public static final TagKey<Item> BOTTLE_FOODS = forgeTag("rollmatout/bottle_foods");
        public static final TagKey<Item> BUCKET_FOODS = forgeTag("rollmatout/bucket_foods");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CulturalDelights.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }
}
