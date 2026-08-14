package com.example.zombieevolution;

import com.example.zombieevolution.StructureManager.SavedBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModStructures {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModStructures");
    private static final List<SavedBuilding> BUILDINGS = new ArrayList<>();
    private static SavedBuilding defaultShelter;

    public static void register() {
        BUILDINGS.clear();
        Path structuresDir = Path.of("config", "zombie_evolution", "structures");
        if (!Files.exists(structuresDir)) {
            try {
                Files.createDirectories(structuresDir);
            } catch (IOException e) {
                LOGGER.error("Failed to create structures directory", e);
            }
            return;
        }
        try (var stream = Files.list(structuresDir)) {
            stream.filter(p -> p.toString().endsWith(".nbt")).forEach(p -> {
                String name = p.getFileName().toString().replace(".nbt", "");
                try {
                    var tag = NbtIo.readCompressed(p.toFile());
                    if (tag != null && tag.contains("size", 9)) {
                        var sizeList = tag.getList("size", 3);
                        int w = sizeList.getInt(0);
                        int h = sizeList.getInt(1);
                        int d = sizeList.getInt(2);
                        BUILDINGS.add(new SavedBuilding(name, new int[]{w, h, d}));
                        LOGGER.info("Loaded building: {} ({}x{}x{})", name, w, h, d);
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to load building structure: {}", p, e);
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to list structures directory", e);
        }
        LOGGER.info("Loaded {} buildings", BUILDINGS.size());
    }

    public static SavedBuilding getRandomBuilding(RandomSource random) {
        if (BUILDINGS.isEmpty()) return null;
        return BUILDINGS.get(random.nextInt(BUILDINGS.size()));
    }

    public static SavedBuilding getShelterBuilding() {
        for (SavedBuilding b : BUILDINGS) {
            if (b.name.equals("shelter")) {
                return b;
            }
        }
        if (defaultShelter == null) {
            defaultShelter = createDefaultShelter();
        }
        return defaultShelter;
    }

    private static SavedBuilding createDefaultShelter() {
        String palette = "{\"#\":\"minecraft:stone\",\".\":\"minecraft:air\",\"S\":\"minecraft:sea_lantern\",\"C\":\"minecraft:chest\"}";

        String blocks =
                "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########\n" +
                "\n" +
                "#.......#\n" + "#.......#\n" + "#..C....#\n" + "#...S...#\n" + "#..C....#\n" + "#.......#\n" + "#.......#\n" +
                "\n" +
                "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" +
                "\n" +
                "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" + "#.......#\n" +
                "\n" +
                "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########\n" + "#########";

        Map<String, String> nbt = new HashMap<>();
        nbt.put("2,1,2", "");
        nbt.put("2,1,4", "");

        LOGGER.info("Using default built-in shelter");
        return new SavedBuilding("shelter", new int[]{9, 5, 7}, palette, blocks, nbt);
    }

    public static SavedBuilding placeRandomBuilding(LevelAccessor level, BlockPos pos, RandomSource random) {
        if (BUILDINGS.isEmpty()) return null;
        SavedBuilding building = BUILDINGS.get(random.nextInt(BUILDINGS.size()));
        Path filePath = Path.of("config", "zombie_evolution", "structures", building.name + ".nbt");
        if (!Files.exists(filePath)) return null;

        try {
            var tag = NbtIo.readCompressed(filePath.toFile());
            if (tag == null) return null;
            var template = new StructureTemplate();
            template.load(BuiltInRegistries.BLOCK.asLookup(), tag);
            var placeSettings = new StructurePlaceSettings();
            template.placeInWorld((ServerLevelAccessor) level, pos, pos, placeSettings, random, 2);
            return building;
        } catch (IOException e) {
            LOGGER.error("Failed to place building: {}", building.name, e);
            return null;
        }
    }
}
