package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.screens.VatMenu;
import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VatRecipeHandler implements StandardRecipeHandler<VatMenu> {

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof VatEmiRecipe;
    }

    @Override
    public List<Slot> getInputSources(VatMenu handler) {
        List<Slot> list = Lists.newArrayList();
        for (int i = 0; i < 43; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(VatMenu handler) {
        List<Slot> list = Lists.newArrayList();
        for (int i = 36; i <= 42; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public @Nullable Slot getOutputSlot(VatMenu handler) {
        return handler.getSlot(43);
    }
}
