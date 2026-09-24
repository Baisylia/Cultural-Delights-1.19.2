package com.baisylia.culturaldelights.item.custom;

import com.baisylia.culturaldelights.item.ModDataComponents;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.sound.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.baisylia.culturaldelights.util.ModTags;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public class MarshmallowOnAStickItem extends Item {
    public static final int COOKING_TIME = 100;

    public MarshmallowOnAStickItem(Item.Properties properties) {
        super(properties);
    }

    private static boolean isPlayerNearHeatSource(Player player, LevelReader level) {
        if (player.isOnFire()) {
            return true;
        }

        BlockPos pos = player.blockPosition();
        for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(nearbyPos);
            if (state.is(ModTags.Blocks.HEAT_SOURCES) ||
                state.is(BlockTags.CAMPFIRES) ||
                state.is(BlockTags.FIRE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (canCookOrIsCooking(stack)) {
            int fireAspectLevel = ItemUtils.getValidatedEnchantmentLevel(Enchantments.FIRE_ASPECT, entity.level().registryAccess(), stack);
            int cookingTime = stack.getOrDefault(ModDataComponents.COOK_TIME, COOKING_TIME);
            return SkilletBlock.getSkilletCookingTime(cookingTime, fireAspectLevel);
        } else {
            return super.getUseDuration(stack, entity);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack cookingStack = player.getItemInHand(hand);
        boolean cooking = cookingStack.has(ModDataComponents.COOK_TIME);
        if (isPlayerNearHeatSource(player, level)) {
            if (cooking) {
                player.startUsingItem(hand);
                return InteractionResultHolder.pass(cookingStack);
            }

            Optional<ItemStack> recipe = getCookingRecipe(cookingStack);
            if (recipe.isPresent()) {
                cookingStack.set(ModDataComponents.COOK_TIME, COOKING_TIME);
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(cookingStack);
            }
            return InteractionResultHolder.pass(cookingStack);
        } else {
            cookingStack.remove(ModDataComponents.COOK_TIME);
            return super.use(level, player, hand);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        if (canCookOrIsCooking(itemStack)) return UseAnim.NONE;
        if (itemStack.has(DataComponents.FOOD)) return UseAnim.EAT;
        return UseAnim.NONE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (entity instanceof Player player && level.random.nextInt(50) == 0) {
            playLocalSound(level, player, ModSounds.MARSHMALLOW_SIZZLE.get());
        }
    }

    private static void playLocalSound(Level level, Player player, SoundEvent sound) {
        Vec3 pos = player.position();
        double x = pos.x() + 0.5D;
        double y = pos.y();
        double z = pos.z() + 0.5D;
        level.playLocalSound(x, y, z, sound, SoundSource.BLOCKS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F, false);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player) {
            stack.remove(ModDataComponents.COOK_TIME);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (stack.has(ModDataComponents.COOK_TIME)) {
                Optional<ItemStack> cookingRecipe = getCookingRecipe(stack);
                cookingRecipe.ifPresent(resultStack -> {
                    resultStack.setCount(1);

                    if (resultStack.is(ModItems.CHARRED_MARSHMALLOW_ON_A_STICK.get())) {
                        playLocalSound(level, player, ModSounds.MARSHMALLOW_CHAR.get());
                    } else if (resultStack.is(ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get())) {
                        playLocalSound(level, player, ModSounds.MARSHMALLOW_CARAMELIZE.get());
                    }

                    if (stack.getCount() == 1 && entity.getItemInHand(InteractionHand.MAIN_HAND).equals(stack)) {
                        entity.setItemInHand(InteractionHand.MAIN_HAND, resultStack);
                    } else if (stack.getCount() == 1 && entity.getItemInHand(InteractionHand.OFF_HAND).equals(stack)) {
                        entity.setItemInHand(InteractionHand.OFF_HAND, resultStack);
                    } else {
                        stack.shrink(1);
                        if (!player.getInventory().add(resultStack)) {
                            player.drop(resultStack, false);
                        }
                    }

                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
                    }
                });
                stack.remove(ModDataComponents.COOK_TIME);
            } else {
                return super.finishUsingItem(stack, level, entity);
            }
        }

        return stack;
    }

    public static Optional<ItemStack> getCookingRecipe(final ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();

        Item newItem;
        if (stack.is(ModItems.MARSHMALLOW_ON_A_STICK.get())) {
            newItem = ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get();
        } else if (stack.is(ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get())) {
            newItem = ModItems.CHARRED_MARSHMALLOW_ON_A_STICK.get();
        } else {
            return Optional.empty();
        }
        return Optional.of(new ItemStack(newItem));
    }

    public static boolean canCookOrIsCooking(LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof Player player) {
            return isPlayerNearHeatSource(player, player.level());
        }
        return canCookOrIsCooking(stack);
    }

    public static boolean canCookOrIsCooking(ItemStack stack) {
        if (stack.is(ModItems.CHARRED_MARSHMALLOW_ON_A_STICK.get())) {
            return false;
        }
        return stack.has(ModDataComponents.COOK_TIME);
    }
}
