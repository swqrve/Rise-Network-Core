package me.swerve.cosmetic;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class Cosmetic {
    public enum CosmeticType { PREFIX, SUFFIX, CHAT_COLOR, NAME_COLOR }

    @Getter private static final List<Cosmetic> cosmetics = new ArrayList<>();

    @Getter private final int cosmeticID;
    @Getter private final String prefix;
    @Getter private final String suffix;
    @Getter private final String chatColor;
    @Getter private final String nameColor;

    @Getter private final CosmeticType cosmeticType;

    public Cosmetic(CosmeticType type, String value, int id) {
        this.cosmeticType = type;
        this.cosmeticID = id;

        this.prefix = value;
        this.suffix = value;
        this.nameColor = value;
        this.chatColor = value;

        cosmetics.add(this);
    }

    public static Cosmetic getCosmetic(CosmeticType type, int id) {
        for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
            if (cosmetic.getCosmeticType() != type) continue;
            if (cosmetic.getCosmeticID() != id) continue;

            return cosmetic;
        }

        return null;
    }
}
