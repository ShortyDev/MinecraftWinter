package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {
    
    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn").getWorld().getName()))
            event.setCancelled(true);
    }
}
