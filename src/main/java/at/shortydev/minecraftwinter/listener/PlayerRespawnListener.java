package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerRespawnListener implements Listener {
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location location = MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn");
        event.setRespawnLocation(location);
        PlayerMoveListener.SPAWNED.add(player);
        player.playSound(location, Sound.BLOCK_BEACON_AMBIENT,  1, 1);
        player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
    }
}
