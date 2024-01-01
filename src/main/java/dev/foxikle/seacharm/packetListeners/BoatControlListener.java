package dev.foxikle.seacharm.packetListeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import dev.foxikle.seacharm.SeaCharm;
import dev.foxikle.seacharm.objects.Boat;
import dev.foxikle.seacharm.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BoatControlListener extends PacketAdapter {

    private final SeaCharm plugin;

    public BoatControlListener(Plugin plugin, PacketType... types) {
        super(plugin, types);
        this.plugin = (SeaCharm) plugin;
    }

    public BoatControlListener(Plugin plugin, Iterable<? extends PacketType> types) {
        super(plugin, types);
        this.plugin = (SeaCharm) plugin;
    }

    public BoatControlListener(Plugin plugin, ListenerPriority listenerPriority, Iterable<? extends PacketType> types) {
        super(plugin, listenerPriority, types);
        this.plugin = (SeaCharm) plugin;
    }

    public BoatControlListener(Plugin plugin, ListenerPriority listenerPriority, Iterable<? extends PacketType> types, ListenerOptions... options) {
        super(plugin, listenerPriority, types, options);
        this.plugin = (SeaCharm) plugin;
    }

    public BoatControlListener(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
        this.plugin = (SeaCharm) plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        float steeringValue = packet.getFloat().read(0);
        float accelerationValue = packet.getFloat().read(1);
        if (event.getPlayer().getVehicle() instanceof BlockDisplay bd) {
            Boat b = plugin.boats.get(bd.getUniqueId());
            if(b == null ) {
                event.getPlayer().sendMessage("Hey! Your boat is null?");
                return;
            }
            if (steeringValue != 0) {
                b.setHeading(b.getHeading() + steeringValue);
                Bukkit.getScheduler().runTask(plugin, () -> plugin.getBoats().get(bd.getUniqueId()).getPassengers().forEach(uuid -> {
                    if (Bukkit.getEntity(uuid) instanceof BlockDisplay display) {

                        Transformation t = display.getTransformation();

                        Vector3f translation = new Vector3f(t.getTranslation());


                        float rotation = Math.toRadians(steeringValue);

                        // Figure out how to rotate the translation
                        Matrix4f rotationMatrix = new Matrix4f().rotateY(rotation);
                        Vector3f rotatedPoint = new Vector3f(translation);
                        rotationMatrix.transformPosition(rotatedPoint);

                        Matrix4f finalTransformation = Util.composeMatrixFromTransformation(t)
                                .rotateAroundLocal(t.getRightRotation().rotateY(rotation), (float) (display.getLocation().x() + translation.x), (float) (display.getLocation().y() + translation.y), (float) (display.getLocation().z() + translation.z))
                                .setTranslation(rotatedPoint.x, rotatedPoint.y, rotatedPoint.z);
                        display.setTransformationMatrix(finalTransformation);
                    }
                }));
            }
            if(accelerationValue != 0) {
                b.setAcceleration(b.getAcceleration() + accelerationValue/10);
            }
        }
    }
}
