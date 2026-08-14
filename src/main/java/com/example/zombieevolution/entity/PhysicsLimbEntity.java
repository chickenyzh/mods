package com.example.zombieevolution.entity;

import com.example.zombieevolution.LimbPhysicsManager;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class PhysicsLimbEntity extends Entity {

    private static final EntityDataAccessor<Integer> LIMB_TYPE =
            SynchedEntityData.defineId(PhysicsLimbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SKIN_INDEX =
            SynchedEntityData.defineId(PhysicsLimbEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RANDOM_YAW =
            SynchedEntityData.defineId(PhysicsLimbEntity.class, EntityDataSerializers.FLOAT);

    private int age;
    private boolean hasTouchedGround;

    public PhysicsLimbEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIMB_TYPE, 2);
        this.entityData.define(SKIN_INDEX, 0);
        this.entityData.define(RANDOM_YAW, 0.0f);
    }

    public void setLimbType(LimbType type) {
        this.entityData.set(LIMB_TYPE, type.ordinal());
    }

    public LimbType getLimbType() {
        return LimbType.values()[this.entityData.get(LIMB_TYPE)];
    }

    public void setSkinIndex(int index) {
        this.entityData.set(SKIN_INDEX, index);
    }

    public int getSkinIndex() {
        return this.entityData.get(SKIN_INDEX);
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return 400;
    }

    public void setRandomYaw(float yaw) {
        this.entityData.set(RANDOM_YAW, yaw);
    }

    public float getRandomYaw() {
        return this.entityData.get(RANDOM_YAW);
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof PhysicsLimbEntity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return !this.isSpectator();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && source.getEntity() != null) {
            this.knockback(source.getEntity());
        }
        return false;
    }

    private void knockback(Entity attacker) {
        if (attacker == null || this.level().isClientSide) return;
        Vec3 vel = this.getDeltaMovement();
        double dx = this.getX() - attacker.getX();
        double dz = this.getZ() - attacker.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        float w = LimbPhysicsManager.getLimbWeight();

        if (len > 0.01) {
            double push = 0.5 / len / w;
            this.setDeltaMovement(vel.x + dx * push, 0.3 / w, vel.z + dz * push);
        } else {
            this.setDeltaMovement(vel.x, 0.3 / w, vel.z);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide && this.age > 5) {
            Vec3 vel = this.getDeltaMovement();
            double dx = this.getX() - player.getX();
            double dz = this.getZ() - player.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            float w = LimbPhysicsManager.getLimbWeight();

            if (len > 0.01) {
                this.setDeltaMovement(
                        vel.x + dx * 0.15 / len / w,
                        vel.y + 0.08 / w,
                        vel.z + dz * 0.15 / len / w
                );
            }
        }
    }

    @Override
    public void push(double dx, double dy, double dz) {
        Vec3 vel = this.getDeltaMovement();
        this.setDeltaMovement(vel.x + dx, vel.y + dy, vel.z + dz);
    }

    @Override
    public void tick() {
        double prevX = this.getX();
        double prevY = this.getY();
        double prevZ = this.getZ();
        Level w = this.level();

        this.age++;
        if (this.age >= this.getMaxAge()) {
            if (!w.isClientSide) {
                for (int i = 0; i < 5; i++) {
                    w.addParticle(
                            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHERRACK.defaultBlockState()),
                            this.getX() + (this.random.nextDouble() - 0.5) * 0.4,
                            this.getY() + this.random.nextDouble() * 0.4,
                            this.getZ() + (this.random.nextDouble() - 0.5) * 0.4,
                            0.0, 0.0, 0.0
                    );
                }
            }
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        Vec3 vel = this.getDeltaMovement();

        if (!this.isNoGravity()) {
            vel = vel.add(0, -0.04, 0);
        }

        if (this.isInWater()) {
            vel = new Vec3(vel.x * 0.85, Math.min(vel.y + 0.03, 0.1), vel.z * 0.85);
        } else if (this.isInLava()) {
            this.setRemainingFireTicks(0);
            this.clearFire();
            vel = new Vec3(vel.x * 0.8, vel.y, vel.z * 0.8);
        } else {
            vel = new Vec3(vel.x * 0.99, vel.y, vel.z * 0.99);
        }

        // Cap speed
        double maxSpeed = 2.5;
        double speedSq = vel.x * vel.x + vel.y * vel.y + vel.z * vel.z;
        if (speedSq > maxSpeed * maxSpeed) {
            vel = vel.scale(maxSpeed / Math.sqrt(speedSq));
        }

        this.setDeltaMovement(vel);

        double intendedX = vel.x;
        double intendedY = vel.y;
        double intendedZ = vel.z;

        this.move(MoverType.SELF, vel);

        double actualX = this.getX() - prevX;
        double actualY = this.getY() - prevY;
        double actualZ = this.getZ() - prevZ;

        Vec3 newVel = this.getDeltaMovement();

        if (Math.abs(intendedX - actualX) > 0.001) {
            newVel = new Vec3(-intendedX * 0.35, newVel.y, newVel.z);
        }

        if (Math.abs(intendedZ - actualZ) > 0.001) {
            newVel = new Vec3(newVel.x, newVel.y, -intendedZ * 0.35);
        }

        if (Math.abs(intendedY - actualY) > 0.001 && intendedY < 0) {
            newVel = new Vec3(newVel.x, -intendedY * 0.2, newVel.z);
        }

        this.setDeltaMovement(newVel);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.entityData.set(LIMB_TYPE, nbt.getInt("LimbType"));
        this.entityData.set(SKIN_INDEX, nbt.getInt("SkinIndex"));
        this.age = nbt.getInt("Age");
        this.hasTouchedGround = nbt.getBoolean("TouchedGround");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("LimbType", this.entityData.get(LIMB_TYPE));
        nbt.putInt("SkinIndex", this.entityData.get(SKIN_INDEX));
        nbt.putInt("Age", this.age);
        nbt.putBoolean("TouchedGround", this.hasTouchedGround);
    }

    public enum LimbType {
        HEAD(1.0f),
        TORSO(1.0f),
        ARM_LEFT(1.0f),
        ARM_RIGHT(1.0f),
        LEG_LEFT(1.0f),
        LEG_RIGHT(1.0f);

        private final float scale;

        LimbType(float scale) {
            this.scale = scale;
        }

        public float getScale() {
            return scale;
        }
    }
}
