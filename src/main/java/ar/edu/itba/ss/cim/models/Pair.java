package ar.edu.itba.ss.cim.models;

import java.util.Objects;

public class Pair<T> {
    private final T value1;
    private final T value2;

    Pair(T value1, T value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public static <T> Pair<T> of(T value1, T value2) {
        return new Pair<>(value1, value2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?> pair)) return false;
        return (value1.equals(pair.value1) && value2.equals(pair.value2)) || (value1.equals(pair.value2) && value2.equals(pair.value1));

    }

    @Override
    public int hashCode() {
        return Objects.hash(value1.hashCode() + value2.hashCode());
    }
}
