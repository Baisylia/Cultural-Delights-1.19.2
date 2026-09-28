package com.baisylia.culturaldelights.item;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public class ModBoatTypes {
    @SuppressWarnings("unused")
    public static final EnumProxy<Boat.Type> BEANSTALK = new EnumProxy<>(Boat.Type.class,
            (Supplier<Block>) () -> ModBlocks.BEANSTALK_PLANKS.get(),
            CulturalDelights.MOD_ID + ":beanstalk",
            (Supplier<Item>) () -> ModItems.BEANSTALK_RAFT.get(),
            (Supplier<Item>) () -> ModItems.BEANSTALK_CHEST_RAFT.get(),
            (Supplier<Item>) () -> Items.STICK,
            true);
}
