package com.baisylia.culturaldelights.integration.supplementaries;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.BeansBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.world.level.block.state.BlockState;

public class SupplementariesCompat {

    public static BlockState getRopeOrStickBeansToPlace(BlockState stateAbove, BlockState defaultBeansState) {
        if (stateAbove.is(ModRegistry.ROPE.get())) {
            return ModBlocks.ROPE_BEANS.get().withPropertiesOf(stateAbove);
        } else if (stateAbove.is(ModRegistry.STICK_BLOCK.get())) {
            return ModBlocks.STICK_BEANS.get().withPropertiesOf(stateAbove);
        }
        return defaultBeansState.setValue(BeansBlock.ROPELOGGED, true);
    }
}
