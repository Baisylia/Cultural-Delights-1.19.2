package com.baisylia.culturaldelights.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BucketFoodItem extends Item {
    public BucketFoodItem(Item.Properties properties) {
        super(properties.craftRemainder(Items.BUCKET));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(Items.BUCKET);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entity);
        return entity instanceof Player player && player.getAbilities().instabuild ? itemstack : new ItemStack(Items.BUCKET);
    }
}

