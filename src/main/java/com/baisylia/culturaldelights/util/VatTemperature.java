package com.baisylia.culturaldelights.util;

import net.minecraft.util.StringRepresentable;

public enum VatTemperature implements StringRepresentable {
    HOT("hot"),
    COLD("cold"),
    NORMAL("normal");

    private final String name;

    VatTemperature(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}