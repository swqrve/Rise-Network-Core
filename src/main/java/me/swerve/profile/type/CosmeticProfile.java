package me.swerve.profile.type;

import lombok.Getter;
import lombok.Setter;
import me.swerve.profile.Profile;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CosmeticProfile extends Profile {

    @Getter private final List<Integer> ownedPrefixes = new ArrayList<>();
    @Getter private final List<Integer> ownedSuffixes = new ArrayList<>();
    @Getter private final List<Integer> ownedChatColors = new ArrayList<>();
    @Getter private final List<Integer> ownedNameColors = new ArrayList<>();

    @Getter @Setter private int currentPrefixID;
    @Getter @Setter private int currentSuffixID;

    @Getter @Setter private int currentChatColorID;
    @Getter @Setter private int currentNameColorID;

    @Override
    public void save(Document document) {
        document.append("prefixID", getCurrentPrefixID());
        document.append("suffixID", getCurrentSuffixID());
        document.append("chatColorID", getCurrentChatColorID());
        document.append("nameColorID", getCurrentNameColorID());


        document.append("owned-prefixes", ownedPrefixes);
        document.append("owned-suffixes", ownedSuffixes);
        document.append("owned-chatColors", ownedChatColors);
        document.append("owned-nameColors", ownedNameColors);
    }
}
