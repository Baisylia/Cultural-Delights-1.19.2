package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.recipes.FermenterShapedRecipe;
import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class FermenterShapedEmiRecipe extends AbstractFermenterRecipe {

    private final List<Ingredient> ingredients;
    private final int height;
    private final int width;

    public FermenterShapedEmiRecipe(FermenterShapedRecipe recipe) {
        super(recipe.getId(), padIngredients(recipe), recipe.getResultItem(), recipe.getCookTime());
        this.height = recipe.getHeight();
        this.width = recipe.getWidth();
        this.ingredients = recipe.getIngredients();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICulturalDelightsPlugin.SHAPED_FERMENTING;
    }

    @Override
    public void addWidgets(WidgetHolder builder) {
        builder.addTexture(AbstractFermenterRecipe.TEXTURE, 0, 0, 124, 58, 0, 0);
        int startX = 2;
        int startY = 2;
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                builder.addSlot(EmiIngredient.of(ingredients.get(index)), startX + x * 18, startY + y * 18).drawBack(false);
                index++;
            }
        }

        drawCookTime(cookTime, builder, 50, getDisplayWidth());
        builder.addSlot(result, 96, 20).recipeContext(this).drawBack(false);
    }

    private static List<EmiIngredient> padIngredients(FermenterShapedRecipe recipe) {
        List<EmiIngredient> list = Lists.newArrayList();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
                    list.add(EmiStack.EMPTY);
                } else {
                    list.add(EmiIngredient.of(recipe.getIngredients().get(i++)));
                }
            }
        }
        return list;
    }
}
