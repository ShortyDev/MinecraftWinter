package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;

import java.util.HashMap;

public class BedListener implements Listener {

    private int players = 0;
    private boolean skipAllowed = false;
    private long skipTime = 0L;
    private final HashMap<Integer, Integer> neededForSkip = new HashMap<>();

    public BedListener() {
        neededForSkip.put(1, 1);
        neededForSkip.put(2, 2);
        neededForSkip.put(3, 2);
        neededForSkip.put(4, 2);
        neededForSkip.put(5, 3);
        neededForSkip.put(6, 4);
        neededForSkip.put(7, 4);
        neededForSkip.put(8, 4);
        neededForSkip.put(9, 4);
        neededForSkip.put(10, 4);
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        event.setCancelled(!skipAllowed && event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP));
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK)
            return;
        if (!event.getBed().getLocation().getWorld().getName().equalsIgnoreCase(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn").getWorld().getName()))
            return;
        players++;
        int needed = calculateNeeded();
        if (needed == players) {
            handle(event.getPlayer().getWorld());
            Bukkit.broadcastMessage(MinecraftWinter.getInstance().getPrefix() + String.format("§e%s §7schläft zZz... die Nacht wird übersprungen...", event.getPlayer().getName()));
            Bukkit.broadcastMessage(MinecraftWinter.getInstance().getPrefix() + "§7Guten Morgen ...");
        } else {
            int required = needed - players;
            Bukkit.broadcastMessage(MinecraftWinter.getInstance().getPrefix() + String.format("§e%s §7schläft zZz, es %s noch §e%s Spieler §7um die Nacht zu überspringen.", event.getPlayer().getName(), required == 1 ? "fehlt" : "fehlen", required));
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        if (!event.getBed().getLocation().getWorld().getName().equalsIgnoreCase(MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn").getWorld().getName()))
            return;
        players--;
        if (skipTime - System.currentTimeMillis() > -5000)
            return;
        Bukkit.broadcastMessage(MinecraftWinter.getInstance().getPrefix() + String.format("§e%s §7hat das Bett verlassen.", event.getPlayer().getName()));
    }

    private int calculateNeeded() {
        int onlineSize = Math.toIntExact(Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().getName().equals("world")).count());
        return neededForSkip.getOrDefault(onlineSize, Math.toIntExact(onlineSize / 3));
    }

    private void handle(World world) {
        if (!world.getName().equals("world"))
            return;
        skipAllowed = true;
        world.setTime(0L);
        skipAllowed = false;
        skipTime = System.currentTimeMillis();
    }
}
