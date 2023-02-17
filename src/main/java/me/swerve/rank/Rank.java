package me.swerve.rank;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public class Rank {
    @Getter private static final Map<String, Rank> ranks = new HashMap<>();

    @Getter private final String prefix;
    @Getter private final String nameColor;

    @Getter private final String rankName;
    @Getter private final int permissionLevel;

    public Rank(String prefix, String nameColor, String rankName, int permissionLevel) {
        this.prefix = prefix;
        this.nameColor = nameColor;
        this.rankName = rankName;

        this.permissionLevel = permissionLevel;

        ranks.put(rankName, this);
    }
}
