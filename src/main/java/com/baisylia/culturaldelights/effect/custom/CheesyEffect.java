package com.baisylia.culturaldelights.effect.custom;

import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CheesyEffect extends MobEffect {

    public CheesyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap map, int amplifier) {
        if (!entity.level.isClientSide) {
            entity.getActiveEffects().stream()
                    .filter(effect -> {
                        var eff = effect.getEffect();

                        // Preserve Alcoholy Side Effects
                        if (eff == this || eff == ModEffects.INTOXICATION.get()) return false;
                        if ((eff == net.minecraft.world.effect.MobEffects.CONFUSION
                                || eff == net.minecraft.world.effect.MobEffects.POISON)
                                && entity.hasEffect(ModEffects.INTOXICATION.get()))
                            return false;

                        return true;
                    })
                    .map(effect -> effect.getEffect())
                    .toList()
                    .forEach(entity::removeEffect);
        }

        super.addAttributeModifiers(entity, map, amplifier);
    }
}