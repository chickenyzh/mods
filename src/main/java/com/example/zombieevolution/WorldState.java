package com.example.zombieevolution;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WorldState extends SavedData {
    private boolean shelterPlaced;

    public WorldState() {
        this.shelterPlaced = false;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("ShelterPlaced", this.shelterPlaced);
        return nbt;
    }

    public static WorldState fromNbt(CompoundTag nbt) {
        WorldState state = new WorldState();
        state.shelterPlaced = nbt.getBoolean("ShelterPlaced");
        return state;
    }

    public boolean isShelterPlaced() {
        return shelterPlaced;
    }

    public void setShelterPlaced(boolean v) {
        this.shelterPlaced = v;
        setDirty();
    }

    public static WorldState get(MinecraftServer server) {
        if (server == null) return null;
        ServerLevel overworld = server.overworld();
        if (overworld == null) return null;
        DimensionDataStorage mgr = overworld.getDataStorage();
        return mgr.computeIfAbsent(WorldState::fromNbt, WorldState::new, "zombie_evolution");
    }
}
