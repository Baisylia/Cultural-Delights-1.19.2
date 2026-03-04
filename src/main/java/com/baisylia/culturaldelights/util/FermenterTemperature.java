package com.baisylia.culturaldelights.util;

import net.minecraft.util.StringRepresentable;

public enum FermenterTemperature implements StringRepresentable {
    HOT("hot"),
    COLD("cold"),
    NORMAL("normal");

    private final String name;

    FermenterTemperature(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}