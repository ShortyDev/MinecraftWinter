package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "rec":
                        MinecraftWinter.getInstance().setStatus(player, "§cREC");
                        MinecraftWinter.getInstance().statusCache.put(player.getUniqueId().toString(), "§cREC");
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: §cREC");
                        break;
                    case "live":
                        MinecraftWinter.getInstance().setStatus(player, "§5LIVE");
                        MinecraftWinter.getInstance().statusCache.put(player.getUniqueId().toString(), "§5LIVE");
                        break;
                    case "rp":
                        MinecraftWinter.getInstance().setStatus(player, "§eRP");
                        MinecraftWinter.getInstance().statusCache.put(player.getUniqueId().toString(), "§RP");
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: §eRP");
                        break;
                    case "off":
                        MinecraftWinter.getInstance().statusCache.remove(player.getUniqueId().toString());
                        MinecraftWinter.getInstance().setStatus(player, null);
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Status zurückgesetzt.");
                        break;
                    default:
                        if (player.isOp()) {
                            String message = ChatColor.translateAlternateColorCodes('&', args[0]);
                            MinecraftWinter.getInstance().setStatus(player, message);
                            MinecraftWinter.getInstance().statusCache.put(player.getUniqueId().toString(), message);
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: " + message);
                        } else {
                            MinecraftWinter.getInstance().statusCache.remove(player.getUniqueId().toString());
                            MinecraftWinter.getInstance().setStatus(player, null);
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Status zurückgesetzt.");
                        }
                        break;
                }
            } else {
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§c/" + s + " <rec/live/rp/off>");
            }
        }
        return false;
    }
}
