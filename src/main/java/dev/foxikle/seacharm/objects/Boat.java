package dev.foxikle.seacharm.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.joml.Matrix4f;

import java.util.*;
import java.util.regex.Pattern;

public class Boat {

   private final List<String> data;
   private final int price;
   private final String name;
   private final double waterline;
   private UUID parent;
   private UUID armorstand;
   private boolean spawned;
   private double heading = 0.0;


    List<UUID> passengers = new ArrayList<>();
    double acceleration = 0.0;
    private BoatRunnable runnable;

    private static final Pattern MATERIAL_PATTERN = Pattern.compile("Name:\"minecraft:([^\"]+)\"");
    private static final Pattern MATRIX_PATTERN = Pattern.compile("transformation:\\[(.+)\\]");

    public Boat(List<String> data, int price, String name, double waterline) {
        this.data = data;
        this.price = price;
        this.name = name;
        this.waterline = waterline;
    }

    public UUID spawn(Location location) {
        if(spawned) throw new IllegalStateException("This boat has already been spawned! You must remove it to spawn it again.");
        location.setPitch(0);
        location.setYaw(90);
        var armorstand = location.getWorld().spawn(location, ArmorStand.class);
        armorstand.setVisible(false);
        armorstand.setSmall(true);

        var parent = location.getWorld().spawn(location, BlockDisplay.class);
        parent.addScoreboardTag("PARENT");
        this.armorstand = armorstand.getUniqueId();
        parent.setInterpolationDelay(-1);
        parent.setInterpolationDuration(1);
        parent.getTransformation().getLeftRotation().rotateAxis((float) Math.cos(Math.PI/4),  (float) Math.sin(Math.PI/4), 0, 0);

        parseData().forEach(boatData -> {
            BlockDisplay bd = location.getWorld().spawn(location, BlockDisplay.class);
            bd.setTransformationMatrix(boatData.transformations());
            bd.setBlock(boatData.blockData());
            parent.addPassenger(bd);
            passengers.add(bd.getUniqueId());
        });
        this.parent = parent.getUniqueId();
        spawned = true;
        runnable.start();
        armorstand.addPassenger(parent);
        return this.parent;
    }

    private Matrix4f parseTransformations(List<String> data) {
        Matrix4f matrix = new Matrix4f();
        for(int i = 0; i < data.size(); ++i) {
            matrix.setRowColumn(i >> 2, i & 3, Float.parseFloat(data.get(i)));
        }
        return matrix.determineProperties();
        //return new Matrix4f(Float.parseFloat(data.get(0)), Float.parseFloat(data.get(1)), Float.parseFloat(data.get(2)), Float.parseFloat(data.get(3)), Float.parseFloat(data.get(4)), Float.parseFloat(data.get(5)), Float.parseFloat(data.get(6)), Float.parseFloat(data.get(7)), Float.parseFloat(data.get(8)), Float.parseFloat(data.get(9)), Float.parseFloat(data.get(10)), Float.parseFloat(data.get(11)), Float.parseFloat(data.get(12)), Float.parseFloat(data.get(13)), Float.parseFloat(data.get(14)), Float.parseFloat(data.get(15)));
    }

    public List<BoatData> parseData() {
        List<BoatData> boatData = new ArrayList<>();
        data.forEach(s -> {

            String materialString = "";
            if(MATERIAL_PATTERN.matcher(s).find()) {
                if(MATERIAL_PATTERN.matcher(s).results().findFirst().isPresent())
                    materialString = MATERIAL_PATTERN.matcher(s).results().findFirst().get().group();
            } else {
                materialString = "MAGENTA_GLAZED_TERRACOTTA";
            }
            materialString = materialString.replace("Name:\"minecraft:", "").replace("\"", "").toUpperCase(Locale.ROOT);

            String matrixStr = "";
            if(MATRIX_PATTERN.matcher(s).find()) {
                if(MATRIX_PATTERN.matcher(s).results().findFirst().isPresent())
                    matrixStr = MATRIX_PATTERN.matcher(s).results().findFirst().get().group();
            } else {
                matrixStr = new Matrix4f().toString();
            }

            matrixStr = matrixStr.replace("transformation:[", "").replace("]", "").toUpperCase(Locale.ROOT);
            boatData.add(new BoatData(parseTransformations(Arrays.stream(matrixStr.split(",")).toList()), Material.valueOf(materialString).createBlockData()));
        });
        return boatData;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getData() {
        return data;
    }

    public BoatRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(BoatRunnable runnable) {
        this.runnable = runnable;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public List<UUID> getPassengers() {
        return passengers;
    }

    public UUID getParent() {
        return parent;
    }

    public UUID getArmorstand() {
        return armorstand;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }
}
