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
                String suffix = player.isOp() ? " §7[§cOP§7]" : "";
                switch (args[0].toLowerCase()) {
                    case "rec":
                        player.setPlayerListName("§7[§cREC§7] " + player.getName() + suffix);
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: §cREC");
                        break;
                    case "live":
                        player.setPlayerListName("§7[§5LIVE§7] " + player.getName() + suffix);
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: §5LIVE");
                        break;
                    case "rp":
                        player.setPlayerListName("§7[§eRP§7] " + player.getName() + suffix);
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: §eRP");
                        break;
                    case "off":
                        player.setPlayerListName("§7" + player.getName() + suffix);
                        player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Status zurückgesetzt.");
                        break;
                    default:
                        if (player.isOp()) {
                            String message = "§7[" + ChatColor.translateAlternateColorCodes('&', args[0]).toUpperCase() + "§7] ";
                            player.setPlayerListName(message + "§7" + player.getName() + suffix);
                            player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Neuer Status: " + message);
                        } else {
                            player.setPlayerListName("§7" + player.getName() + suffix);
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
