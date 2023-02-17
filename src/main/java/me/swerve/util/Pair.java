package me.swerve.util;

import lombok.Getter;
import lombok.Setter;

public class Pair {
    @Getter @Setter private Integer valueOne;
    @Getter @Setter private String valueTwo;

    public Pair(Integer valueOne, String valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }
}
