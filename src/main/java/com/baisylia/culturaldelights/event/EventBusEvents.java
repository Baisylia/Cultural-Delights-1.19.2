package com.baisylia.culturaldelights.event;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.Objects;

@EventBusSubscriber(modid = CulturalDelights.MOD_ID)
public class EventBusEvents {

    // Cheesers wow
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.CHEESY))
            return;

        // Preserve Alcoholy Side Effects
        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        if (effect.is(ModEffects.CHEESY.getKey()) || effect.is(ModEffects.INTOXICATION.getKey()))
            return;

        if (effect.is(Objects.requireNonNull(MobEffects.POISON.getKey())) || effect.is(Objects.requireNonNull(MobEffects.CONFUSION.getKey()))) {
            if (event.getEntity().hasEffect(ModEffects.INTOXICATION))
                return;
        }

        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }

    // Alcyhols woa
    @SubscribeEvent
    public static void intoxicationStack(MobEffectEvent.Applicable event) {
        if (!event.getEffectInstance().getEffect().is(ModEffects.INTOXICATION.getKey()))
            return;

        var entity = event.getEntity();
        var current = entity.getEffect(ModEffects.INTOXICATION);

        if (current != null) {
            int newAmplifier = current.getAmplifier() + event.getEffectInstance().getAmplifier() + 1;
            if (newAmplifier > 9) {
                newAmplifier = 9;
            }
            int duration = Math.max(
                    current.getDuration(),
                    event.getEffectInstance().getDuration()
            );
            entity.removeEffect(ModEffects.INTOXICATION);
            entity.addEffect(new MobEffectInstance(
                    ModEffects.INTOXICATION,
                    duration,
                    newAmplifier
            ));
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}