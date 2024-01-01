package dev.foxikle.seacharm;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import dev.foxikle.seacharm.commands.BoatCommand;
import dev.foxikle.seacharm.listeners.MoveListener;
import dev.foxikle.seacharm.managers.FileManager;
import dev.foxikle.seacharm.objects.Boat;
import dev.foxikle.seacharm.packetListeners.BoatControlListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SeaCharm extends JavaPlugin {

    private FileManager fileManager;

    public static boolean USE_PLACEHOLDER_API = false;

    private ProtocolManager protocolManager;

    public Map<UUID, Boat> boats = new HashMap<>();

    @Override
    public void onEnable() {
        fileManager = new FileManager(this);
        if (fileManager.createFiles()) {

            // setup bstats
            Metrics metrics = new Metrics(this, 20391);


            getServer().getPluginManager().registerEvents(new MoveListener(this), this);


            // setup papi
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                this.getLogger().info("Successfully hooked into PlaceholderAPI.");
                USE_PLACEHOLDER_API = true;
            } else {
                USE_PLACEHOLDER_API = false;
                this.getLogger().info("Could not find PlaceholderAPI! PlaceholderAPI isn't required, but SeaCharm does support it.");
            }
        }
        registerCommands();
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new BoatControlListener(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("boat").setExecutor(new BoatCommand(this));
        getCommand("boat").setTabCompleter(new BoatCommand(this));
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Map<UUID, Boat> getBoats() {
        return boats;
    }
}
