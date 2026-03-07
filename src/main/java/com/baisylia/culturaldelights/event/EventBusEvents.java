package com.baisylia.culturaldelights.event;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CulturalDelights.MOD_ID)
public class EventBusEvents {

    // Cheesers wow
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.CHEESY.get()))
            return;

        // Preserve Alcoholy Side Effects
        var effect = event.getEffectInstance().getEffect();
        if (effect == ModEffects.CHEESY.get() || effect == ModEffects.INTOXICATION.get())
            return;

        if (effect == net.minecraft.world.effect.MobEffects.POISON
                || effect == net.minecraft.world.effect.MobEffects.CONFUSION) {
            if (event.getEntity().hasEffect(ModEffects.INTOXICATION.get()))
                return;
        }

        event.setResult(Event.Result.DENY);
    }

    // Alcyhols woa
    @SubscribeEvent
    public static void intoxicationStack(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() != ModEffects.INTOXICATION.get())
            return;

        var entity = event.getEntity();
        var current = entity.getEffect(ModEffects.INTOXICATION.get());

        if (current != null) {
            int newAmplifier = current.getAmplifier() + event.getEffectInstance().getAmplifier() + 1;
            if (newAmplifier > 9) {
                newAmplifier = 9;
            }
            int duration = Math.max(
                    current.getDuration(),
                    event.getEffectInstance().getDuration()
            );
            entity.removeEffect(ModEffects.INTOXICATION.get());
            entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    ModEffects.INTOXICATION.get(),
                    duration,
                    newAmplifier
            ));
            event.setResult(Event.Result.DENY);
        }
    }
}