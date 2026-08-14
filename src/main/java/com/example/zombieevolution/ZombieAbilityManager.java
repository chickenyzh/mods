package com.example.zombieevolution;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ZombieAbilityManager {

    public static final int ABILITY_NONE = 0;
    public static final int ABILITY_TNT = 1;
    public static final int ABILITY_BREAK_BLOCKS = 2;
    public static final int ABILITY_BUILD = 4;
    public static final int ABILITY_CLIMB = 8;
    public static final int ABILITY_SPLIT = 16;

    public static final int TNT_UNLOCK_DAY = 10;
    public static final int BREAK_BLOCKS_UNLOCK_DAY = 20;
    public static final int BUILD_UNLOCK_DAY = 40;
    public static final int CLIMB_UNLOCK_DAY = 60;
    public static final int SPLIT_UNLOCK_DAY = 80;

    private static final Block[] BREAKABLE_BLOCKS = {
            Blocks.DIRT, Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE,
            Blocks.ANDESITE, Blocks.COBBLESTONE,
            Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS,
            Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS,
            Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG,
            Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG,
            Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD,
            Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD,
            Blocks.OAK_STAIRS, Blocks.COBBLESTONE_STAIRS, Blocks.STONE_STAIRS,
            Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS,
            Blocks.MUD_BRICK_STAIRS,
            Blocks.PACKED_MUD,
            Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS,
            Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS,
            Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL,
            Blocks.BRICK_WALL, Blocks.STONE_BRICK_WALL,
            Blocks.COBBLESTONE_SLAB, Blocks.OAK_SLAB, Blocks.STONE_SLAB,
            Blocks.SMOOTH_STONE_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.BRICK_SLAB,
            Blocks.GLASS, Blocks.GLASS_PANE,
            Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE,
            Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL,
            Blocks.GRAVEL, Blocks.SAND,
            Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE,
            Blocks.SMOOTH_SANDSTONE,
            Blocks.RED_SAND, Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE,
            Blocks.CUT_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE,
            Blocks.OAK_DOOR, Blocks.IRON_DOOR, Blocks.LADDER,
            Blocks.CHEST, Blocks.BARREL, Blocks.FURNACE, Blocks.CRAFTING_TABLE,
            Blocks.TORCH, Blocks.WALL_TORCH, Blocks.LANTERN,
            Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE,
            Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS,
            Blocks.WHITE_WOOL,
            Blocks.OAK_TRAPDOOR, Blocks.IRON_BARS,
            Blocks.BRICKS
    };

    public static double getAbilitySpawnChance(int day) {
        return Math.min(day / 10.0 * 0.1, 1.0);
    }

    public static int getAbilitiesForDay(int day) {
        int flags = 0;
        if (day >= BREAK_BLOCKS_UNLOCK_DAY) flags |= ABILITY_BREAK_BLOCKS;
        if (day >= TNT_UNLOCK_DAY) flags |= ABILITY_TNT;
        if (day >= BUILD_UNLOCK_DAY) flags |= ABILITY_BUILD;
        if (day >= CLIMB_UNLOCK_DAY) flags |= ABILITY_CLIMB;
        if (day >= SPLIT_UNLOCK_DAY) flags |= ABILITY_SPLIT;
        return flags;
    }

    public static boolean canBreakBlock(BlockPos pos, Level world) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return false;
        Block block = state.getBlock();
        for (Block b : BREAKABLE_BLOCKS) {
            if (b == block) return true;
        }
        return false;
    }
}
