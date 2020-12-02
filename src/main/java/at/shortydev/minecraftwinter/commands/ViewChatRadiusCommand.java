package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ViewChatRadiusCommand implements CommandExecutor {
    
    public static List<String> showChatRadius = new ArrayList<>();
    public static Map<String, Map<Location, BlockData>> oldDataMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (showChatRadius.contains(player.getUniqueId().toString())) {
                showChatRadius.remove(player.getUniqueId().toString());
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Chat-Radius wird nun nicht mehr angezeigt.");
                
                oldDataMap.remove(player.getUniqueId().toString()).forEach(player::sendBlockChange);
            } else {
                showChatRadius.add(player.getUniqueId().toString());
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Chat-Radius wird nun angezeigt.");
                Set<Location> blocks = circle(player.getLocation(), 30, true);

                Map<Location, BlockData> oldData = new HashMap<>();

                BlockData blockData = Bukkit.getServer().createBlockData(Material.RED_STAINED_GLASS);

                blocks.forEach(location -> {
                    oldData.put(location, location.getBlock().getBlockData().clone());
                    player.sendBlockChange(location, blockData);
                });
                
                oldDataMap.put(player.getUniqueId().toString(), oldData);
            }

        }
        return false;
    }

    /*
    https://bukkit.org/threads/spheres-hollow-sphers-circles-hollow-circles-tutorial.306490/
     */
    public static Set<Location> makeHollow(Set<Location> blocks, boolean sphere) {
        Set<Location> edge = new HashSet<>();
        if (!sphere) {
            for (Location l : blocks) {
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))) {
                    edge.add(l);
                }
            }
        } else {
            for (Location l : blocks) {
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                Location top = new Location(w, X, Y + 1, Z);
                Location bottom = new Location(w, X, Y - 1, Z);
                if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))) {
                    edge.add(l);
                }
            }
        }
        return edge;
    }

    public static Set<Location> circle(Location location, int radius, boolean hollow) {
        Set<Location> blocks = new HashSet<>();
        World world = location.getWorld();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;

        if (hollow) {
            for (int x = X - radius; x <= X + radius; x++) {
                for (int z = Z - radius; z <= Z + radius; z++) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                        Location block = new Location(world, x, Y, z);
                        blocks.add(block);
                    }
                }
            }
            return makeHollow(blocks, false);
        } else {
            for (int x = X - radius; x <= X + radius; x++) {
                for (int z = Z - radius; z <= Z + radius; z++) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                        Location block = new Location(world, x, Y, z);
                        blocks.add(block);
                    }
                }
            }
            return blocks;
        }
    }
}
