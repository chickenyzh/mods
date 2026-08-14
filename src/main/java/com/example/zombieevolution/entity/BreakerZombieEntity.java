package com.example.zombieevolution.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BreakerZombieEntity extends Zombie {
    public BreakerZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new BreakBlockGoal(this));
    }

    static class BreakBlockGoal extends Goal {
        private final BreakerZombieEntity zombie;
        private int breakTick = 0;

        BreakBlockGoal(BreakerZombieEntity z) {
            this.zombie = z;
        }

        @Override
        public boolean canUse() {
            return this.zombie.getTarget() != null;
        }

        @Override
        public void tick() {
            if (this.zombie.getTarget() == null) return;

            BlockPos pos = this.zombie.blockPosition().relative(this.zombie.getDirection());
            BlockState state = this.zombie.level().getBlockState(pos);

            if (!state.isAir()
                    && state.getDestroySpeed(this.zombie.level(), pos) >= 0
                    && state.getDestroySpeed(this.zombie.level(), pos) < 50) {
                breakTick++;
                if (breakTick >= 20) {
                    this.zombie.level().destroyBlock(pos, true);
                    breakTick = 0;
                }
            } else {
                breakTick = 0;
            }
        }
    }
}
