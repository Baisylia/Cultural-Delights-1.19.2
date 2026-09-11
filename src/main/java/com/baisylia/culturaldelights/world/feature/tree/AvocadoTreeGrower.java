package com.baisylia.culturaldelights.world.feature.tree;

import com.baisylia.culturaldelights.world.feature.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class AvocadoTreeGrower {
    public static final TreeGrower AVOCADO_TREE_GROWER = new TreeGrower(
            "culturaldelights:avocado",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.AVOCADO_TREE_KEY),
            Optional.empty()
    );
}