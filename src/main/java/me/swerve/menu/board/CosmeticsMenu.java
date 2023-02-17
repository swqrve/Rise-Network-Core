package me.swerve.menu.board;

import me.swerve.cosmetic.Cosmetic;
import me.swerve.menu.Menu;
import me.swerve.menu.Page;
import me.swerve.player.CorePlayer;
import me.swerve.util.ColorUtil;
import me.swerve.util.ItemCreator;
import me.swerve.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

public class CosmeticsMenu extends Menu {

    public CosmeticsMenu(Player player) {
        super("Cosmetics", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(27, "main");
        CorePlayer p = CorePlayer.getCorePlayers().get(player.getUniqueId());

        for (int i = 0; i < 27; i++) mainPage.put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(DyeColor.GRAY.getData()).setName("").getItem());

        mainPage.put(10, new ItemCreator(Material.NAME_TAG, 1).setName("&3Prefixes").getItem());
        mainPage.put(11, new ItemCreator(Material.ANVIL, 1).setName("&3Suffixes").getItem());

        mainPage.put(13, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + p.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase different cosmetics! Name colors, prefixes, suffixes, chat colors, and more in the future!")).getItem());

        mainPage.put(15, new ItemCreator(Material.INK_SACK, 1).setData(12).setName("&3Chat Colors").getItem());
        mainPage.put(16, new ItemCreator(Material.INK_SACK, 1).setData(14).setName("&3Name Colors").getItem());

        mainPage.put(22, new ItemCreator(Material.TRIPWIRE_HOOK, 1).setName("&cDisable all cosmetics").getItem());

        Page prefixPage = new Page(27, "Prefixes");
        Page suffixPage = new Page(27, "Suffixes");
        Page chatPage = new Page(27, "Chat Colors");
        Page namePage = new Page(27, "Name Colors");

        addPage(mainPage);
        addPage(prefixPage);
        addPage(suffixPage);
        addPage(chatPage);
        addPage(namePage);

        for (Page page : getPages()) {
            if (page.getIdentifier().equalsIgnoreCase("main")) continue;

            for (int i = 0; i < 9; i++) page.put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(DyeColor.GRAY.getData()).setName("").getItem());
            page.put(0, new ItemCreator(Material.ARROW, 1).setName("&cReturn").getItem());
            page.put(8, new ItemCreator(Material.TRIPWIRE_HOOK, 1).setName("&cDisable Cosmetic").getItem());
            page.put(4, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + p.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase cosmetics, just click on a cosmetic you don't own!")).getItem());
        }

        for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
            String owned;
            String bottomLoreMessage;
            Pair colorInformation;

            switch (cosmetic.getCosmeticType()) {
                case PREFIX:
                    owned = " &7(&c&oX&7)";
                    if (p.getCosmeticsProfile().getOwnedPrefixes().contains(cosmetic.getCosmeticID())) owned = " &7(&a&o✓&7)";

                    bottomLoreMessage = "&aClick to Purchase! &7(&a&o✓&7)";
                    if (p.getCoins() < 500) bottomLoreMessage = "&cNot enough funds &7(&c&oX&7)";
                    if (!owned.contains("X")) bottomLoreMessage = "&aClick to select";

                    prefixPage.put(getNextFreeSlot(prefixPage), new ItemCreator(Material.NAME_TAG, 1).setName(cosmetic.getPrefix() + owned).addLore(
                            Arrays.asList("&7Prefix",
                                          "",
                                          "&7Preview: ",
                                          cosmetic.getPrefix() + " &7" + player.getDisplayName() + ": Pick me!",
                                          "",
                                          bottomLoreMessage)
                    ).getItem());
                    break;
                case SUFFIX:
                    owned = " &7(&c&oX&7)";
                    if (p.getCosmeticsProfile().getOwnedSuffixes().contains(cosmetic.getCosmeticID())) owned = " &7(&a&o✓&7)";

                    bottomLoreMessage = "&aClick to Purchase! &7(&a&o✓&7)";
                    if (p.getCoins() < 500) bottomLoreMessage = "&cNot enough funds &7(&c&oX&7)";
                    if (!owned.contains("X")) bottomLoreMessage = "&aClick to select";

                    suffixPage.put(getNextFreeSlot(suffixPage), new ItemCreator(Material.NAME_TAG, 1).setName(cosmetic.getSuffix() + owned).addLore(
                            Arrays.asList("&7Suffix",
                                    "",
                                    "&7Preview: ",
                                    "&7" + player.getDisplayName() + " " + cosmetic.getSuffix() + " &7: Pick me!",
                                    "",
                                    bottomLoreMessage)
                    ).getItem());
                    break;
                case NAME_COLOR:
                    owned = " &7(&c&oX&7)";
                    if (p.getCosmeticsProfile().getOwnedNameColors().contains(cosmetic.getCosmeticID())) owned = " &7(&a&o✓&7)";

                    bottomLoreMessage = "&aClick to Purchase! &7(&a&o✓&7)";
                    if (p.getCoins() < 750) bottomLoreMessage = "&cNot enough funds &7(&c&oX&7)";
                    if (!owned.contains("X")) bottomLoreMessage = "&aClick to select";


                    colorInformation = ColorUtil.colorCodeToWoolData(cosmetic.getNameColor());
                    namePage.put(getNextFreeSlot(namePage), new ItemCreator(Material.WOOL, 1).setData(colorInformation.getValueOne()).setName(cosmetic.getNameColor() + colorInformation.getValueTwo() + owned).addLore(
                            Arrays.asList("&7Name Color",
                                    "",
                                    "&7Preview: ",
                                    cosmetic.getNameColor() + player.getDisplayName() + ": &7Pick me!",
                                    "",
                                    bottomLoreMessage)
                    ).getItem());
                    break;
                case CHAT_COLOR:
                    owned = " &7(&c&oX&7)";
                    if (p.getCosmeticsProfile().getOwnedChatColors().contains(cosmetic.getCosmeticID())) owned = " &7(&a&o✓&7)";

                    bottomLoreMessage = "&aClick to Purchase! &7(&a&o✓&7)";
                    if (p.getCoins() < 750) bottomLoreMessage = "&cNot enough funds &7(&c&oX&7)";
                    if (!owned.contains("X")) bottomLoreMessage = "&aClick to select";

                    colorInformation = ColorUtil.colorCodeToWoolData(cosmetic.getChatColor());
                    chatPage.put(getNextFreeSlot(chatPage), new ItemCreator(Material.WOOL, 1).setData(colorInformation.getValueOne()).setName(cosmetic.getChatColor() + colorInformation.getValueTwo() + owned).addLore(
                            Arrays.asList("&7Chat Color",
                                    "",
                                    "&7Preview: ",
                                    "&7" + player.getDisplayName() + ": " + cosmetic.getChatColor() + "Pick me!",
                                    "",
                                    bottomLoreMessage)
                    ).getItem());
                    break;
            }

        }

        updateInventory(player);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (!(e.getCurrentItem().getItemMeta().hasLore()) && (e.getCurrentItem().getType() == Material.ANVIL || e.getCurrentItem().getType() == Material.INK_SACK || e.getCurrentItem().getType() == Material.NAME_TAG))  {
            String pageIdentifier = "" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            for (int i = 0; i < getPages().size(); i++) {
                Page page = getPages().get(i);
                if (page.getIdentifier().equalsIgnoreCase(pageIdentifier)) setCurrentPage(i);
            }

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        CorePlayer corePlayer = CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId());
        if (corePlayer == null) return;
        switch (currentPage.getIdentifier()) {
            case "Prefixes":
                if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled your prefix!"));
                    CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId()).getCosmeticsProfile().setCurrentPrefixID(0);

                    e.getWhoClicked().closeInventory();
                    return;
                }

                if (e.getCurrentItem().getType() == Material.NAME_TAG) {
                    Cosmetic chosenCosmetic = null;
                    for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
                        if (cosmetic.getCosmeticType() != Cosmetic.CosmeticType.PREFIX) continue;
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', cosmetic.getPrefix())))) {
                            chosenCosmetic = cosmetic;
                            break;
                        }
                    }

                    if (chosenCosmetic == null) return;

                    if (corePlayer.getCosmeticsProfile().getOwnedPrefixes().contains(chosenCosmetic.getCosmeticID())) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully changed your prefix to: " + chosenCosmetic.getPrefix()));
                        corePlayer.getCosmeticsProfile().setCurrentPrefixID(chosenCosmetic.getCosmeticID());
                        e.getWhoClicked().closeInventory();
                        return;
                    }

                    if (corePlayer.getCoins() >= 500 || e.getWhoClicked().hasPermission("cosmetics.override")) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully purchased the prefix: " + chosenCosmetic.getPrefix()));
                        corePlayer.getCosmeticsProfile().getOwnedPrefixes().add(chosenCosmetic.getCosmeticID());

                        currentPage.put(e.getSlot(), new ItemCreator(Material.NAME_TAG, 1).setName(chosenCosmetic.getPrefix() + " &7(&a&o✓&7)").addLore(
                                Arrays.asList("&7Prefix",
                                        "",
                                        "&7Preview: ",
                                        chosenCosmetic.getPrefix() + " &7" + e.getWhoClicked().getName() + ": Pick me!",
                                        "",
                                        "&aClick to select")
                        ).getItem());

                        corePlayer.setCoins(corePlayer.getCoins() - 500);
                        currentPage.put(4, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + corePlayer.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase cosmetics, just click on a cosmetic you don't own!")).getItem());

                        updateInventory((Player) e.getWhoClicked());
                        return;
                    }

                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't afford this prefix!"));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.CAT_HISS, 1, 1);
                    return;
                }
                break;
            case "Suffixes":
                if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled your suffix!"));
                    CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId()).getCosmeticsProfile().setCurrentSuffixID(0);

                    e.getWhoClicked().closeInventory();
                    return;
                }

                if (e.getCurrentItem().getType() == Material.NAME_TAG) {
                    Cosmetic chosenCosmetic = null;
                    for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
                        if (cosmetic.getCosmeticType() != Cosmetic.CosmeticType.SUFFIX) continue;
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', cosmetic.getSuffix())))) {
                            chosenCosmetic = cosmetic;
                            break;
                        }
                    }

                    if (chosenCosmetic == null) return;
                    if (corePlayer.getCosmeticsProfile().getOwnedSuffixes().contains(chosenCosmetic.getCosmeticID())) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully changed your suffix to: " + chosenCosmetic.getSuffix()));
                        corePlayer.getCosmeticsProfile().setCurrentSuffixID(chosenCosmetic.getCosmeticID());
                        e.getWhoClicked().closeInventory();
                        return;
                    }

                    if (corePlayer.getCoins() >= 500 || e.getWhoClicked().hasPermission("cosmetics.override")) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully purchased the suffix: " + chosenCosmetic.getSuffix()));
                        corePlayer.getCosmeticsProfile().getOwnedSuffixes().add(chosenCosmetic.getCosmeticID());

                        currentPage.put(e.getSlot(), new ItemCreator(Material.NAME_TAG, 1).setName(chosenCosmetic.getSuffix() + " &7(&a&o✓&7)").addLore(
                                Arrays.asList("&7Suffix",
                                        "",
                                        "&7Preview: ",
                                        "&7" + e.getWhoClicked().getName() + " " + chosenCosmetic.getSuffix() + " &7: Pick me!",
                                        "",
                                        "&aClick to select")
                        ).getItem());

                        corePlayer.setCoins(corePlayer.getCoins() - 500);
                        currentPage.put(4, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + corePlayer.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase cosmetics, just click on a cosmetic you don't own!")).getItem());

                        updateInventory((Player) e.getWhoClicked());
                        return;
                    }

                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't afford this suffix!"));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.CAT_HISS, 1, 1);
                    return;
                }
                break;
            case "Name Colors":
                if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled your name color!"));
                    CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId()).getCosmeticsProfile().setCurrentNameColorID(0);

                    e.getWhoClicked().closeInventory();
                    return;
                }

                if (e.getCurrentItem().getType() == Material.WOOL) {
                    Cosmetic chosenCosmetic = null;
                    for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
                        if (cosmetic.getCosmeticType() != Cosmetic.CosmeticType.NAME_COLOR) continue;
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains(ColorUtil.colorCodeToWoolData(cosmetic.getNameColor()).getValueTwo())) {
                            chosenCosmetic = cosmetic;
                            break;
                        }
                    }

                    if (chosenCosmetic == null) return;
                    Pair colorPair = ColorUtil.colorCodeToWoolData(chosenCosmetic.getNameColor());

                    if (corePlayer.getCosmeticsProfile().getOwnedNameColors().contains(chosenCosmetic.getCosmeticID())) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully changed your name color to: " + chosenCosmetic.getNameColor() + colorPair.getValueTwo()));
                        corePlayer.getCosmeticsProfile().setCurrentNameColorID(chosenCosmetic.getCosmeticID());
                        e.getWhoClicked().closeInventory();
                        return;
                    }

                    if (corePlayer.getCoins() >= 750 || e.getWhoClicked().hasPermission("cosmetics.override")) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully purchased the name color: " + chosenCosmetic.getNameColor() + colorPair.getValueTwo()));
                        corePlayer.getCosmeticsProfile().getOwnedNameColors().add(chosenCosmetic.getCosmeticID());

                        currentPage.put(e.getSlot(), new ItemCreator(Material.WOOL, 1).setData(colorPair.getValueOne()).setName(chosenCosmetic.getNameColor() + colorPair.getValueTwo() + " &7(&a&o✓&7)").addLore(
                                Arrays.asList("&7Name Color",
                                        "",
                                        "&7Preview: ",
                                        chosenCosmetic.getNameColor() + e.getWhoClicked().getName() + ": &7Pick me!",
                                        "",
                                        "&aClick to select")
                        ).getItem());

                        corePlayer.setCoins(corePlayer.getCoins() - 750);
                        currentPage.put(4, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + corePlayer.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase cosmetics, just click on a cosmetic you don't own!")).getItem());

                        updateInventory((Player) e.getWhoClicked());
                        return;
                    }

                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't afford this name color!"));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.CAT_HISS, 1, 1);
                    return;
                }
                break;
            case "Chat Colors":
                if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled your chat color!"));
                    CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId()).getCosmeticsProfile().setCurrentChatColorID(0);

                    e.getWhoClicked().closeInventory();
                    return;
                }

                if (e.getCurrentItem().getType() == Material.WOOL) {
                    Cosmetic chosenCosmetic = null;
                    for (Cosmetic cosmetic : Cosmetic.getCosmetics()) {
                        if (cosmetic.getCosmeticType() != Cosmetic.CosmeticType.CHAT_COLOR) continue;
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).contains(ColorUtil.colorCodeToWoolData(cosmetic.getChatColor()).getValueTwo())) {
                            chosenCosmetic = cosmetic;
                            break;
                        }
                    }

                    if (chosenCosmetic == null) return;
                    Pair colorPair = ColorUtil.colorCodeToWoolData(chosenCosmetic.getChatColor());

                    if (corePlayer.getCosmeticsProfile().getOwnedChatColors().contains(chosenCosmetic.getCosmeticID())) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully changed your chat color to: " + chosenCosmetic.getChatColor() + colorPair.getValueTwo()));
                        corePlayer.getCosmeticsProfile().setCurrentChatColorID(chosenCosmetic.getCosmeticID());
                        e.getWhoClicked().closeInventory();
                        return;
                    }

                    if (corePlayer.getCoins() >= 750 || e.getWhoClicked().hasPermission("cosmetics.override")) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully purchased the chat color: " + chosenCosmetic.getNameColor() + colorPair.getValueTwo()));
                        corePlayer.getCosmeticsProfile().getOwnedChatColors().add(chosenCosmetic.getCosmeticID());

                        currentPage.put(e.getSlot(), new ItemCreator(Material.WOOL, 1).setData(colorPair.getValueOne()).setName(chosenCosmetic.getChatColor() + colorPair.getValueTwo() + " &7(&a&o✓&7)").addLore(
                                Arrays.asList("&7Chat Color",
                                        "",
                                        "&7Preview: ",
                                        "&7" + e.getWhoClicked().getName() + ": " + chosenCosmetic.getChatColor() + "Pick me!",
                                        "",
                                        "&aClick to select")
                        ).getItem());

                        corePlayer.setCoins(corePlayer.getCoins() - 750);
                        currentPage.put(4, new ItemCreator(Material.GOLD_INGOT, 1).setName("&e&lCoins: " + corePlayer.getCoins()).addLore(Collections.singletonList("&7You can use coins to purchase cosmetics, just click on a cosmetic you don't own!")).getItem());

                        updateInventory((Player) e.getWhoClicked());
                        return;
                    }

                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't afford this chat color!"));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.CAT_HISS, 1, 1);
                    return;
                }
                break;
            default:
                if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou disabled all your cosmetics!"));
                    CorePlayer player = CorePlayer.getCorePlayers().get(e.getWhoClicked().getUniqueId());

                    player.getCosmeticsProfile().setCurrentPrefixID(0);
                    player.getCosmeticsProfile().setCurrentSuffixID(0);
                    player.getCosmeticsProfile().setCurrentChatColorID(0);
                    player.getCosmeticsProfile().setCurrentNameColorID(0);

                    e.getWhoClicked().closeInventory();
                    return;
                }
                break;
        }

        if (e.getCurrentItem().getType() == Material.ARROW) {
            setCurrentPage(0);

            updateInventory((Player) e.getWhoClicked());
        }
    }

    @Override public void lastChance(Inventory inventory) { }


    private int getNextFreeSlot(Page page) {
        for (int i = 0; i < 27; i++) {
            if (page.getPageContents().get(i) != null) continue;
            return i;
        }

        return -1;
    }
}