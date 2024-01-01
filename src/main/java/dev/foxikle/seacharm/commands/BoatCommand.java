package dev.foxikle.seacharm.commands;

import dev.foxikle.seacharm.SeaCharm;
import dev.foxikle.seacharm.managers.FileManager;
import dev.foxikle.seacharm.objects.Boat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BoatCommand implements TabExecutor {
    private final SeaCharm plugin;

    public BoatCommand(SeaCharm plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            //todo: permcheck
            if(args.length >= 1){
                if(plugin.getFileManager().getFileNames().contains(args[0])){
                    player.sendMessage(ChatColor.GREEN + "Successfully spawned boat '" + args[0].replace(".yml", "") + "'");
                    Boat b = FileManager.parseBoat(FileManager.getBoatFromFile(args[0]));
                    UUID uuid = b.spawn(player.getLocation());
                    Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getEntity(uuid).addPassenger(player), 1);
                    plugin.boats.put(uuid, b);
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return plugin.getFileManager().getFileNames();
    }
}
