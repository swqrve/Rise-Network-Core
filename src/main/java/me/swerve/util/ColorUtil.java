package me.swerve.util;

public class ColorUtil {
    public static Pair colorCodeToWoolData(String colorCode) {
        switch (colorCode) {
            case "&0": // Black
                return new Pair(15, "Black");
            case "&1": // Dark Blue
                return new Pair(11, "Dark Blue");
            case "&2": // Dark Green
                return new Pair(13, "Dark Green");
            case "&3": // Dark Aqua
                return new Pair(11, "Dark Aqua");
            case "&4": // Dark Red
                return new Pair(14, "Dark Red");
            case "&5": // Dark Purple
                return new Pair(10, "Dark Purple");
            case "&6": // Gold
                return new Pair(4, "Gold");
            case "&7": // Gray
                return new Pair(8, "Gray");
            case "&8": // Dark Gray
                return new Pair(8, "Dark Gray");
            case "&9": // Blue
                return new Pair(11, "Blue");
            case "&a": // Green
                return new Pair(13, "Green");
            case "&b": // Aqua
                return new Pair(3, "Aqua");
            case "&c": // Red
                return new Pair(14, "Red");
            case "&d": // Light PUrple
                return new Pair(10, "Light Purple");
            case "&e": // Yellow
                return new Pair(4, "Yellow");
            case "&f": // White
            default:
                return new Pair(0, "White");
        }
    }
}
