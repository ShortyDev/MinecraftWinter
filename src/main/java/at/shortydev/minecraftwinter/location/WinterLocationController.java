package at.shortydev.minecraftwinter.location;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Location;

import java.io.*;

public class WinterLocationController {
    
    public void saveLocation(String name, WinterLocation location) {
        File file = new File(name);
        if (file.delete())
            System.out.println("Location file for " + name + " deleted.");
        try {
            if (file.createNewFile()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(MinecraftWinter.GSON.toJson(location));
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Location getLocation(String name) {
        File file = new File(name);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            return MinecraftWinter.GSON.fromJson(bufferedReader, WinterLocation.class).getLegacyLocation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
