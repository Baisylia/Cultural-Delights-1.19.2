package com.baisylia.culturaldelights.world.feature.tree;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.world.feature.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class LemonTreeGrower {
    public static final TreeGrower LEMON_TREE_GROWER = new TreeGrower(
            CulturalDelights.MOD_ID + ":lemon",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.LEMON_KEY),
            Optional.empty()
    );
}
