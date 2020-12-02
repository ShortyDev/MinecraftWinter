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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewChatRadiusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            final Location center = player.getLocation().clone();

            int[] index = {-15, -1};

            new BukkitRunnable() {
                @Override
                public void run() {
                    index[0]++;

                    if (index[0] < 15) {
                        Set<Location> blocks = circle(center, 15 + index[0], true);

                        Map<Location, BlockData> oldData = new HashMap<>();

                        BlockData blockData = Bukkit.getServer().createBlockData(Material.RED_STAINED_GLASS);

                        blocks.forEach(location -> {
                            oldData.put(location, location.getBlock().getBlockData().clone());
                            player.sendBlockChange(location, blockData);
                        });

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                blocks.forEach(location -> player.sendBlockChange(location, oldData.get(location)));
                            }
                        }.runTaskLater(MinecraftWinter.getInstance(), index[0] == 14 ? 100L : 1L);
                    } else if (index[0] == 15) {
                        index[1] = 15;
                        cancel();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                index[1]--;
                                if (index[1] < 0) {
                                    cancel();
                                    return;
                                }
                                Set<Location> blocks = circle(center, 15 + index[1], true);

                                Map<Location, BlockData> oldData = new HashMap<>();

                                BlockData blockData = Bukkit.getServer().createBlockData(Material.RED_STAINED_GLASS);

                                blocks.forEach(location -> {
                                    oldData.put(location, location.getBlock().getBlockData().clone());
                                    player.sendBlockChange(location, blockData);
                                });

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        blocks.forEach(location -> player.sendBlockChange(location, oldData.get(location)));
                                    }
                                }.runTaskLater(MinecraftWinter.getInstance(), 1L);
                            }
                        }.runTaskTimer(MinecraftWinter.getInstance(), 100L, 1L);
                    }
                }
            }.runTaskTimer(MinecraftWinter.getInstance(), 0L, 1L);
        }
        return false;
    }

    /*
    https://bukkit.org/threads/spheres-hollow-sphers-circles-hollow-circles-tutorial.306490/
     */
    private Set<Location> makeHollow(Set<Location> blocks, boolean sphere) {
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

    public Set<Location> circle(Location location, int radius, boolean hollow) {
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
