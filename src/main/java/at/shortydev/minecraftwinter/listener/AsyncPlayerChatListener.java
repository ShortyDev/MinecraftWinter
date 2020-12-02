package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AsyncPlayerChatListener implements Listener {

    private final int localRadius = 30;
    public static final Map<Player, Long> LAST_CHATTED = new HashMap<>();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (new PlayerAfkPredicate().test(player))
            player.setPlayerListName("§7" + player.getName() + (player.isOp() ? " §7[§cOP§7]" : ""));
        
        LAST_CHATTED.put(player, System.currentTimeMillis());

        if (event.getMessage().startsWith("@") && event.getMessage().length() > 1) {
            final String[] message = {event.getMessage()};
            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> message[0].contains(onlinePlayer.getName()))
                    .forEach(onlinePlayer -> {
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                        message[0] = message[0].replace(onlinePlayer.getName(), "§c@" + onlinePlayer.getName() + "§r");
                    });
            event.setMessage(message[0]);
            event.setFormat("§7[§eGlobal§7] " + (player.isOp() ? "§7[§cOPERATOR§7] " : "") + "§7" + player.getName() + "§f: " + event.getMessage().substring(1));
        } else if (event.getMessage().startsWith("*") && event.getMessage().length() > 1) {
            event.getRecipients().clear();
            List<Player> playerStream = player.getWorld().getPlayers().stream()
                    .filter(onlinePlayer -> onlinePlayer.getLocation().distance(player.getLocation()) <= localRadius)
                    .collect(Collectors.toList());
            playerStream.forEach(event.getRecipients()::add);
            if (playerStream.size() <= 1)
                player.sendMessage("§7§oNiemand kann sehen was du machst...");
            event.setFormat("*§o" + player.getName() + " " + event.getMessage().substring(1) + "*");
        } else {
            event.getRecipients().clear();
            List<Player> playerStream = player.getWorld().getPlayers().stream()
                    .filter(onlinePlayer -> onlinePlayer.getLocation().distance(player.getLocation()) <= localRadius)
                    .collect(Collectors.toList());
            playerStream.forEach(event.getRecipients()::add);
            if (playerStream.size() <= 1)
                player.sendMessage("§7§oNiemand kann dich hören... mit @ vor deiner Nachricht schreibst du in den Globalchat.");
            event.setFormat((player.isOp() ? "§7[§cOPERATOR§7] " : "") + "§7" + player.getName() + "§f: " + event.getMessage());
        }

    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/minecraft:") && !event.getPlayer().isOp()) {
            event.getPlayer().chat("@7" + event.getMessage().substring(1));
            event.setCancelled(true);
        }
    }
}
