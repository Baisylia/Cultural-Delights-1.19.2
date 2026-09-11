package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StickBeansBlock extends BeansBlock {
    public static final BooleanProperty AXIS_X = ModBlockProperties.AXIS_X;
    public static final BooleanProperty AXIS_Z = ModBlockProperties.AXIS_Z;
    public static final MapCodec<StickBeansBlock> CODEC = simpleCodec(StickBeansBlock::new);

    public StickBeansBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(this.getAgeProperty(), 0)
                .setValue(ROPELOGGED, false)
                .setValue(AXIS_X, false)
                .setValue(AXIS_Z, false));
    }

    public static BlockState fromBeans(BlockState beansState) {
        return ModBlocks.STICK_BEANS.get().withPropertiesOf(beansState).setValue(ROPELOGGED, false);
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS_X, AXIS_Z);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return StickBlock.getStickShape(state.getValue(AXIS_X), true, state.getValue(AXIS_Z));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.isSecondaryUseActive() && context.getItemInHand().is(Items.STICK)) {
            return switch (context.getClickedFace().getAxis()) {
                case Z -> !state.getValue(AXIS_Z);
                case X -> !state.getValue(AXIS_X);
                default -> false;
            };
        }
        return super.canBeReplaced(state, context);
    }
}
