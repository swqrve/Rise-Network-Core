package me.swerve.file;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import me.swerve.RiseCore;
import me.swerve.cosmetic.Cosmetic;
import me.swerve.permission.Permission;
import me.swerve.player.CorePlayer;
import me.swerve.punishment.Punishment;
import me.swerve.rank.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.UUID;

public class FileManager {

    public static Document getPlayerDocument(UUID uuid) {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document document = new Document("uuid", uuid.toString()).append("uuid", uuid.toString());

        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.get("uuid") != null) if (doc.getString("uuid").equalsIgnoreCase(uuid.toString())) document = doc;


        if(document.get("rank") == null) {
            document.append("rank", "Default");

            document.append("prefixID", 0);
            document.append("suffixID", 0);
            document.append("chatColorID", 0);
            document.append("nameColorID", 0);

            document.append("punishments", 0);

            document.append("coins", 0);

            document.append("owned-prefixes", Collections.emptyList());
            document.append("owned-suffixes", Collections.emptyList());
            document.append("owned-chatColors", Collections.emptyList());
            document.append("owned-nameColors", Collections.emptyList());
        }

        return document;
    }

    public static void savePlayerDocument(UUID uuid) {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document document = new Document("uuid", uuid.toString()).append("uuid", uuid.toString());
        CorePlayer player = CorePlayer.getCorePlayers().get(uuid);

        player.getRankProfile().save(document);
        player.getCosmeticsProfile().save(document);
        player.getPunishmentProfile().save(document);

        document.append("coins", player.getCoins());

        Document docToReplace = null;
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.get("uuid") != null) if (doc.getString("uuid").equalsIgnoreCase(uuid.toString())) docToReplace = doc;

        if (docToReplace == null) {
            collection.insertOne(document);
            return;
        }

        collection.findOneAndReplace(docToReplace, document);
    }

    public static void savePlayerDocument(UUID uuid, Document document) {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document docToReplace = null;
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.get("uuid") != null) if (doc.getString("uuid").equalsIgnoreCase(uuid.toString())) docToReplace = doc;

        if (docToReplace == null) {
            collection.insertOne(document);
            return;
        }

        collection.findOneAndReplace(docToReplace, document);
    }

    public static void savePlayerDocument(UUID uuid, String newRank) {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document document = getPlayerDocument(uuid);

        document.remove("rank");
        document.append("rank", newRank);

        Document docToReplace = null;
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.get("uuid") != null) if (doc.getString("uuid").equalsIgnoreCase(uuid.toString())) docToReplace = doc;

        if (docToReplace == null) {
            collection.insertOne(document);
            return;
        }

        collection.findOneAndReplace(docToReplace, document);
    }

    public static void savePlayerDocument(UUID uuid, Punishment newPunishment) {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document document = getPlayerDocument(uuid);

        int punishments = document.getInteger("punishments") + 1;
        document.remove("punishments");
        document.append("punishments", punishments);

        String punishmentTitle = "punishment-" + punishments + "-";

        document.append(punishmentTitle + "type", newPunishment.getType().toString().toLowerCase());
        document.append(punishmentTitle + "date", newPunishment.getPunishmentDate());
        document.append(punishmentTitle + "timeformat", newPunishment.getTimeFormat());
        document.append(punishmentTitle + "reason", newPunishment.getReason());
        document.append(punishmentTitle + "expires", newPunishment.isExpires());
        document.append(punishmentTitle + "time", newPunishment.getPunishmentTime());
        document.append(punishmentTitle + "punisher", newPunishment.getPunisherName());
        document.append(punishmentTitle + "expired", newPunishment.isExpired());

        Document docToReplace = null;
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.get("uuid") != null) if (doc.getString("uuid").equalsIgnoreCase(uuid.toString())) docToReplace = doc;

        if (docToReplace == null) {
            collection.insertOne(document);
            return;
        }

        collection.findOneAndReplace(docToReplace, document);
    }


    public static void loadPermissions() {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document permissionsDoc = new Document("permissions", "permissions");

        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.getString("permissions") != null && doc.getString("permissions").equalsIgnoreCase("permissions")) {
            permissionsDoc = doc;
            System.out.println("Found a doc! - Permissions");
        }

        int permissionsCount = 0;
        if (permissionsDoc.getInteger("permissionCount") != null) permissionsCount = permissionsDoc.getInteger("permissionCount");

        for (int i = 0; i < permissionsCount; i++) {
            String title = "permission-" + i + "-";

            new Permission(
                    permissionsDoc.getInteger(title + "level"),
                    permissionsDoc.getString(title + "name")
            );
        }
    }

    public static void savePermissions() {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document permissionsDoc = new Document("permissions", "permissions");

        int permissionCount = Permission.getPermissions().size();

        for (int i = 0; i < permissionCount; i++) {
            String title = "permission-" + i + "-";

            permissionsDoc.append(title + "level", Permission.getPermissions().get(i).getPermissionLevel());
            permissionsDoc.append(title + "name", Permission.getPermissions().get(i).getPermissionName());
        }

        permissionsDoc.append("permissionCount", permissionCount);

        Document docToReplace = null;
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.getString("permissions") != null && doc.getString("permissions").equalsIgnoreCase("permissions")) docToReplace = doc;

        if (docToReplace == null) {
            collection.insertOne(permissionsDoc);
            return;
        }

        collection.findOneAndReplace(docToReplace, permissionsDoc);
    }

    public static void loadRanks() {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");

        Document rankDoc = new Document("ranks", "ranks");

        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.getString("ranks") != null && doc.getString("ranks").equalsIgnoreCase("ranks")) rankDoc = doc;

        int rankCount = 0;
        if (rankDoc.getInteger("rankCount") != null) rankCount = rankDoc.getInteger("rankCount");

        for (int i = 0; i < rankCount; i++) {
            String title = "rank-" + i + "-";

            new Rank(
                    rankDoc.getString(title + "prefix"),
                    rankDoc.getString(title + "nameColor"),
                    rankDoc.getString(title + "name"),
                    rankDoc.getInteger(title + "level")
            );
        }
    }
    public static void loadCosmetics() {
        MongoCollection<Document> collection = RiseCore.getInstance().getMongoDatabase().getCollection("mainCollection");
        Document cosmeticDoc = new Document("cosmetics", "cosmetics");

        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) if (doc.getString("cosmetics") != null && doc.getString("cosmetics").equalsIgnoreCase("cosmetics")) cosmeticDoc = doc;

        int cosmeticCount = 0;
        if (cosmeticDoc.getInteger("cosmeticCount") != null) cosmeticCount = cosmeticDoc.getInteger("cosmeticCount");

        for (int i = 0; i < cosmeticCount; i++) {
            String title = "cosmetic-" + i + "-";

            new Cosmetic(
                    Cosmetic.CosmeticType.valueOf (
                            cosmeticDoc.getString(title + "type")),
                            cosmeticDoc.getString(title + "value"),
                            cosmeticDoc.getInteger(title + "id")
            );
        }
    }
}
