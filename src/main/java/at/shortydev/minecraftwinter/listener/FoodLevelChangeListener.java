package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!MinecraftWinter.started)
            event.setCancelled(true);
    }
}
