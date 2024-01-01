package dev.foxikle.seacharm.listeners;

import dev.foxikle.seacharm.SeaCharm;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final SeaCharm plugin;

    public MoveListener(SeaCharm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        event.getTo().getNearbyLivingEntities(30).forEach(entity -> {
            if(entity instanceof BlockDisplay display){
                if(display.getBoundingBox().overlaps(event.getPlayer().getBoundingBox())) {
                    event.getPlayer().sendMessage("hewo!");
                }
            }
        });
    }
}
