package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.recipes.FermenterRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;

public class FermenterEmiRecipe extends AbstractFermenterRecipe {

    public FermenterEmiRecipe(FermenterRecipe recipe) {
        super(recipe.getId(), getIngredients(recipe), recipe.getResultItem(), recipe.getCookTime());
    }

    private static ArrayList<EmiIngredient> getIngredients(FermenterRecipe recipe) {
        ArrayList<EmiIngredient> i = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            i.add(EmiIngredient.of(ingredient));
        }
        return i;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICulturalDelightsPlugin.SHAPELESS_FERMENTING;
    }

    @Override
    public void addWidgets(WidgetHolder builder) {
        builder.addTexture(AbstractFermenterRecipe.TEXTURE, 0, 0, 124, 58, 0, 0);

        builder.addSlot(getInputs().get(0), 2, 2);
        if (getInputs().size() > 1) {
            builder.addSlot(getInputs().get(1), 20, 2);
            if (getInputs().size() > 2) {
                builder.addSlot(getInputs().get(2), 38, 2);
                if (getInputs().size() > 3) {
                    builder.addSlot(getInputs().get(3), 2, 20);
                    if (getInputs().size() > 4) {
                        builder.addSlot(getInputs().get(4), 20, 20);
                        if (getInputs().size() > 5) {
                            builder.addSlot(getInputs().get(5), 38, 20);
                            if (getInputs().size() > 6) {
                                builder.addSlot(getInputs().get(6), 2, 38);
                                    }}}}}}

        drawCookTime(cookTime, builder, 50, getDisplayWidth());
        builder.addSlot(result, 96, 20).recipeContext(this).drawBack(false);
    }
}
