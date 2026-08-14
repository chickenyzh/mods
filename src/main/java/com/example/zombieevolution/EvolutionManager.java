package com.example.zombieevolution;

import net.minecraft.world.level.Level;

public class EvolutionManager {
    private static int lastAnnouncedStage = -2;

    private static final int[] DAY_THRESHOLDS = {
            0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100
    };
    private static final float[] SPEED_MULTIPLIERS = {
            1.5f, 2.0f, 2.25f, 2.5f, 2.75f, 3.0f, 3.25f, 3.5f, 3.75f, 4.0f, 4.5f
    };
    private static final float[] DAMAGE_VALUES = {
            1.0f, 5.0f, 7.0f, 9.0f, 11.0f, 13.0f, 13.0f, 13.0f, 13.0f, 13.0f, 13.0f
    };
    private static final float[] MAX_HEALTH = {
            20f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f, 100f, 120f
    };
    private static final int[] FOLLOW_RANGES = {
            35, 20, 40, 60, 80, 100, 128, 128, 128, 128, 128
    };

    public static final int SPAWN_BLOCK_DAYS = 1;
    public static final int SUN_IMMUNITY_DAY = 5;

    public static int getCurrentDay(Level world) {
        return (int) (world.getGameTime() / 24000L) + 1;
    }

    public static int getEvolutionStage(int day) {
        if (day < 10) return -1;
        int stage = -1;
        for (int i = 1; i < DAY_THRESHOLDS.length; i++) {
            if (day >= DAY_THRESHOLDS[i]) {
                stage = i;
            } else {
                break;
            }
        }
        return stage;
    }

    public static float getSpeedMultiplierForDay(int day) {
        int stage = getEvolutionStage(day);
        if (stage < 0) return 1.0f;
        return SPEED_MULTIPLIERS[Math.min(stage, SPEED_MULTIPLIERS.length - 1)];
    }

    public static float getDamageForDay(int day) {
        int stage = getEvolutionStage(day);
        if (stage < 0) return 1.0f;
        return DAMAGE_VALUES[Math.min(stage, DAMAGE_VALUES.length - 1)];
    }

    public static float getMaxHealthForDay(int day) {
        int stage = getEvolutionStage(day);
        if (stage < 0) return 20.0f;
        return MAX_HEALTH[Math.min(stage, MAX_HEALTH.length - 1)];
    }

    public static int getFollowRangeForDay(int day) {
        int stage = getEvolutionStage(day);
        if (stage < 0) return 20;
        return FOLLOW_RANGES[Math.min(stage, FOLLOW_RANGES.length - 1)];
    }

    public static int getAnnouncedEvolutionDay(Level world) {
        int day = getCurrentDay(world);
        int stage = getEvolutionStage(day);
        if (stage > lastAnnouncedStage) {
            lastAnnouncedStage = stage;
            if (stage < 0) return -1;
            return DAY_THRESHOLDS[stage];
        }
        return -1;
    }

    public static void resetAnnouncedStage() {
        lastAnnouncedStage = -2;
    }

    public static String getEvolutionChatMessage(int day) {
        return switch (day) {
            case 10 -> "§c✔ 僵尸获得了放置§4TNT§c的能力！小心脚下！";
            case 20 -> "§6✔ 僵尸学会了破坏方块！你的防御不再安全！";
            case 30 -> "§e✔ 僵尸的速度和伤害大幅提升！";
            case 40 -> "§a✔ 僵尸学会了搭建方块！它们可以搭路靠近你！";
            case 50 -> "§b✔ 僵尸必定掉落§d腐化核心§b！可用于合成强力装备！";
            case 60 -> "§d✔ 僵尸学会了爬墙！高处不再安全！";
            case 70 -> "§5✔ 僵尸的追踪范围再次扩大！无处可逃！";
            case 80 -> "§c✔ 僵尸学会了死亡分裂！杀一个变两个！";
            case 90 -> "§4✔ 僵尸全面进化！所有属性达到巅峰！";
            case 100 -> "§6§l✔ 僵尸达到了最终的进化形态！迎接末日吧！";
            default -> "";
        };
    }

    public static boolean canSpawnZombie(int day) {
        return day >= SPAWN_BLOCK_DAYS;
    }

    public static boolean isSunImmunityActive(int day) {
        return day >= SUN_IMMUNITY_DAY;
    }
}
