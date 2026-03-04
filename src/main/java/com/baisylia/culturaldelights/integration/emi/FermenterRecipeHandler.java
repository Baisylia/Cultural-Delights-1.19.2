package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.screens.FermenterMenu;
import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class FermenterRecipeHandler implements StandardRecipeHandler<FermenterMenu> {

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof FermenterEmiRecipe;
    }

    @Override
    public List<Slot> getInputSources(FermenterMenu handler) {
        List<Slot> list = Lists.newArrayList();
        for (int i = 0; i < 43; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(FermenterMenu handler) {
        List<Slot> list = Lists.newArrayList();
        for (int i = 1; i < 8; i++) {
            list.add(handler.getSlot(35+i));
        }
        return list;
    }
}
