package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.listener.PlayerMoveListener;
import at.shortydev.minecraftwinter.location.WinterLocation;
import at.shortydev.minecraftwinter.predicates.NumberPredicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class WinterCommand implements CommandExecutor {

    private static final NumberPredicate NUMBER_PREDICATE = new NumberPredicate();

    public static boolean starting = false;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!player.isOp()) {
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§cDu hast keine Berechtigung dazu.");
                return false;
            }

            switch (args.length) {
                case 1:
                    switch (args[0]) {
                        case "setspawn":
                            MinecraftWinter.getInstance().getWinterLocation().saveLocation("spawn", WinterLocation.toWinterLocation(player.getLocation()));
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Spawn gesetzt.");
                            break;
                        case "spawn":
                            player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Zum Spawn teleportiert.");
                            break;
                        case "start":
                            if (!MinecraftWinter.started) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission set essentials.warp true");
                                starting = true;
                                MinecraftWinter.getInstance().getFileConfiguration().set("start", true);
                                try {
                                    MinecraftWinter.getInstance().getFileConfiguration().save(MinecraftWinter.getInstance().getDataFolder() + "/config.yml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                MinecraftWinter.started = true;
                                Location spawn = MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn").add(0, 200, 0);
                                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                                    onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 100, 0));
                                    onlinePlayer.teleport(spawn);
                                    onlinePlayer.setAllowFlight(true);
                                    onlinePlayer.setFlying(true);
                                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 0);
                                    hideEveryone(onlinePlayer);
                                });
                                String[] titles = new String[]{"präsentiert von cxnix und BlutorangeLp", "gesponsort von mc-protection.eu", "programmiert von Shorty#8274", "Los geht's"};
                                final int[] index = {0};
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                                            onlinePlayer.sendTitle("§f§lMinecraft §b§lWINTER", titles[index[0]]);
                                            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 0);
                                            onlinePlayer.setAllowFlight(true);
                                            onlinePlayer.setFlying(true);
                                        });
                                        index[0]++;
                                        if (titles.length == index[0]) {
                                            Location spawn = MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn");
                                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                                                onlinePlayer.getActivePotionEffects().forEach(potionEffect -> onlinePlayer.removePotionEffect(potionEffect.getType()));
                                                onlinePlayer.setFlying(false);
                                                onlinePlayer.setAllowFlight(false);
                                                onlinePlayer.teleport(spawn);
                                                showEveryone(onlinePlayer);
                                                PlayerMoveListener.SPAWNED.add(onlinePlayer);
                                                onlinePlayer.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                                            });
                                            starting = false;
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(MinecraftWinter.getInstance(), 20 * 2, 20 * 4);
                                return false;
                            }
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Winter wurde bereits gestartet.");
                            break;
                        case "reload":
                            MinecraftWinter.getInstance().loadValues();
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Winter wurde neu geladen!");
                            break;
                        default:
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§c/" + s + " <setspawn/tpheight/vheight/spawn/reload> [height]");
                            break;
                    }
                    break;
                case 2:
                    switch (args[0]) {
                        case "tpheight":
                            if (NUMBER_PREDICATE.test(args[1])) {
                                int i = Integer.parseInt(args[1]);
                                MinecraftWinter.getInstance().getFileConfiguration().set("tpheight", i);
                                try {
                                    MinecraftWinter.getInstance().getFileConfiguration().save(MinecraftWinter.getInstance().getDataFolder() + "/config.yml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7TP-Höhe gesetzt.");
                                return false;
                            }
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§cBitte verwende eine Zahl.");
                            break;
                        case "setlocation":
                        case "setloc":
                            MinecraftWinter.getInstance().getWinterLocation().saveLocation(args[1], WinterLocation.toWinterLocation(player.getLocation()));
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Position für §c" + args[1] + " §7gesetzt.");
                            break;
                        case "vheight":
                            if (NUMBER_PREDICATE.test(args[1])) {
                                int i = Integer.parseInt(args[1]);
                                MinecraftWinter.getInstance().getFileConfiguration().set("vheight", i);
                                try {
                                    MinecraftWinter.getInstance().getFileConfiguration().save(MinecraftWinter.getInstance().getDataFolder() + "/config.yml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7V-Höhe gesetzt.");
                                return false;
                            }
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§cBitte verwende eine Zahl.");
                            break;
                        default:
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§c/" + s + " <setspawn/tpheight/vheight/spawn/reload> [height]");
                            break;
                    }
                    break;
                default:
                    player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§c/" + s + " <setspawn/tpheight/vheight/spawn/reload> [height]");
                    break;
            }
        }

        return false;
    }

    public void showEveryone(Player player) {
        Bukkit.getOnlinePlayers().forEach(player::showPlayer);
    }

    public void hideEveryone(Player player) {
        Bukkit.getOnlinePlayers().forEach(player::hidePlayer);
    }
}
