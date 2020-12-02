package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.commands.WinterCommand;
import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerMoveListener implements Listener {

    public static final List<Player> SPAWNED = new ArrayList<>();
    public static final Map<String, ItemStack> ITEMS = new HashMap<>();
    public static final Map<Player, Long> LAST_MOVED = new HashMap<>();

    private static final PlayerAfkPredicate PLAYER_AFK_PREDICATE = new PlayerAfkPredicate();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (PLAYER_AFK_PREDICATE.test(player))
            player.setPlayerListName("§7" + player.getName() + (player.isOp() ? " §7[§cOP§7]" : ""));
        
        LAST_MOVED.put(player, System.currentTimeMillis());

        if (WinterCommand.starting) {
            event.setCancelled(true);
            event.getPlayer().teleport(event.getFrom());
            return;
        }

        if (!MinecraftWinter.started) {
            if (player.getLocation().getY() < MinecraftWinter.getInstance().getVelocityHeight()) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.hidePlayer(player));
                player.setVelocity(new Vector(0, 2, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));
                        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(player));
                    }
                }.runTaskLater(MinecraftWinter.getInstance(), 20);
            }
        } else {
            if (player.getLocation().getY() < MinecraftWinter.getInstance().getTeleportHeight()) {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.hidePlayer(player));
                player.setVelocity(new Vector(0, 2, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));
                        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(player));
                        player.sendTitle("§cFehler", "§cDu kannst nicht dahin!");
                        if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() != Material.ELYTRA)
                            player.getWorld().dropItem(player.getLocation(), player.getInventory().getChestplate());
                        player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                        SPAWNED.add(player);
                    }
                }.runTaskLater(MinecraftWinter.getInstance(), 20);
            } else if (player.getLocation().getY() < MinecraftWinter.getInstance().getVelocityHeight()) {
                if (SPAWNED.contains(player))
                    if (player.getInventory().getChestplate() == null && MinecraftWinter.getInstance().getVelocityHeight() - player.getLocation().getY() < 20)
                        player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                    else if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA && player.isOnGround()) {
                        if (ITEMS.containsKey(player.getUniqueId().toString()))
                            player.getInventory().setChestplate(ITEMS.remove(player.getUniqueId().toString()));
                        else
                            player.getInventory().setChestplate(null);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                SPAWNED.remove(player);
                            }
                        }.runTaskLater(MinecraftWinter.getInstance(), 20);
                    }
            }
        }
    }
}
