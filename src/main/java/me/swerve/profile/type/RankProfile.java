package me.swerve.profile.type;

import lombok.Getter;
import lombok.Setter;
import me.swerve.profile.Profile;
import org.bson.Document;


public class RankProfile extends Profile {
    @Getter @Setter private String rankName;

    @Override
    public void save(Document document) {
        document.append("rank", rankName);
    }
}
