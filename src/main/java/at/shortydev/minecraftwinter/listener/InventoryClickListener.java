package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {
    
    public static final Map<Player, Long> LAST_INVENTORY_INTERACTION = new HashMap<>();
    private static final PlayerAfkPredicate PLAYER_AFK_PREDICATE = new PlayerAfkPredicate(); 
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            if (PLAYER_AFK_PREDICATE.test(player))
                player.setPlayerListName("§7" + player.getName() + (player.isOp() ? " §7[§cOP§7]" : ""));
            
            LAST_INVENTORY_INTERACTION.put(player, System.currentTimeMillis());
            
            if (player.isOp())
                return;
            if (PlayerMoveListener.SPAWNED.contains(player))
                event.setCancelled(true);
        }
    }
}
