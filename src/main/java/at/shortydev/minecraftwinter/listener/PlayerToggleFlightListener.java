package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.commands.WinterCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerToggleFlightListener implements Listener {
    
    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        if (WinterCommand.starting)
            event.setCancelled(true);
    }
}
