package com.example.zombieevolution.block;

import com.example.zombieevolution.StructureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StructureFoundationBlock extends Block {

    private static int buildingCounter = 0;

    public StructureFoundationBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        showScanRange(world, pos);

        if (!world.isClientSide) {
            buildingCounter++;
            String name = "building_" + buildingCounter;
            String filePath = StructureManager.saveStructure(world, pos, name);

            if (filePath != null) {
                long fileSize = new java.io.File(filePath).length();
                player.displayClientMessage(Component.literal("§a✔ 建筑已保存 §7(" + (fileSize / 1024 + 1) + "KB)"), false);
                player.displayClientMessage(Component.literal("§e文件名: " + name + ".json"), false);
                player.displayClientMessage(Component.literal("§7📁 " + filePath), false);
            } else {
                player.displayClientMessage(Component.literal("§c✘ 上方没有检测到方块！请先搭建建筑"), false);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void showScanRange(Level world, BlockPos pos) {
        BlockPos start = pos.above();
        int w = 16, h = 16, d = 16;

        BlockPos[] corners = new BlockPos[8];
        corners[0] = start;
        corners[1] = start.offset(w - 1, 0, 0);
        corners[2] = start.offset(0, 0, d - 1);
        corners[3] = start.offset(w - 1, 0, d - 1);
        corners[4] = start.offset(0, h - 1, 0);
        corners[5] = start.offset(w - 1, h - 1, 0);
        corners[6] = start.offset(0, h - 1, d - 1);
        corners[7] = start.offset(w - 1, h - 1, d - 1);

        // Vertical edges
        for (int i = 0; i < 4; i++) {
            BlockPos bottom = corners[i];
            BlockPos top = corners[i + 4];
            int yStep = Math.max(1, h / 8);
            for (int y = 0; y < h; y += yStep) {
                spawnParticle(world, bottom.getX(), bottom.getY() + y, bottom.getZ());
            }
            spawnParticle(world, top.getX(), top.getY(), top.getZ());
        }

        // Horizontal edges (bottom + top)
        for (int i = 0; i < 4; i++) {
            BlockPos from = corners[i];
            BlockPos to = corners[(i + 1) % 4];
            int steps = Math.max(1, w / 2);
            for (int s = 0; s <= steps; s++) {
                double t = (double) s / steps;
                int x = (int) (from.getX() + t * (to.getX() - from.getX()));
                int z = (int) (from.getZ() + t * (to.getZ() - from.getZ()));
                spawnParticle(world, x, from.getY(), z);
                spawnParticle(world, x, from.getY() + h - 1, z);
            }
        }
    }

    private void spawnParticle(Level world, int x, int y, int z) {
        if (world instanceof ServerLevel sw) {
            sw.sendParticles(ParticleTypes.FLAME, x + 0.5, y + 0.5, z + 0.5, 1, 0.1, 0.1, 0.1, 0.01);
        }
    }
}
