package com.baisylia.culturaldelights.recipes;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class OvenShapedRecipe implements Recipe<RecipeWrapper> {

    final ItemStack output;
    private final ShapedRecipePattern pattern;
    private final int cookTime;

    public OvenShapedRecipe(ShapedRecipePattern pattern, ItemStack output, int cookTime) {
        this.output = output;
        this.pattern = pattern;
        this.cookTime = cookTime;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BAKING_SHAPED_SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return pattern.ingredients();
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public boolean matches(RecipeWrapper inv, Level level) {
        ItemStack outputSlot = inv.getItem(9);
        if (!outputSlot.isEmpty() && !ItemStack.isSameItem(this.output, outputSlot)) {
            return false;
        }

        if (!outputSlot.isEmpty() && outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
            return false;
        }

        boolean[][] slotUsed = new boolean[3][3]; // Track which slots are used

        // Iterate over the crafting grid
        for (int offsetX = 0; offsetX <= 3 - this.getWidth(); ++offsetX) {
            for (int offsetY = 0; offsetY <= 3 - this.getHeight(); ++offsetY) {
                if (checkIngredients(inv, offsetX, offsetY, slotUsed)) {
                    if (areOtherSlotsEmpty(inv, offsetX, offsetY)) {
                        return true; // Match found, return true
                    }
                }
            }
        }

        return false; // No match found
    }

    private boolean areOtherSlotsEmpty(RecipeWrapper pContainer, int offsetX, int offsetY) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i < offsetX || i >= offsetX + this.getWidth() || j < offsetY || j >= offsetY + this.getHeight()) {
                    ItemStack itemStack = pContainer.getItem(i + j * 3); // Use a fixed grid size of 3x3
                    if (!itemStack.isEmpty()) {
                        return false; // Slot is not empty
                    }
                }
            }
        }
        return true; // All other slots are empty
    }

    private boolean checkIngredients(RecipeWrapper pContainer, int offsetX, int offsetY, boolean[][] slotUsed) {
        // Iterate over the recipe's dimensions
        for (int i = 0; i < this.getWidth(); ++i) {
            for (int j = 0; j < this.getHeight(); ++j) {
                int gridX = i + offsetX;
                int gridY = j + offsetY;

                // Check if the current position is within the crafting grid
                if (gridX >= 3 || gridY >= 3) {
                    continue;
                }

                // Check if the slot is already used by another recipe
                if (slotUsed[gridX][gridY]) {
                    return false;
                }

                Ingredient recipeIngredient = this.pattern.ingredients().get(i + j * this.getWidth());
                ItemStack gridStack = pContainer.getItem(gridX + gridY * 3); // Use a fixed grid size of 3x3

                // Check if the ingredient matches the item in the crafting grid
                if (!recipeIngredient.test(gridStack)) {
                    return false;
                }

                // Mark the slot as used
                slotUsed[gridX][gridY] = true;
            }
        }

        return true; // All ingredients matched
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv, HolderLookup.Provider provider) {
        return output.copy();
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BAKING_SHAPED.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.OVEN.get());
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((ingredient) -> !ingredient.isEmpty()).anyMatch((ingredient) -> true);
    }

    public static class Serializer implements RecipeSerializer<OvenShapedRecipe> {
        public static final MapCodec<OvenShapedRecipe> CODEC = RecordCodecBuilder.mapCodec((recipe) -> recipe.group(
                ShapedRecipePattern.MAP_CODEC.forGetter((p_311733_) -> p_311733_.pattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_311730_) -> p_311730_.output),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(OvenShapedRecipe::getCookTime)
        ).apply(recipe, OvenShapedRecipe::new));

        public MapCodec<OvenShapedRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, OvenShapedRecipe> streamCodec() {
            return StreamCodec.of(OvenShapedRecipe.Serializer::toNetwork, OvenShapedRecipe.Serializer::fromNetwork);
        }

        public static OvenShapedRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            int cookTimeIn = buffer.readVarInt();
            return new OvenShapedRecipe(shapedrecipepattern, itemstack, cookTimeIn);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, OvenShapedRecipe recipe) {
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
