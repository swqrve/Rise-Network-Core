package me.swerve.menu.board;

import me.swerve.menu.Menu;

import me.swerve.menu.Page;
import me.swerve.permission.Permission;
import me.swerve.player.CorePlayer;
import me.swerve.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PermissionMenu extends Menu {

    public PermissionMenu(Player p) {
        super("View Permissions", Menu.InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(18, "main");
        addPage(mainPage);

        int maxPermissionLevel = 0;
        for (Permission perm : Permission.getPermissions()) if (perm.getPermissionLevel() > maxPermissionLevel) maxPermissionLevel = perm.getPermissionLevel();

        for (int i = 0; i <= maxPermissionLevel; i++) {
            mainPage.put(i, new ItemCreator(Material.NAME_TAG, 1).setName("&c&lPermission Level: &r&f" + i).getItem());

            Page pageToAdd = new Page(27, "" + i);
            pageToAdd.getPageContents().put(26, new ItemCreator(Material.ARROW, 1).setName("&cReturn").getItem());
            addPage(pageToAdd);
        }

        for (Permission perm : Permission.getPermissions()) {
            for (Page page : getPages()) {
                if (Objects.equals(page.getIdentifier(), "main")) continue;
                if (!(perm.getPermissionLevel() <= Integer.parseInt(page.getIdentifier()))) continue;
                for (int i = 0; i < 27; i++) {
                    if (page.getPageContents().get(i) != null) continue;
                    page.getPageContents().put(i, new ItemCreator(Material.PAPER, 1).setName("&a" + perm.getPermissionName()).getItem());
                    break;
                }
            }
        }


        updateInventory(p);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.NAME_TAG)  {
            String pageIdentifier = "" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replaceAll("[^0-9]","");

            for (int i = 0; i < getPages().size(); i++) {
                Page page = getPages().get(i);
                if (page.getIdentifier().equalsIgnoreCase(pageIdentifier)) setCurrentPage(i);
            }

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.PAPER) {
            Permission permission = null;
            for (Permission perm : Permission.getPermissions()) if (perm.getPermissionName().equalsIgnoreCase(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) permission = perm;
            if (permission == null) return;

            CorePlayer corePlayer = CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId());

            corePlayer.setWaitingForInput(true);
            corePlayer.setPermissionToEdit(permission);

            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the new permission name, or write a new permission level. You can also type delete to remove the permission, and cancel to cancel all actions."));
            e.getWhoClicked().closeInventory();
        }

        if (e.getCurrentItem().getType() == Material.ARROW) {
            setCurrentPage(0);

            updateInventory((Player) e.getWhoClicked());
            return;
        }
    }

    @Override public void lastChance(Inventory inventory) { }
}