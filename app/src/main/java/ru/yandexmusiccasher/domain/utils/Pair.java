package ru.yandexmusiccasher.domain.utils;

/**
 * Created by grish on 18.04.2017.
 */

public class Pair<T, U> {

    public final T f;
    public final U s;

    public Pair(T f, U s) {
        this.f = f;
        this.s = s;
    }
}