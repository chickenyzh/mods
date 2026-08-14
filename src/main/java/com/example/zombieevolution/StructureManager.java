package com.example.zombieevolution;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StructureManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("StructureManager");

    public static String saveStructure(Level level, BlockPos pos, String name) {
        StructureTemplate template = new StructureTemplate();
        BlockPos startPos = pos.above();
        Vec3i size = new Vec3i(16, 16, 16);

        template.fillFromWorld(level, startPos, size, false, Blocks.AIR);

        CompoundTag tag = template.save(new CompoundTag());
        if (tag.isEmpty()) {
            return null;
        }

        Path saveDir = Path.of("config", "zombie_evolution", "structures");
        try {
            Files.createDirectories(saveDir);
            Path filePath = saveDir.resolve(name + ".nbt");
            NbtIo.writeCompressed(tag, filePath.toFile());
            LOGGER.info("Structure saved: {}", filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            LOGGER.error("Failed to save structure", e);
            return null;
        }
    }

    public static boolean placeBuilding(Level level, BlockPos origin, SavedBuilding building) {
        if (building.palette != null && building.blocks != null) {
            return placeBuildingFromPalette(level, origin, building);
        }
        Path filePath = Path.of("config", "zombie_evolution", "structures", building.name + ".nbt");
        if (!Files.exists(filePath)) return false;
        try {
            CompoundTag tag = NbtIo.readCompressed(filePath.toFile());
            if (tag == null) return false;
            StructureTemplate template = new StructureTemplate();
            template.load(BuiltInRegistries.BLOCK.asLookup(), tag);
            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld((ServerLevelAccessor) level, origin, origin, settings, RandomSource.create(), 2);
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to place building: {}", building.name, e);
            return false;
        }
    }

    private static boolean placeBuildingFromPalette(Level level, BlockPos origin, SavedBuilding building) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> palette = gson.fromJson(building.palette, type);
            if (palette == null) return false;

            String[] layers = building.blocks.split("\n\n", -1);

            for (int y = 0; y < layers.length; y++) {
                String[] rows = layers[y].split("\n", -1);
                for (int z = 0; z < rows.length; z++) {
                    String row = rows[z];
                    for (int x = 0; x < row.length(); x++) {
                        String key = String.valueOf(row.charAt(x));
                        String blockId = palette.get(key);
                        if (blockId == null || blockId.isEmpty()) continue;
                        if (blockId.equals("minecraft:air")) continue;

                        Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockId));
                        if (block != null && block != Blocks.AIR) {
                            BlockPos targetPos = origin.offset(x, y, z);
                            if (level.isEmptyBlock(targetPos) || level.getBlockState(targetPos).canBeReplaced()) {
                                level.setBlock(targetPos, block.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to place building from palette", e);
            return false;
        }
    }

    public static class SavedBuilding {
        public final String name;
        public final int[] size;
        public String palette;
        public String blocks;
        public Map<String, String> nbt;

        public SavedBuilding(String name, int[] size) {
            this.name = name;
            this.size = size;
        }

        public SavedBuilding(String name, int[] size, String palette, String blocks, Map<String, String> nbt) {
            this.name = name;
            this.size = size;
            this.palette = palette;
            this.blocks = blocks;
            this.nbt = nbt;
        }
    }
}
