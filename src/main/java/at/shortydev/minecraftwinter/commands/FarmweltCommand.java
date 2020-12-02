package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmweltCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (MinecraftWinter.started && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Bukkit.dispatchCommand(player, "warp farmwelt");
        }
        
        return false;
    }
}
