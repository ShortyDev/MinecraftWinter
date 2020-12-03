package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.predicates.PlayerDeathPredicate;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MinecraftWinter.getInstance().setStatus(player, null);

        event.setJoinMessage(MinecraftWinter.getInstance().getPrefix() + "§7" + player.getName() + " hat das Spiel §abetreten§7.");
        player.sendMessage("§cSponsored by https://mc-protection.eu (https://discord.gg/DQrJQWQZBd)");
        if (!MinecraftWinter.started) {
            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Willkommen bei Minecraft-Winter, das Projekt startet bald.");
            player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));
        } else {
            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Willkommen zurück bei Minecraft-Winter.");
            if (new PlayerDeathPredicate().test(player))
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
            else
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1, 1);
            if (player.getLocation().getY() > MinecraftWinter.getInstance().getVelocityHeight() && !new PlayerDeathPredicate().test(player)) {
                player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                PlayerMoveListener.SPAWNED.add(player);
            }
        }

        if (player.isOp())
            player.sendMessage("§9§lAktueller Status: " + (MinecraftWinter.started ? "§aGestartet" : "§cNoch nicht gestartet"));
    }
}
