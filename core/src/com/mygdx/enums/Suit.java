package com.mygdx.enums;

public enum Suit {
    Club("club", 0), Heart("heart", 1), Spade("spade", 2), Diamond("diamond", 3);
    public final String name;
    public final int index;

    Suit(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
