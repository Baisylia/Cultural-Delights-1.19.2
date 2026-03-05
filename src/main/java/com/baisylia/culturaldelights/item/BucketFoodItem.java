package com.baisylia.culturaldelights.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BucketFoodItem extends Item {
    public BucketFoodItem(Item.Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entityLiving);
        return entityLiving instanceof Player && ((Player)entityLiving).getAbilities().instabuild ? itemstack : new ItemStack(Items.BUCKET);
    }
}

