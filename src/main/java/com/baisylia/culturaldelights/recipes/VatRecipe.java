package com.baisylia.culturaldelights.recipes;

import com.baisylia.culturaldelights.util.VatTemperature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class VatRecipe implements Recipe<RecipeWrapper> {

    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final VatTemperature temperature;
    private final Ingredient containerItem;
    private final int cookTime;

    public VatRecipe(ItemStack output, NonNullList<Ingredient> recipeItems, VatTemperature temperature, Ingredient containerItem, int cookTime) {
        this.output = output;
        this.recipeItems = recipeItems;
        this.temperature = temperature;
        this.containerItem = containerItem;
        this.cookTime = cookTime;
    }

    public VatTemperature getTemperature() {
        return this.temperature;
    }

    public Ingredient getContainer() {
        return this.containerItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.AGING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.AGING_TYPE.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output;
    }

    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        // Check if output slot is already occupied with a different item
        ItemStack outputSlot = inv.getItem(7);
        if (!outputSlot.isEmpty() && !ItemStack.isSameItemSameComponents(this.output, outputSlot)) {
            return false;
        }
        if (!outputSlot.isEmpty() && outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
            return false;
        }

        // Container
        ItemStack containerSlot = inv.getItem(6);
        boolean containerMatches;
        if (this.containerItem.isEmpty()) {
            containerMatches = containerSlot.isEmpty();
        } else {
            containerMatches = this.containerItem.test(containerSlot);
        }
        if (!containerMatches) {
            return false;
        }

        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        for (int j = 0; j < 6; ++j) {
            ItemStack stack = inv.getItem(j);
            if (!stack.isEmpty()) {
                ++count;
                inputs.add(stack);
            }
        }

        return count == this.recipeItems.size() && RecipeMatcher.findMatches(inputs, this.recipeItems) != null;
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv, HolderLookup.Provider provider) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public static class Serializer implements RecipeSerializer<VatRecipe> {
        public static final StreamCodec<RegistryFriendlyByteBuf, VatRecipe> STREAM_CODEC = StreamCodec.of(
                VatRecipe.Serializer::toNetwork,
                VatRecipe.Serializer::fromNetwork
        );
        private static final MapCodec<VatRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.output),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
                    NonNullList<Ingredient> nonNullList = NonNullList.create();
                    nonNullList.addAll(ingredients);
                    return nonNullList;
                }, ingredients -> ingredients).forGetter(VatRecipe::getIngredients),
                VatTemperature.CODEC.optionalFieldOf("temperature", VatTemperature.NORMAL).forGetter(VatRecipe::getTemperature),
                Ingredient.CODEC.optionalFieldOf("container", Ingredient.EMPTY).forGetter(VatRecipe::getContainer),
                Codec.INT.optionalFieldOf("cooktime", 200).forGetter(VatRecipe::getCookTime)
        ).apply(inst, VatRecipe::new));

        private static VatRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(i, Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            Ingredient container = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            VatTemperature temperature = buffer.readEnum(VatTemperature.class);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
            int cookTime = buffer.readVarInt();
            return new VatRecipe(output, inputs, temperature, container, cookTime);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, VatRecipe recipe) {
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.containerItem);
            buffer.writeEnum(recipe.temperature);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeVarInt(recipe.cookTime);
        }

        @Override
        public MapCodec<VatRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, VatRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}