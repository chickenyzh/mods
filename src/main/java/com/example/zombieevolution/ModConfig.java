package com.example.zombieevolution;

public class ModConfig {
    private static boolean buildingsEnabled = true;
    private static int buildingRarity = 2;

    public static boolean isBuildingsEnabled() {
        return buildingsEnabled;
    }

    public static void setBuildingsEnabled(boolean enabled) {
        buildingsEnabled = enabled;
    }

    public static int getBuildingRarity() {
        return buildingRarity;
    }

    public static void setBuildingRarity(int rarity) {
        buildingRarity = rarity;
    }

    public static int getBuildingRarityChance() {
        return switch (buildingRarity) {
            case 0 -> 48;
            case 1 -> 24;
            case 2 -> 12;
            case 3 -> 6;
            default -> 12;
        };
    }

    public static String getBuildingRarityLabel() {
        return switch (buildingRarity) {
            case 0 -> "非常稀有";
            case 1 -> "稀有";
            case 2 -> "普通";
            case 3 -> "常见";
            default -> "普通";
        };
    }
}
