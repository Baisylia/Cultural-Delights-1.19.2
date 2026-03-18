package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.recipes.VatRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;

public class VatEmiRecipe extends AbstractVatRecipe {

    public VatEmiRecipe(VatRecipe recipe) {
        super(recipe.getId(), getIngredients(recipe), EmiIngredient.of(recipe.getContainer()), recipe.getResultItem(), recipe.getCookTime(), recipe.getTemperature());
    }

    private static ArrayList<EmiIngredient> getIngredients(VatRecipe recipe) {
        ArrayList<EmiIngredient> i = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            i.add(EmiIngredient.of(ingredient));
        }
        return i;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EMICulturalDelightsPlugin.AGING;
    }

    @Override
    public void addWidgets(WidgetHolder builder) {
        builder.addTexture(AbstractVatRecipe.TEXTURE, 0, 0, 124, 58, 0, 0);

        builder.addSlot(getInputs().get(0), 19, 2);
        if (getInputs().size() > 1) {
            builder.addSlot(getInputs().get(1), 37, 2);
            if (getInputs().size() > 2) {
                builder.addSlot(getInputs().get(2), 19, 20);
                if (getInputs().size() > 3) {
                    builder.addSlot(getInputs().get(3), 37, 20);
                    if (getInputs().size() > 4) {
                        builder.addSlot(getInputs().get(4), 19, 38);
                        if (getInputs().size() > 5) {
                            builder.addSlot(getInputs().get(5), 37, 38);
                            }}}}}
        builder.addSlot(container, 95, 38);
        drawCookTime(cookTime, builder, 50, getDisplayWidth());
        builder.addSlot(result, 95, 9).recipeContext(this).drawBack(false);

        int xOffset = switch (temperature) {
            case COLD -> 0;
            case NORMAL -> 13;
            case HOT -> 26;
        };
        builder.addTexture(AbstractVatRecipe.TEXTURE, 5, 6, 12, 46, 126 + xOffset, 20)
                .tooltip((mx, my) -> java.util.List.of(
                        ClientTooltipComponent.create(Component.translatable("container.culturaldelights.vat."
                                + temperature.getSerializedName()).getVisualOrderText())
                ));
    }
}
