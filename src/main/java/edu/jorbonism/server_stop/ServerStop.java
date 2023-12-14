package edu.jorbonism.server_stop;

import net.fabricmc.api.ModInitializer;

public class ServerStop implements ModInitializer {

	public static boolean reboot = false;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Server Stop initialized.");
	}
}
