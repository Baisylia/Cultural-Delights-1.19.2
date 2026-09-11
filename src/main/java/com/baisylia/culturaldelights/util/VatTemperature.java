package com.baisylia.culturaldelights.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum VatTemperature implements StringRepresentable {
    HOT("hot"),
    COLD("cold"),
    NORMAL("normal");

    public static final Codec<VatTemperature> CODEC = StringRepresentable.fromEnum(VatTemperature::values);

    private final String name;

    VatTemperature(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}