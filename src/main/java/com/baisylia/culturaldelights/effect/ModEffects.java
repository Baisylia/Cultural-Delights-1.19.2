package com.baisylia.culturaldelights.effect;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.effect.custom.CheesyEffect;
import com.baisylia.culturaldelights.effect.custom.IntoxicationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CulturalDelights.MOD_ID);

    public static final RegistryObject<MobEffect> CHEESY = MOB_EFFECTS.register("cheesy",
            () -> new CheesyEffect(MobEffectCategory.BENEFICIAL, 0xF8CD44));

    public static final RegistryObject<MobEffect> INTOXICATION = MOB_EFFECTS.register("intoxication",
            () -> new IntoxicationEffect(MobEffectCategory.BENEFICIAL, 0xFFFCF2));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}