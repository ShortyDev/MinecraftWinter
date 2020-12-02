package at.shortydev.minecraftwinter.predicates;

import at.shortydev.minecraftwinter.listener.AsyncPlayerChatListener;
import at.shortydev.minecraftwinter.listener.InventoryClickListener;
import at.shortydev.minecraftwinter.listener.PlayerMoveListener;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class PlayerAfkPredicate implements Predicate<Player> {
    
    private static final long AFK_TIME = 60000L;

    @Override
    public boolean test(Player player) {
        long currentTime = System.currentTimeMillis();
        return currentTime - PlayerMoveListener.LAST_MOVED.getOrDefault(player, 0L) > AFK_TIME
                && currentTime - InventoryClickListener.LAST_INVENTORY_INTERACTION.getOrDefault(player, 0L) > AFK_TIME
                && currentTime - AsyncPlayerChatListener.LAST_CHATTED.getOrDefault(player, 0L) > AFK_TIME;
    }
}
