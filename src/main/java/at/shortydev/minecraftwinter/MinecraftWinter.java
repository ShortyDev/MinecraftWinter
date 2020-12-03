package at.shortydev.minecraftwinter;

import at.shortydev.minecraftwinter.commands.*;
import at.shortydev.minecraftwinter.listener.*;
import at.shortydev.minecraftwinter.location.WinterLocationController;
import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import at.shortydev.minecraftwinter.utils.CustomCrafting;
import com.google.gson.Gson;
import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Plugin(name = "MinecraftWinter", version = "1.0.0")
@Author(value = "Shorty")
@Description(value = "Sponsored by mc-protection.eu")
@LoadOrder(value = PluginLoadOrder.POSTWORLD)
@ApiVersion(value = ApiVersion.Target.v1_13)
@DependsOn({@Dependency(value = "EffectLib")})
@Commands({@Command(name = "winter"), @Command(name = "farmwelt", aliases = {"fw"}), @Command(name = "spawn", aliases = {"normal"}), @Command(name = "invisibleframe", aliases = {"if", "getframe"}), @Command(name = "status"), @Command(name = "viewchatrange", aliases = {"vcr"})})
public class MinecraftWinter extends JavaPlugin {

    @Getter
    private static MinecraftWinter instance;
    private final String prefix = "§b§lWINTER §8| ";

    public static boolean started = false;
    public static final Gson GSON = new Gson();
    public final Map<String, String> statusCache = new HashMap<>();

    private WinterLocationController winterLocation;
    private FileConfiguration fileConfiguration;
    private int velocityHeight = 0;
    private int teleportHeight = 0;

    private EffectManager effectManager;

    @Override
    public void onEnable() {
        instance = this;
        winterLocation = new WinterLocationController();

        effectManager = new EffectManager(this);

        setupConfig();

        getCommand("winter").setExecutor(new WinterCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("farmwelt").setExecutor(new FarmweltCommand());
        getCommand("invisibleframe").setExecutor(new InvisibleFrameCommand());
        getCommand("status").setExecutor(new StatusCommand());
        getCommand("viewchatrange").setExecutor(new ViewChatRadiusCommand());

        Listener[] listeners = new Listener[]{
                new PlayerJoinListener(), new PlayerQuitListener(), new PlayerDeathListener(), new PlayerRespawnListener(), new PlayerMoveListener(), new EntityDamageListener(),
                new InventoryClickListener(), new PlayerToggleFlightListener(), new FoodLevelChangeListener(), new BlockBuildListener(), new BedListener(), new AsyncPlayerChatListener(),
                new PlayerInteractListener()
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
                Bukkit.getOnlinePlayers().stream()
                        .filter(playerAfkPredicate)
                        .forEach(player -> player.setPlayerListName("§7[§c§oAFK§7] " + player.getName() + (player.isOp() ? " §7[§cOP§7]" : "")));
                Bukkit.getOnlinePlayers().forEach(player -> player.setPlayerListFooter("\n§cDeine Spielzeit\n§8" + getTimeString(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) + "\n"));
            }
        }.runTaskTimer(this, 0L, 100L);
    }

    private void setupConfig() {
        saveDefaultConfig();
        saveConfig();
        loadValues();

        this.fileConfiguration = getConfig();
    }

    private String getTimeString(long seconds) {
        long minutes = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        long hours = 0;
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        long days = 0;
        while (hours >= 24) {
            hours -= 24;
            days++;
        }
        return (days == 0 ? "" : days + "d") + (hours + "h") + (minutes + "m");
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().stream().filter(PlayerMoveListener.SPAWNED::contains).forEach(player -> player.getInventory().setChestplate(null));

        effectManager.dispose();
    }

    public void setStatus(Player player, @Nullable String status) {
        String format = "%s%s%s";
        if (status == null)
            status = statusCache.getOrDefault(player.getUniqueId().toString(), "");
        player.setPlayerListName(String.format(format, status.length() != 0 ? "§7[" + status + "§7] " : "§7", player.getName(), player.isOp() ? " §7[§cOP§7]" : ""));
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
