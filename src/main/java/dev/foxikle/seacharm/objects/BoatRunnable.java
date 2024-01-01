package dev.foxikle.seacharm.objects;

import dev.foxikle.seacharm.SeaCharm;
import dev.foxikle.seacharm.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BoatRunnable extends BukkitRunnable {
    private final SeaCharm plugin;
    private final Boat boat;

    public BoatRunnable(SeaCharm plugin, Boat boat) {
        this.plugin = plugin;
        this.boat = boat;
    }

    @Override
    public void run() {
        Entity parent = Bukkit.getEntity(boat.getArmorstand());
        if(parent != null) {
            double yLvl = parent.getWorld().getSeaLevel()-parent.getHeight(); // desired y level
            double yDelta = yLvl - parent.getLocation().getY();
            parent.setVelocity(Util.yawToVector(boat.getHeading()).multiply(boat.acceleration == 0 ? 0 : boat.acceleration).add(new Vector(0.0, yDelta, 0.0)));
        } else {
            cancel();
        }
    }

    public void start(){
        this.runTaskTimer(plugin, 20, 1);
    }
}
