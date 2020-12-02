package at.shortydev.minecraftwinter;

import at.shortydev.minecraftwinter.commands.*;
import at.shortydev.minecraftwinter.listener.*;
import at.shortydev.minecraftwinter.location.WinterLocationController;
import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import at.shortydev.minecraftwinter.utils.CustomCrafting;
import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

@Getter
@Plugin(name = "MinecraftWinter", version = "1.0.0")
@Author(value = "Shorty")
@Description(value = "Sponsored by mc-protection.eu")
@LoadOrder(value = PluginLoadOrder.POSTWORLD)
@ApiVersion(value = ApiVersion.Target.v1_13)
@Commands({@Command(name = "winter"), @Command(name = "farmwelt", aliases = {"fw"}), @Command(name = "spawn", aliases = {"normal"}), @Command(name = "invisibleframe", aliases = {"if", "getframe"}), @Command(name = "status"), @Command(name = "viewchatrange", aliases = {"vcr"})})
public class MinecraftWinter extends JavaPlugin {

    @Getter
    private static MinecraftWinter instance;
    private final String prefix = "§b§lWINTER §8| ";

    public static boolean started = false;
    public static final Gson GSON = new Gson();

    private WinterLocationController winterLocation;
    private FileConfiguration fileConfiguration;
    private int velocityHeight = 0;
    private int teleportHeight = 0;

    @Override
    public void onEnable() {
        instance = this;
        winterLocation = new WinterLocationController();

        setupConfig();

        getCommand("winter").setExecutor(new WinterCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("farmwelt").setExecutor(new FarmweltCommand());
        getCommand("invisibleframe").setExecutor(new InvisibleFrameCommand());
        getCommand("status").setExecutor(new StatusCommand());
        getCommand("viewchatrange").setExecutor(new ViewChatRadiusCommand());

        Listener[] listeners = new Listener[]{
                new PlayerJoinListener(), new PlayerQuitListener(), new PlayerDeathListener(), new PlayerRespawnListener(), new PlayerMoveListener(), new EntityDamageListener(),
                new InventoryClickListener(), new PlayerToggleFlightListener(), new FoodLevelChangeListener(), new BlockBuildListener(), new BedListener(), new AsyncPlayerChatListener()
        };
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        checkAFK();

        try {
            new CustomCrafting();
        } catch (Exception ignored) {
        }
    }

    private void checkAFK() {
        PlayerAfkPredicate playerAfkPredicate = new PlayerAfkPredicate();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (playerAfkPredicate.test(player))
                        player.setPlayerListName("§7[§c§oAFK§7] " + player.getName() + (player.isOp() ? " §7[§cOP§7]" : ""));
                });
            }
        }.runTaskTimer(this, 0L, 100L);
    }

    private void setupConfig() {
        saveDefaultConfig();
        saveConfig();
        loadValues();

        this.fileConfiguration = getConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().stream().filter(PlayerMoveListener.SPAWNED::contains).forEach(player -> player.getInventory().setChestplate(null));
    }

    public void loadValues() {
        this.velocityHeight = getConfig().getInt("vheight");
        this.teleportHeight = getConfig().getInt("tpheight");
        started = getConfig().getBoolean("start");

        if (!started)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission set essentials.warp false");
        else
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission set essentials.warp true");
    }

}
