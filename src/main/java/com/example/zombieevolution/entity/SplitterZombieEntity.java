package com.example.zombieevolution.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class SplitterZombieEntity extends Zombie {
    public SplitterZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide) {
            int count = 2 + this.random.nextInt(2);
            for (int i = 0; i < count; i++) {
                Zombie baby = EntityType.ZOMBIE.create(this.level());
                if (baby != null) {
                    baby.setBaby(true);
                    baby.setPos(
                        this.getX() + this.random.nextDouble() - 0.5,
                        this.getY(),
                        this.getZ() + this.random.nextDouble() - 0.5
                    );
                    this.level().addFreshEntity(baby);
                }
            }
        }
        super.die(source);
    }
}
