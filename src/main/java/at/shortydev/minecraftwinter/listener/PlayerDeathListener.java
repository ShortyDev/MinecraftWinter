package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import de.slikey.effectlib.effect.TornadoEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        assert player != null;
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH,  1, 1);

        TornadoEffect tornadoEffect = new TornadoEffect(MinecraftWinter.getInstance().getEffectManager());
        tornadoEffect.iterations = 5;
        tornadoEffect.setLocation(player.getLocation());
        tornadoEffect.start();
    }
}
