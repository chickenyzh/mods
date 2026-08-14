package com.example.zombieevolution;

import com.example.zombieevolution.block.ModBlocks;
import com.example.zombieevolution.entity.ModEntities;
import com.example.zombieevolution.handler.SpawnShelterHandler;
import com.example.zombieevolution.item.ModItems;
import com.example.zombieevolution.mixin.SpawnGroupAccessor;
import com.example.zombieevolution.network.ModPackets;
import com.example.zombieevolution.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZombieEvolution implements ModInitializer {
	public static final String MOD_ID = "zombie_evolution";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Zombie Evolution...");

		ModItems.register();
		ModBlocks.register();
		ModEntities.register();
		ModPackets.registerServer();
		ModWorldGen.register();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SpawnShelterHandler.tryPlaceShelter(server);

			MobCategory monster = MobCategory.MONSTER;
			((SpawnGroupAccessor) (Object) monster).setCapacity(400);
			LOGGER.info("Zombie apocalypse mode: {} capacity set to 400", monster.getName());
		});
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
