package com.example.zombieevolution.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BuilderZombieEntity extends Zombie {
    public BuilderZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PlaceBlockGoal(this));
    }

    static class PlaceBlockGoal extends Goal {
        private final BuilderZombieEntity zombie;
        private int placeTick = 0;

        PlaceBlockGoal(BuilderZombieEntity z) {
            this.zombie = z;
        }

        @Override
        public boolean canUse() {
            return this.zombie.getTarget() != null;
        }

        @Override
        public void tick() {
            if (this.zombie.getTarget() == null) return;

            placeTick++;
            if (placeTick < 40) return;
            placeTick = 0;

            Direction direction = this.zombie.getDirection();
            BlockPos pos = this.zombie.blockPosition().relative(direction);
            BlockState state = this.zombie.level().getBlockState(pos);

            if (state.isAir() || state.canBeReplaced()) {
                this.zombie.level().setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            }
        }
    }
}
