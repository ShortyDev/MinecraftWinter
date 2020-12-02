package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.listener.PlayerMoveListener;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            
            if (player.getWorld().getName().equals("world")) {
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Du kannst nur von der Farmwelt, dem Nether oder dem End aus zurück zum Spawn.");
                return false;
            }
            
            player.teleport(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn"));
            if (player.getInventory().getChestplate() != null) {
                PlayerMoveListener.ITEMS.put(player.getUniqueId().toString(), player.getInventory().getChestplate());
            }
            player.getInventory().setChestplate(null);
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            PlayerMoveListener.SPAWNED.add(player);
        }
        return false;
    }
}
