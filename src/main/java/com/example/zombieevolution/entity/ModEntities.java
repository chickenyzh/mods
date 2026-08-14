package com.example.zombieevolution.entity;

import com.example.zombieevolution.ZombieEvolution;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Zombie;

public class ModEntities {
    public static final EntityType<PhysicsLimbEntity> PHYSICS_LIMB = register("physics_limb",
            EntityType.Builder.of(PhysicsLimbEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).clientTrackingRange(64).updateInterval(1));
    public static final EntityType<TNTZombieEntity> TNT_ZOMBIE = register("ability_tnt_zombie",
            EntityType.Builder.of(TNTZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
    public static final EntityType<BreakerZombieEntity> BREAKER_ZOMBIE = register("ability_breaker_zombie",
            EntityType.Builder.of(BreakerZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
    public static final EntityType<BuilderZombieEntity> BUILDER_ZOMBIE = register("ability_builder_zombie",
            EntityType.Builder.of(BuilderZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
    public static final EntityType<ClimberZombieEntity> CLIMBER_ZOMBIE = register("ability_climber_zombie",
            EntityType.Builder.of(ClimberZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
    public static final EntityType<SplitterZombieEntity> SPLITTER_ZOMBIE = register("ability_splitter_zombie",
            EntityType.Builder.of(SplitterZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f));

    public static void register() {
        FabricDefaultAttributeRegistry.register(TNT_ZOMBIE, Zombie.createAttributes());
        FabricDefaultAttributeRegistry.register(BREAKER_ZOMBIE, Zombie.createAttributes());
        FabricDefaultAttributeRegistry.register(BUILDER_ZOMBIE, Zombie.createAttributes());
        FabricDefaultAttributeRegistry.register(CLIMBER_ZOMBIE, Zombie.createAttributes());
        FabricDefaultAttributeRegistry.register(SPLITTER_ZOMBIE, Zombie.createAttributes());
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, ZombieEvolution.id(name), builder.build(name));
    }
}
