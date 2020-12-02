package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.commands.WinterCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        player.setPlayerListName(player.getName());
        PlayerMoveListener.LAST_MOVED.remove(player);

        if (WinterCommand.starting) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        if (PlayerMoveListener.SPAWNED.contains(player))
            player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));

        event.setQuitMessage(MinecraftWinter.getInstance().getPrefix() + "§7" + player.getName() + " hat das Spiel §cverlassen§7.");
    }
}
