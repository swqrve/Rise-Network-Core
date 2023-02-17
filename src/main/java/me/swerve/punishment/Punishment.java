package me.swerve.punishment;

import lombok.Getter;
import lombok.Setter;
import me.swerve.util.TimeUtil;
import org.bukkit.ChatColor;

import java.util.Calendar;
import java.util.Date;

public class Punishment {
    public enum PunishmentType { BAN, MUTE, WARN }

    @Getter private final PunishmentType type;
    @Getter private final Date punishmentDate;

    @Getter private final int timeFormat;

    @Getter private final String reason;

    @Getter private final boolean expires;
    @Getter private int punishmentTime;
    @Getter String punisherName;

    @Getter @Setter private boolean expired;


    public Punishment(PunishmentType type, Date punishmentDate, int timeFormat, String reason, boolean expires, int punishmentTime, String punisherName, boolean expired) {
        this.type = type;

        this.punishmentDate = punishmentDate;
        this.timeFormat = timeFormat;
        this.reason = reason;

        this.expires = expires;
        if (expires) this.punishmentTime = punishmentTime;

        this.punisherName = punisherName;

        this.expired = expired;
    }

    public String getBanMessage() {
        String reason = getReason();
        if (reason == null || reason.length() < 1) reason = "Not listed.";

        String remainingTime;
        if (!(isExpires())) remainingTime = "Infinity. :)";
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getPunishmentDate());
            calendar.add(getTimeFormat(), getPunishmentTime());

            int differenceInSeconds  = TimeUtil.differenceInSeconds(calendar.getTime(), getPunishmentDate());

            remainingTime = String.format("%02d:%02d", (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
            if (differenceInSeconds / 3600 != 0) remainingTime = String.format("%02d:%02d:%02d", differenceInSeconds / 3600, (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
        }

       return ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &cYou are currently banned from the Rise Network. Reason: &f" + reason + " &cBanned on: &f" + getPunishmentDate().toString() +  " &cTime Remaining: &f" + remainingTime);
    }

    public String getMuteMessage() {
        String remainingTime;
        if (!(isExpires())) remainingTime = "Infinity. :)";
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getPunishmentDate());
            calendar.add(getTimeFormat(), getPunishmentTime());

            int differenceInSeconds  = TimeUtil.differenceInSeconds(calendar.getTime(), getPunishmentDate());

            remainingTime = String.format("%02d:%02d", (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
            if (differenceInSeconds / 3600 != 0) remainingTime = String.format("%02d:%02d:%02d", differenceInSeconds / 3600, (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
        }

        return ChatColor.translateAlternateColorCodes('&', "&cYou are muted. Time Remaining: &f" + remainingTime);
    }
}
