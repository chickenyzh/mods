package com.example.zombieevolution.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ClimberZombieEntity extends Zombie {
    public ClimberZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }
}
