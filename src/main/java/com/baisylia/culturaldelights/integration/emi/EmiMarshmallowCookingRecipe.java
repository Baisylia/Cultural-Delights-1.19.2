package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.CulturalDelights;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiCookingRecipe;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EmiMarshmallowCookingRecipe extends EmiCookingRecipe {
    private final ResourceLocation id;
    private final int cookingTime;
    private final Item input;
    private final Item output;

    public EmiMarshmallowCookingRecipe(int cookingTime, Item input, Item output) {
        super(new CampfireCookingRecipe("", CookingBookCategory.FOOD, Ingredient.of(input), new ItemStack(output), 0.0F, cookingTime), VanillaEmiRecipeCategories.CAMPFIRE_COOKING, 0, true);
        this.id = ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "/" + BuiltInRegistries.ITEM.getKey(output).getPath());
        this.cookingTime = cookingTime;
        this.input = input;
        this.output = output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(24, 5, 50 * cookingTime).tooltip((mx, my) -> List.of(ClientTooltipComponent.create(EmiPort.ordered(EmiPort.translatable("emi.cooking.time", (float) cookingTime / 20.0F)))));
        widgets.addTexture(EmiTexture.FULL_FLAME, 1, 24);

        widgets.addSlot(EmiStack.of(input), 0, 4);
        widgets.addSlot(EmiStack.of(output), 56, 0).large(true).recipeContext(this).appendTooltip(Component.translatable("emi.culturaldelights.marshmallow_on_a_stick.help"));
    }
}
