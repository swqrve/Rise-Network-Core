package me.swerve.profile.type;

import lombok.Getter;
import me.swerve.profile.Profile;
import me.swerve.punishment.Punishment;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class PunishmentProfile extends Profile {
    @Getter private final List<Punishment> punishments = new ArrayList<>();

    public Punishment getActive(Punishment.PunishmentType type) {
        for (Punishment p : punishments) {
            p.update();

            if (p.getType() != type) continue;
            if (p.isExpired()) continue;

            return p;
        }

        return null;
    }

    @Override
    public void save(Document document) {
        append(document,"punishments", punishments.size());

        int punishmentID = 1;
        for (Punishment punishment : punishments) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            append(document,punishmentTitle + "type", punishment.getType().toString().toLowerCase());
            append(document,punishmentTitle + "date", punishment.getPunishmentDate());
            append(document,punishmentTitle + "timeformat", punishment.getTimeFormat());
            append(document,punishmentTitle + "reason", punishment.getReason());
            append(document,punishmentTitle + "expires", punishment.isExpires());
            append(document,punishmentTitle + "time", punishment.getPunishmentTime());
            append(document,punishmentTitle + "punisher", punishment.getPunisherName());
            append(document,punishmentTitle + "expired", punishment.isExpired());

            punishmentID++;
        }
    }

    private void append(Document document, String key, Object value) {
        if (document.get(key) != null) document.remove(key);
        document.append(key, value);
    }
}
