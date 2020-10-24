package uk.co.benjiweber.expressions;

public class Times {
    public interface Action { void apply(); }
    public interface TimesBuilder { void invoke(Action action); }

    public static TimesBuilder times(int times) {
        return action -> {
            for (int i = 0; i < times; i++) {
                action.apply();
            }
        };
    }
}
