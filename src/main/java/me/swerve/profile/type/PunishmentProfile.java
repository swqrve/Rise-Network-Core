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
            if (p.getType() != type) continue;
            if (p.isExpired()) continue;

            return p;
        }

        return null;
    }

    @Override
    public void save(Document document) {
        document.append("punishments", punishments.size());

        int punishmentID = 1;
        for (Punishment punishment : punishments) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            document.append(punishmentTitle + "type", punishment.getType().toString().toLowerCase());
            document.append(punishmentTitle + "date", punishment.getPunishmentDate());
            document.append(punishmentTitle + "timeformat", punishment.getTimeFormat());
            document.append(punishmentTitle + "reason", punishment.getReason());
            document.append(punishmentTitle + "expires", punishment.isExpires());
            document.append(punishmentTitle + "time", punishment.getPunishmentTime());
            document.append(punishmentTitle + "punisher", punishment.getPunisherName());
            document.append(punishmentTitle + "expired", punishment.isExpired());

            punishmentID++;
        }
    }
}
