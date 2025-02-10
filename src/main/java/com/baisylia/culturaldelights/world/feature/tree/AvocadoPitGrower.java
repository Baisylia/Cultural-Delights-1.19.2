package com.baisylia.culturaldelights.world.feature.tree;

import com.baisylia.culturaldelights.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class AvocadoPitGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean pLargeHive) {
        return ModConfiguredFeatures.AVOCADO_PIT.getHolder().get();
    }
}