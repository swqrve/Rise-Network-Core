package me.swerve.menu.board;

import me.swerve.file.FileManager;
import me.swerve.menu.Menu;
import me.swerve.menu.Page;
import me.swerve.permission.Permission;
import me.swerve.player.CorePlayer;
import me.swerve.rank.Rank;
import me.swerve.util.ColorUtil;
import me.swerve.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import javax.swing.text.Document;
import java.util.UUID;

public class RankMenu extends Menu {

    private final UUID uuid;
    public RankMenu(Player setter, UUID uuidToSet) {
        super("Set Rank", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);
        uuid = uuidToSet;

        Page mainPage = new Page(18, "main");

        int totalRanks = Rank.getRanks().size();
        for (int i = 0; i < totalRanks; i++) {
            Rank rank = null;
            for (Rank r : Rank.getRanks().values()) if (r.getPermissionLevel() == i + 1) rank = r;
            if (rank == null) return;

            mainPage.put(i, new ItemCreator(Material.WOOL, 1).setData(ColorUtil.colorCodeToWoolData(rank.getNameColor()).getValueOne()).setName(rank.getNameColor() + rank.getRankName()).getItem());
        }

        addPage(mainPage);

        updateInventory(setter);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.WOOL)  {
            Rank selectedRank = Rank.getRanks().get(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
            if (selectedRank == null) return;

            FileManager.savePlayerDocument(uuid, selectedRank.getRankName());

            if (Bukkit.getPlayer(uuid) != null) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour rank has been changed. You are now rank: " + selectedRank.getNameColor() + selectedRank.getRankName()));

                CorePlayer.getCorePlayers().get(uuid).updateRank(selectedRank.getRankName());
            }

            e.getWhoClicked().closeInventory();
        }
    }

    @Override public void lastChance(Inventory inventory) { }
}