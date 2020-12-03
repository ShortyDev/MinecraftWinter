package at.shortydev.minecraftwinter.listener;

import at.shortydev.minecraftwinter.MinecraftWinter;
import at.shortydev.minecraftwinter.predicates.PlayerAfkPredicate;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.BleedEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!MinecraftWinter.started || PlayerMoveListener.SPAWNED.contains(player) || new PlayerAfkPredicate().test(player))
                event.setCancelled(true);
            else if (!player.isBlocking()) {
                Effect effect = new BleedEffect(MinecraftWinter.getInstance().getEffectManager());
                effect.setEntity(player);
                effect.iterations = 1;
                effect.start();
            }
        }
    }
}
