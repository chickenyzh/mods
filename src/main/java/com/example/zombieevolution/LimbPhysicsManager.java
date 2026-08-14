package com.example.zombieevolution;

public class LimbPhysicsManager {
    private static boolean limbsEnabled = true;
    private static float limbWeight = 1.0f;

    public static boolean isLimbsEnabled() {
        return limbsEnabled;
    }

    public static void setLimbsEnabled(boolean enabled) {
        limbsEnabled = enabled;
    }

    public static float getLimbWeight() {
        return limbWeight;
    }

    public static void setLimbWeight(float weight) {
        limbWeight = weight;
    }
}
