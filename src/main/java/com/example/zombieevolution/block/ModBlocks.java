package com.example.zombieevolution.block;

import com.example.zombieevolution.ZombieEvolution;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block STRUCTURE_FOUNDATION = new StructureFoundationBlock(
            BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(-1.0f, 3600000.0f)
                    .noLootTable()
    );

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, ZombieEvolution.id("structure_foundation"), STRUCTURE_FOUNDATION);
        Registry.register(BuiltInRegistries.ITEM, ZombieEvolution.id("structure_foundation"),
                new BlockItem(STRUCTURE_FOUNDATION, new FabricItemSettings().rarity(Rarity.EPIC)));

        ItemGroupEvents.modifyEntriesEvent(net.minecraft.world.item.CreativeModeTabs.BUILDING_BLOCKS)
                .register(entries -> entries.accept(STRUCTURE_FOUNDATION));
    }
}
