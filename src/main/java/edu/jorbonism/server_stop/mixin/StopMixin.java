package edu.jorbonism.server_stop.mixin;

import java.io.IOException;
import java.net.Proxy;
import java.time.LocalTime;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.DataFixer;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.ApiServices;
import net.minecraft.util.SystemDetails;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.level.storage.LevelStorage.Session;

@Mixin(MinecraftDedicatedServer.class)
public class StopMixin extends MinecraftServer {
	public StopMixin(Thread serverThread, Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader,
			Proxy proxy, DataFixer dataFixer, ApiServices apiServices,
			WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
		super(serverThread, session, dataPackManager, saveLoader, proxy, dataFixer, apiServices,
				worldGenerationProgressListenerFactory);
	}


	@Inject(at = @At("RETURN"), method = "tickWorlds")
	private void stop(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		LocalTime time = LocalTime.now();
		if (time.getHour() == 6 && time.getMinute() == 0) {
			this.stop(false);
		}
		else if (time.getSecond() == 0) throw new CrashException(CrashReport.create(new Throwable(), "Test crash"));
	}

	

	@Shadow public boolean shouldBroadcastConsoleToOps() { return false; }
	@Shadow public boolean areCommandBlocksEnabled() { return false; }
	@Shadow public int getFunctionPermissionLevel() { return 0; }
	@Shadow public int getOpPermissionLevel() { return 0; }
	@Shadow public int getRateLimit() { return 0; }
	@Shadow public boolean isDedicated() { return false; }
	@Shadow public boolean isHost(GameProfile profile) { return false; }
	@Shadow public boolean isRemote() { return false; }
	@Shadow public boolean isUsingNativeTransport() { return false; }
	@Shadow protected boolean setupServer() throws IOException { return false; }
	@Shadow public boolean shouldBroadcastRconToOps() { return false; }
	@Shadow public SystemDetails addExtraSystemDetails(SystemDetails details) { return null; }
}
