package edu.jorbonism.server_stop.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.logging.LogUtils;

import edu.jorbonism.server_stop.ServerStop;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import net.minecraft.util.thread.ReentrantThreadExecutor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements CommandOutput, AutoCloseable {

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "setCrashReport"), require = 1)
    public void runServerMod(CallbackInfo ci) {
        ServerStop.reboot = true;
    }

    @Inject(method = "runServer", at = @At("RETURN"))
    public void runServer(CallbackInfo ci) {
        if (!ServerStop.reboot) return;
        ServerStop.reboot = false;
        System.out.println("server rebooting");
        this.runServer();
    }
    
    @Shadow public void runServer() {}
    @Shadow private static final Logger LOGGER = LogUtils.getLogger();
    @Shadow public void sendMessage(Text var1) {}
    @Shadow public boolean shouldReceiveFeedback() { return false; }
    @Shadow public boolean shouldTrackOutput() { return false; }
    @Shadow public boolean shouldBroadcastConsoleToOps() { return false; }
    @Shadow protected ServerTask createTask(Runnable var1) { return null; }
    @Shadow protected boolean canExecute(ServerTask var1) { return false; }
    @Shadow protected Thread getThread() { return null; }

}
