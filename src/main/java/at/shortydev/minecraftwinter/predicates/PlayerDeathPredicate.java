package at.shortydev.minecraftwinter.predicates;

import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class PlayerDeathPredicate implements Predicate<Player> {
    @Override
    public boolean test(Player player) {
        return player.isDead();
    }
}
