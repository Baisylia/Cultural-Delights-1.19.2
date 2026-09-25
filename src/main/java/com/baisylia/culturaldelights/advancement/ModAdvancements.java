package com.baisylia.culturaldelights.advancement;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAdvancements {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CulturalDelights.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, SaltGrowthTrigger> GROW_SALT_SPIKE =
            TRIGGERS.register("grow_salt_spike", SaltGrowthTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }
}
