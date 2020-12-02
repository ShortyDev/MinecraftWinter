package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBuildListener implements Listener {

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getPlayer() == null)
            return;
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.ITEM_FRAME && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName())
            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§cWinter-Frame")) {
                ItemFrame itemFrame = (ItemFrame) event.getEntity();
                itemFrame.setVisible(false);
            }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Location location = MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn");
        if (location.getWorld().getName().equalsIgnoreCase(event.getBlock().getLocation().getWorld().getName()) && location.distance(event.getBlock().getLocation()) < 20 && !event.getPlayer().isOp())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location location = MinecraftWinter.getInstance().getWinterLocation().getLocation("spawn");
        if (location.getWorld().getName().equalsIgnoreCase(event.getBlock().getLocation().getWorld().getName()) && location.distance(event.getBlock().getLocation()) < 20 && !event.getPlayer().isOp())
            event.setCancelled(true);
    }
}
