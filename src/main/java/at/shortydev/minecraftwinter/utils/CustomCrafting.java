package at.shortydev.minecraftwinter.utils;

import at.shortydev.minecraftwinter.MinecraftWinter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCrafting {

    public CustomCrafting() {
        ItemStack result = new ItemStack(Material.SNOW);
        NamespacedKey namespacedKey = new NamespacedKey(MinecraftWinter.getInstance(), "snow");
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, result);
        shapedRecipe.shape("%%");
        shapedRecipe.setIngredient('%', Material.SNOWBALL);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }
}
