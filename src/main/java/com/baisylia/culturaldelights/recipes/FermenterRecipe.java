package com.baisylia.culturaldelights.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.List;

public class FermenterRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int cookTime;
    private final boolean isSimple;

    public FermenterRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int cookTime) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.cookTime = cookTime;
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        // Check if output slot is already occupied with a different item
        ItemStack outputSlot = pContainer.getItem(9);
        if (!outputSlot.isEmpty() && !ItemStack.isSame(this.getResultItem(), outputSlot)) {
            return false;
        }

        // Check if output slot is full
        if (!outputSlot.isEmpty() && outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
            return false;
        }
        StackedContents stackedcontents = new StackedContents();
        List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < 9; ++j) {
            ItemStack itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
            //stackedcontents.accountStack(itemstack, 1);
        }
            //return i >= this.recipeItems.size() && (isSimple ? stackedcontents.canCraft(this, null) :
                //RecipeMatcher.findMatches(inputs, this.recipeItems) != null);

            //return i >= this.recipeItems.size() && RecipeMatcher.findMatches(inputs, this.recipeItems) != null;
        return i == this.recipeItems.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : RecipeMatcher.findMatches(inputs,  this.recipeItems) != null);
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FermenterRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "FERMENTING";
    }


   public static class Serializer implements RecipeSerializer<FermenterRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final ResourceLocation NAME = new ResourceLocation("culturaldelights", "fermenting");
        public FermenterRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            NonNullList<Ingredient> inputs = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (inputs.isEmpty()) {
                throw new JsonParseException("No ingredients for FERMENTING recipe");
            } else if (inputs.size() > 9) {
                throw new JsonParseException("Too many ingredients for FERMENTING recipe. The maximum is 9");
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                int cookTimeIn = GsonHelper.getAsInt(json, "cooktime", 200);
                return new FermenterRecipe(resourceLocation, itemstack, inputs, cookTimeIn);
            }
        }


        private static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }
       @Override
       public FermenterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
           //String s = buf.readUtf();
           int i = buf.readVarInt();
           NonNullList<Ingredient> inputs = NonNullList.withSize(i, Ingredient.EMPTY);

           for(int j = 0; j < inputs.size(); ++j) {
               inputs.set(j, Ingredient.fromNetwork(buf));
           }

           ItemStack itemstack = buf.readItem();
           int cookTimeIn = buf.readVarInt();
           return new FermenterRecipe(id, itemstack, inputs, cookTimeIn);
       }

       @Override
       public void toNetwork(FriendlyByteBuf buf, FermenterRecipe recipe) {
           buf.writeVarInt(recipe.recipeItems.size());

           for(Ingredient ingredient : recipe.getIngredients()) {
               ingredient.toNetwork(buf);
           }

           buf.writeItem(recipe.getResultItem());
           buf.writeVarInt(recipe.cookTime);
       }
    }
}