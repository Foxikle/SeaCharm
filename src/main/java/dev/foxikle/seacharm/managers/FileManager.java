package dev.foxikle.seacharm.managers;

import dev.foxikle.seacharm.SeaCharm;
import dev.foxikle.seacharm.objects.Boat;
import dev.foxikle.seacharm.objects.BoatRunnable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private final SeaCharm plugin;

    /**
     * <p> Gets the file manager object.
     * </p>
     * @param plugin The instance of the Main class
     */
    public FileManager (SeaCharm plugin){
        this.plugin = plugin;
    }

    /**
     * <p> Creates the files the plugin needs to run
     * </p>
     */
    public boolean createFiles(){
        if (!new File("plugins/SeaCharm/boats/").exists()) {
            new File("plugins/SeaCharm/boats/").mkdirs();
        } else if (!new File("plugins/SeaCharm/config.yml").exists()) {
            plugin.saveResource("config.yml", false);
        }
        File file = new File("plugins/SeaCharm/config.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        return true;
    }

    public List<String> getFileNames() {
        List<String> returnme = new ArrayList<>();
        File dir = new File("plugins/SeaCharm/boats/");
        Arrays.stream(dir.listFiles()).forEach(file -> {
            returnme.add(file.getName());
        });
        return returnme;
    }

    public static File getBoatFromFile(String name) {
        return new File("plugins/SeaCharm/boats/" + name);
    }

    public static Boat parseBoat(File file){
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        List<String> data = yaml.getStringList("displays");
        int price = yaml.getInt("price");
        double waterline = yaml.getDouble("waterline");
        String name = yaml.getString("name");
        List<String> collisions = yaml.getStringList("collisions");
        Boat b = new Boat(data, price, name, waterline);
        b.setRunnable(new BoatRunnable(JavaPlugin.getPlugin(SeaCharm.class), b));
        return b;
    }
}
