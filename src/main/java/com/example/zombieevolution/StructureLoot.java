package com.example.zombieevolution;

import com.example.zombieevolution.StructureManager.SavedBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public class StructureLoot {

    public enum LootTier {
        BASIC, GOOD, RARE, EPIC
    }

    private static final String[] WEAPON_TYPES = {
            "minecraft:iron_sword", "minecraft:diamond_sword",
            "minecraft:bow", "minecraft:crossbow"
    };
    private static final String[] TOOL_TYPES = {
            "minecraft:iron_pickaxe", "minecraft:diamond_pickaxe",
            "minecraft:iron_axe", "minecraft:diamond_axe"
    };
    private static final String[] ARMOR_TYPES = {
            "minecraft:iron_chestplate", "minecraft:diamond_chestplate",
            "minecraft:iron_boots", "minecraft:diamond_boots"
    };
    private static final String[] CORRUPTED_ARMOR = {
            "zombie_evolution:corrupted_crystal_helmet",
            "zombie_evolution:corrupted_crystal_chestplate",
            "zombie_evolution:corrupted_crystal_leggings",
            "zombie_evolution:corrupted_crystal_boots"
    };

    public static void fillChests(Level world, BlockPos origin, SavedBuilding building, RandomSource random) {
        if (building.nbt == null) return;
        for (String key : building.nbt.keySet()) {
            String[] parts = key.split(",");
            if (parts.length != 3) continue;
            try {
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                int z = Integer.parseInt(parts[2].trim());
                BlockPos chestPos = origin.offset(x, y, z);
                BlockEntity be = world.getBlockEntity(chestPos);
                if (be instanceof RandomizableContainerBlockEntity chest) {
                    LootTier tier = getTierForPosition(x, y, z, random);
                    fillChest(chest, random, tier);
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private static LootTier getTierForPosition(int x, int y, int z, RandomSource random) {
        float roll = random.nextFloat();
        if (roll < 0.4f) return LootTier.BASIC;
        if (roll < 0.65f) return LootTier.GOOD;
        if (roll < 0.85f) return LootTier.RARE;
        return LootTier.EPIC;
    }

    private static void fillChest(RandomizableContainerBlockEntity chest, RandomSource random, LootTier tier) {
        ListTag items = new ListTag();
        int slot = 0;

        slot = addGuaranteedItems(items, random, slot);
        slot = switch (tier) {
            case BASIC -> addBasicLoot(items, random, slot);
            case GOOD -> addGoodLoot(items, random, slot);
            case RARE -> addRareLoot(items, random, slot);
            case EPIC -> addEpicLoot(items, random, slot);
        };

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", items);
        chest.load(nbt);
        chest.setChanged();
    }

    private static int addGuaranteedItems(ListTag items, RandomSource random, int slot) {
        slot = addItem(items, item("minecraft:baked_potato", 1 + random.nextInt(3)), slot);
        slot = addItem(items, item("minecraft:torch", 2 + random.nextInt(4)), slot);
        if (random.nextFloat() < 0.5f) {
            slot = addItem(items, item("minecraft:rotten_flesh", 1 + random.nextInt(3)), slot);
        }
        return slot;
    }

    private static int addBasicLoot(ListTag items, RandomSource random, int slot) {
        slot = addItem(items, item("minecraft:bread", 2 + random.nextInt(4)), slot);
        if (random.nextFloat() < 0.4f)
            slot = addItem(items, item("minecraft:iron_nugget", 3 + random.nextInt(6)), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("minecraft:arrow", 2 + random.nextInt(5)), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("minecraft:stick", 2 + random.nextInt(4)), slot);
        if (random.nextFloat() < 0.2f)
            slot = addItem(items, item("minecraft:string", 1 + random.nextInt(3)), slot);
        if (random.nextFloat() < 0.15f)
            slot = addItem(items, item("minecraft:iron_ingot", 1), slot);
        return slot;
    }

    private static int addGoodLoot(ListTag items, RandomSource random, int slot) {
        slot = addItem(items, item("minecraft:iron_ingot", 2 + random.nextInt(4)), slot);
        slot = addItem(items, item("minecraft:bread", 3 + random.nextInt(5)), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("minecraft:golden_apple", 1), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, enchantedItem("minecraft:iron_sword", "minecraft:sharpness", 1 + random.nextInt(2)), slot);
        if (random.nextFloat() < 0.2f)
            slot = addItem(items, item("minecraft:diamond", 1), slot);
        if (random.nextFloat() < 0.25f)
            slot = addItem(items, item("minecraft:emerald", 1 + random.nextInt(3)), slot);
        if (random.nextFloat() < 0.15f)
            slot = addItem(items, item("minecraft:cobweb", 1 + random.nextInt(2)), slot);
        if (random.nextFloat() < 0.1f)
            slot = addItem(items, item("zombie_evolution:corrupted_core", 1), slot);
        return slot;
    }

    private static int addRareLoot(ListTag items, RandomSource random, int slot) {
        slot = addItem(items, item("minecraft:diamond", 1 + random.nextInt(2)), slot);
        slot = addItem(items, item("minecraft:golden_apple", 1), slot);

        float roll = random.nextFloat();
        if (roll < 0.4f) {
            slot = addItem(items, enchantedItem(pick(random, WEAPON_TYPES), "minecraft:sharpness", 3), slot);
        } else if (roll < 0.7f) {
            slot = addItem(items, enchantedItem(pick(random, TOOL_TYPES), "minecraft:efficiency", 3), slot);
        } else {
            slot = addItem(items, enchantedItem(pick(random, ARMOR_TYPES), "minecraft:protection", 3), slot);
        }

        if (random.nextFloat() < 0.25f)
            slot = addItem(items, item("minecraft:enchanted_golden_apple", 1), slot);
        if (random.nextFloat() < 0.2f)
            slot = addItem(items, item("minecraft:ender_pearl", 1), slot);
        if (random.nextFloat() < 0.2f)
            slot = addItem(items, item("zombie_evolution:corrupted_core", 1), slot);
        if (random.nextFloat() < 0.15f)
            slot = addItem(items, item("zombie_evolution:corrupted_crystal_ingot", 1), slot);
        return slot;
    }

    private static int addEpicLoot(ListTag items, RandomSource random, int slot) {
        slot = addItem(items, item("minecraft:diamond", 2 + random.nextInt(3)), slot);
        slot = addItem(items, item("minecraft:golden_apple", 1 + random.nextInt(2)), slot);

        if (random.nextFloat() < 0.5f) {
            slot = addItem(items, enchantedItem(
                    "minecraft:diamond_sword",
                    new String[]{"minecraft:sharpness", "minecraft:unbreaking"},
                    new int[]{3, 2}
            ), slot);
        } else {
            slot = addItem(items, enchantedItem(
                    "minecraft:diamond_pickaxe",
                    new String[]{"minecraft:efficiency", "minecraft:unbreaking"},
                    new int[]{3, 2}
            ), slot);
        }

        if (random.nextFloat() < 0.33f)
            slot = addItem(items, enchantedItem(
                    "minecraft:diamond_chestplate",
                    new String[]{"minecraft:protection", "minecraft:unbreaking"},
                    new int[]{3, 2}
            ), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("minecraft:enchanted_golden_apple", 1), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("minecraft:arrow", 8 + random.nextInt(8)), slot);
        slot = addItem(items, item("zombie_evolution:corrupted_core", 1 + random.nextInt(2)), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("zombie_evolution:corrupted_crystal_ingot", 1), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("zombie_evolution:corrupted_crystal_sword", 1), slot);
        if (random.nextFloat() < 0.3f)
            slot = addItem(items, item("zombie_evolution:corrupted_crystal_pickaxe", 1), slot);
        if (random.nextFloat() < 0.2f)
            slot = addItem(items, item(pick(random, CORRUPTED_ARMOR), 1), slot);
        if (random.nextFloat() < 0.15f)
            slot = addItem(items, item("minecraft:experience_bottle", 4 + random.nextInt(8)), slot);
        return slot;
    }

    private static CompoundTag item(String id, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id);
        tag.putByte("Count", (byte) count);
        return tag;
    }

    private static CompoundTag enchantedItem(String id, String enchId, int level) {
        CompoundTag tag = item(id, 1);
        CompoundTag enchantTag = new CompoundTag();
        ListTag enchantList = new ListTag();
        CompoundTag enchant = new CompoundTag();
        enchant.putString("id", enchId);
        enchant.putInt("lvl", level);
        enchantList.add(enchant);
        enchantTag.put("Enchantments", enchantList);
        tag.put("tag", enchantTag);
        return tag;
    }

    private static CompoundTag enchantedItem(String id, String[] enchIds, int[] levels) {
        CompoundTag tag = item(id, 1);
        ListTag enchantList = new ListTag();
        for (int i = 0; i < enchIds.length; i++) {
            CompoundTag enchant = new CompoundTag();
            enchant.putString("id", enchIds[i]);
            enchant.putInt("lvl", levels[i]);
            enchantList.add(enchant);
        }
        CompoundTag enchantTag = new CompoundTag();
        enchantTag.put("Enchantments", enchantList);
        tag.put("tag", enchantTag);
        return tag;
    }

    private static String pick(RandomSource random, String[] array) {
        return array[random.nextInt(array.length)];
    }

    private static int addItem(ListTag items, CompoundTag item, int slot) {
        item.putByte("Slot", (byte) slot);
        items.add(item);
        return slot + 1;
    }
}
