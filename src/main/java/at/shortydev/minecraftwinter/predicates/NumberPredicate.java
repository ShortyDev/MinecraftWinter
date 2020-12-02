package at.shortydev.minecraftwinter.predicates;

import java.util.function.Predicate;

public class NumberPredicate implements Predicate<String> {
    @Override
    public boolean test(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
