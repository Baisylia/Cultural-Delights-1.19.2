package com.baisylia.culturaldelights.world.feature.tree;

import com.baisylia.culturaldelights.world.feature.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class AvocadoPitGrower {
    public static final TreeGrower AVOCADO_PIT_GROWER = new TreeGrower(
            "culturaldelights:avocado_pit",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.AVOCADO_PIT_KEY),
            Optional.empty()
    );
}