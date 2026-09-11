package com.baisylia.culturaldelights.world.feature;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> AVOCADO_TREE_KEY = registerKey("avocado");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AVOCADO_SPAWN_KEY = registerKey("avocado_spawn");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AVOCADO_PIT_KEY = registerKey("avocado_pit");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AGAVE_KEY = registerKey("agave");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MINT_KEY = registerKey("mint");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CORN_KEY = registerKey("wild_corn");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CUCUMBERS_KEY = registerKey("wild_cucumbers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_EGGPLANTS_KEY = registerKey("wild_eggplants");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_BEANS_KEY = registerKey("wild_beans");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, name));
    }
}
