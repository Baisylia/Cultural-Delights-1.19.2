package com.baisylia.culturaldelights.world.feature;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.FruitingLeaves;
import net.minecraft.core.Registry;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, CulturalDelights.MOD_ID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> AVOCADO_TREE =
            CONFIGURED_FEATURES.register("avocado", () ->
                    new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            BlockStateProvider.simple(ModBlocks.AVOCADO_LOG.get()),
                            new StraightTrunkPlacer(3, 2, 0),
                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ModBlocks.AVOCADO_LEAVES.get().defaultBlockState(),
                                    4).add(ModBlocks.FRUITING_AVOCADO_LEAVES.get().defaultBlockState().setValue(FruitingLeaves.AGE, 4), 1)),
                            new AcaciaFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                            new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
    public static final RegistryObject<ConfiguredFeature<?, ?>> AVOCADO_SPAWN =
            CONFIGURED_FEATURES.register("avocado_spawn", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                            ModPlacedFeatures.AVOCADO_CHECKED.getHolder().get(),
                            0.2F)), ModPlacedFeatures.AVOCADO_CHECKED.getHolder().get())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> AVOCADO_PIT =
            CONFIGURED_FEATURES.register("avocado_pit", () ->
                    new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            BlockStateProvider.simple(ModBlocks.AVOCADO_SAPLING.get()),
                            new StraightTrunkPlacer(3, 2, 0),
                            BlockStateProvider.simple(ModBlocks.AVOCADO_SAPLING.get()),
                            new AcaciaFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                            new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));


    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_CORN = CONFIGURED_FEATURES.register("wild_corn",
            () -> new ConfiguredFeature<>(Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CORN.get()))))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_EGGPLANTS = CONFIGURED_FEATURES.register("wild_eggplants",
            () -> new ConfiguredFeature<>(Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_EGGPLANTS.get()))))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> WILD_CUCUMBERS = CONFIGURED_FEATURES.register("wild_cucumbers",
            () -> new ConfiguredFeature<>(Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CUCUMBERS.get()))))));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
