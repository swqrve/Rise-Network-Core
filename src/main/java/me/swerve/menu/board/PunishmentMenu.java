package me.swerve.menu.board;

import me.swerve.file.FileManager;
import me.swerve.menu.Menu;
import me.swerve.menu.Page;
import me.swerve.player.CorePlayer;
import me.swerve.profile.type.PunishmentProfile;
import me.swerve.punishment.Punishment;
import me.swerve.util.ItemCreator;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

public class PunishmentMenu extends Menu {

    public PunishmentMenu(Player p, UUID player, String name) {
        super(name + "'s Punishments", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(9, "main");

        mainPage.put(2, new ItemCreator(Material.WOOL, 1).setData(DyeColor.YELLOW.getWoolData()).setName("&cWarns").getItem());
        mainPage.put(4, new ItemCreator(Material.WOOL, 1).setData(DyeColor.RED.getWoolData()).setName("&cBans").getItem());
        mainPage.put(6, new ItemCreator(Material.WOOL, 1).setData(DyeColor.PURPLE.getWoolData()).setName("&cMutes").getItem());

        Page warnsPage = new Page(27, "Warns");
        Page bansPage = new Page(27, "Bans");
        Page mutesPage = new Page(27, "Mutes");

        if (CorePlayer.getCorePlayers().get(player) != null) CorePlayer.getCorePlayers().get(player).save();
        Document doc = FileManager.getPlayerDocument(player);

        int punishmentCount = doc.getInteger("punishments");

        PunishmentProfile profile = new PunishmentProfile();

        for (int punishmentID = 1; punishmentID <= punishmentCount; punishmentID++) {
            String punishmentTitle = "punishment-" + punishmentID + "-";

            profile.getPunishments().add(new Punishment(
                    Punishment.PunishmentType.valueOf(doc.getString(punishmentTitle + "type").toUpperCase()),
                    doc.getDate(punishmentTitle + "date"),
                    doc.getInteger(punishmentTitle + "timeformat"),
                    doc.getString(punishmentTitle + "reason"),
                    doc.getBoolean(punishmentTitle + "expires"),
                    doc.getInteger(punishmentTitle + "time"),
                    doc.getString(punishmentTitle + "punisher"),
                    doc.getBoolean(punishmentTitle + "expired")
            ));
        }

        for (Punishment punishment : profile.getPunishments()) {
            punishment.update();

            Page pageToAddTo = null;
            switch (punishment.getType()) {
                case BAN:
                    pageToAddTo = bansPage;
                    break;
                case MUTE:
                    pageToAddTo = mutesPage;
                    break;
                case WARN:
                    pageToAddTo = warnsPage;
                    break;
            }

            for (int i = 0; i < 27; i++) {
                if (pageToAddTo.getPageContents().get(i) != null) continue;
                pageToAddTo.getPageContents().put(i,
                        new ItemCreator(Material.PAPER, 1)
                                .setName("&c" + punishment.getPunishmentDate().toString())
                                .addLore(Arrays.asList(
                                        "&cReason: &f" + punishment.getReason(),
                                        "&cIssuer: &f" + punishment.getPunisherName(),
                                        "&cPunishment Time: &f" + getPunishmentTimeFormat(punishment),
                                        "&cExpired: &f" + punishment.isExpired()
                                        ))
                                .getItem());
                break;
            }
        }

        addPage(mainPage);
        addPage(warnsPage);
        addPage(mutesPage);
        addPage(bansPage);

        for (Page page : getPages()) if (!page.getIdentifier().equalsIgnoreCase("main")) page.put(26, new ItemCreator(Material.ARROW, 1).setName("&cReturn").getItem());

        updateInventory(p);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.WOOL)  {
            String pageIdentifier = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            for (int i = 0; i < getPages().size(); i++) {
                Page page = getPages().get(i);
                if (page.getIdentifier().equalsIgnoreCase(pageIdentifier)) setCurrentPage(i);
            }

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.ARROW) {
            setCurrentPage(0);

            updateInventory((Player) e.getWhoClicked());
        }
    }

    @Override public void lastChance(Inventory inventory) { }

    private String getPunishmentTimeFormat(Punishment punishment) {
        if (!punishment.isExpires()) return "Never";

        String toReturn = punishment.getPunishmentTime() + "";
        switch (punishment.getTimeFormat()) {
            case Calendar.SECOND:
                return toReturn + "s";
            case Calendar.MINUTE:
                return toReturn + "m";
            case Calendar.HOUR:
                return toReturn + "h";
            case Calendar.DATE:
            default:
                return toReturn + "d";
        }
    }
}