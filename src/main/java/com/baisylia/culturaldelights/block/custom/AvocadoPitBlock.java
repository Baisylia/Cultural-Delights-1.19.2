package com.baisylia.culturaldelights.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AvocadoPitBlock extends SaplingBlock {
    public static final MapCodec<AvocadoPitBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(TreeGrower.CODEC.fieldOf("tree").forGetter(b -> b.treeGrower), propertiesCodec())
                    .apply(instance, AvocadoPitBlock::new)
    );

    private static final VoxelShape SHAPE_PIT = Block.box(6, 0, 6, 10, 3, 10);

    public AvocadoPitBlock(TreeGrower treeIn, Properties properties) {
        super(treeIn, properties);
    }

    @Override
    public MapCodec<? extends AvocadoPitBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_PIT;
    }
}
