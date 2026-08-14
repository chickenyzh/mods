package com.example.zombieevolution;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class ZombieEquipmentManager {

    public static int getEquipmentTier(int day) {
        if (day >= 90) return 3;
        if (day >= 70) return 2;
        if (day >= 50) return 1;
        if (day >= 30) return 0;
        return -1;
    }

    public static void applyEquipment(Zombie zombie, int day) {
        int tier = getEquipmentTier(day);
        switch (tier) {
            case 0 -> equipIron(zombie);
            case 1 -> equipDiamond(zombie);
            case 2 -> equipEnchantedDiamond(zombie);
            case 3 -> equipNetherite(zombie);
        }
    }

    private static void equipIron(Zombie zombie) {
        zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        zombie.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        zombie.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        zombie.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    private static void equipDiamond(Zombie zombie) {
        zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        zombie.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        zombie.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        zombie.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
    }

    private static void equipEnchantedDiamond(Zombie zombie) {
        zombie.setItemSlot(EquipmentSlot.HEAD, makeHelmet(Items.DIAMOND_HELMET, 4, 3, 2, 2, 1));
        zombie.setItemSlot(EquipmentSlot.CHEST, makeArmor(Items.DIAMOND_CHESTPLATE, 4, 3, 2));
        zombie.setItemSlot(EquipmentSlot.LEGS, makeArmor(Items.DIAMOND_LEGGINGS, 4, 3, 2));
        zombie.setItemSlot(EquipmentSlot.FEET, makeBoots(Items.DIAMOND_BOOTS, 4, 3, 2, 3));
        zombie.setItemSlot(EquipmentSlot.MAINHAND, makeSword(Items.DIAMOND_SWORD, 4, 2, 2));
    }

    private static void equipNetherite(Zombie zombie) {
        zombie.setItemSlot(EquipmentSlot.HEAD, makeHelmet(Items.NETHERITE_HELMET, 4, 3, 3, 3, 1));
        zombie.setItemSlot(EquipmentSlot.CHEST, makeArmor(Items.NETHERITE_CHESTPLATE, 4, 3, 3));
        zombie.setItemSlot(EquipmentSlot.LEGS, makeArmor(Items.NETHERITE_LEGGINGS, 4, 3, 3));
        zombie.setItemSlot(EquipmentSlot.FEET, makeBoots(Items.NETHERITE_BOOTS, 4, 3, 3, 4));
        zombie.setItemSlot(EquipmentSlot.MAINHAND, makeSword(Items.NETHERITE_SWORD, 5, 2, 2));
    }

    private static ItemStack makeArmor(Item item, int prot, int unbreaking, int thorns) {
        ItemStack stack = new ItemStack(item);
        if (prot > 0) stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, prot);
        if (unbreaking > 0) stack.enchant(Enchantments.UNBREAKING, unbreaking);
        if (thorns > 0) stack.enchant(Enchantments.THORNS, thorns);
        return stack;
    }

    private static ItemStack makeHelmet(Item item, int prot, int unbreaking, int thorns, int respiration, int aquaAffinity) {
        ItemStack stack = makeArmor(item, prot, unbreaking, thorns);
        if (respiration > 0) stack.enchant(Enchantments.RESPIRATION, respiration);
        if (aquaAffinity > 0) stack.enchant(Enchantments.AQUA_AFFINITY, aquaAffinity);
        return stack;
    }

    private static ItemStack makeBoots(Item item, int prot, int unbreaking, int thorns, int featherFalling) {
        ItemStack stack = makeArmor(item, prot, unbreaking, thorns);
        if (featherFalling > 0) stack.enchant(Enchantments.FALL_PROTECTION, featherFalling);
        return stack;
    }

    private static ItemStack makeSword(Item item, int sharpness, int knockback, int fireAspect) {
        ItemStack stack = new ItemStack(item);
        if (sharpness > 0) stack.enchant(Enchantments.SHARPNESS, sharpness);
        if (knockback > 0) stack.enchant(Enchantments.KNOCKBACK, knockback);
        if (fireAspect > 0) stack.enchant(Enchantments.FIRE_ASPECT, fireAspect);
        return stack;
    }
}
