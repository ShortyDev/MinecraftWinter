package at.shortydev.minecraftwinter.commands;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class InvisibleFrameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (removeItemFrame(player.getInventory())) {
                ItemStack itemStack = new ItemStack(Material.ITEM_FRAME, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§cWinter-Frame");
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§7Du hast ein unsichtbaren Frame erhalten.");
            } else {
                player.sendMessage(MinecraftWinter.getInstance().getPrefix() + "§cDu brauchst mindestens einen Item-Rahmen.");
            }
        }
        return false;
    }

    public boolean removeItemFrame(Inventory inventory) {
        AtomicBoolean success = new AtomicBoolean(false);
        Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .filter(content -> content.getType() == Material.ITEM_FRAME)
                .filter(content -> content.getAmount() > 0)
                .limit(1)
                .forEach(content -> {
                    if (content.getAmount() == 1)
                        inventory.remove(content);
                    else
                        content.setAmount(content.getAmount() - 1);
                    success.set(true);
                });
        return success.get();
    }
}
