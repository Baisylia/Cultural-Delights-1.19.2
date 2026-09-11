package com.baisylia.culturaldelights.effect.custom;

import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class CheesyEffect extends MobEffect {

    public CheesyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            entity.getActiveEffects().stream()
                    .map(MobEffectInstance::getEffect)
                    .filter(eff -> {
                        // Preserve Alcoholy Side Effects
                        if (eff.value() == this || eff.is(ModEffects.INTOXICATION.getKey())) return false;
                        return (!eff.is(MobEffects.CONFUSION) && !eff.is(MobEffects.POISON))
                                || !entity.hasEffect(ModEffects.INTOXICATION);
                    })
                    .toList()
                    .forEach(entity::removeEffect);
        }

        super.onEffectStarted(entity, amplifier);
    }
}
