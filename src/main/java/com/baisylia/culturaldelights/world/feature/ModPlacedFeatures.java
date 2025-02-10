package com.baisylia.culturaldelights.world.feature;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CulturalDelights.MOD_ID);

    public static final RegistryObject<PlacedFeature> AVOCADO_CHECKED = PLACED_FEATURES.register("avocado_checked",
            () -> new PlacedFeature(ModConfiguredFeatures.AVOCADO_TREE.getHolder().get(),
                    List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.AVOCADO_SAPLING.get()))));

    public static final RegistryObject<PlacedFeature> AVOCADO_PLACED = PLACED_FEATURES.register("avocado_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.AVOCADO_SPAWN.getHolder().get(), VegetationPlacements.treePlacement(
                    PlacementUtils.countExtra(0, 0.1f, 1))));

    public static final RegistryObject<PlacedFeature> WILD_CORN_PLACED = PLACED_FEATURES.register("wild_corn_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WILD_CORN.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(25),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> WILD_EGGPLANTS_PLACED = PLACED_FEATURES.register("wild_eggplants_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WILD_EGGPLANTS.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(25),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> WILD_CUCUMBERS_PLACED = PLACED_FEATURES.register("wild_cucumbers_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WILD_CUCUMBERS.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(25),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}