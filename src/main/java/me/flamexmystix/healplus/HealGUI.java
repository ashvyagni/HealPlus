package me.flamexmystix.healplus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class HealGUI implements Listener {

    public static final String MAIN_TITLE = "Heal Menu";
    public static final String SELECT_TITLE = "Select Player";

    private final HealPlus plugin;

    public HealGUI(HealPlus plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, MAIN_TITLE);

        ItemStack healSelf = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta healSelfMeta = healSelf.getItemMeta();
        healSelfMeta.setDisplayName(ChatColor.GOLD + "Heal Yourself");
        healSelf.setItemMeta(healSelfMeta);
        inventory.setItem(13, healSelf);

        ItemStack healOther = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta healOtherMeta = (SkullMeta) healOther.getItemMeta();
        healOtherMeta.setDisplayName(ChatColor.AQUA + "Heal Another Player");
        healOther.setItemMeta(healOtherMeta);
        inventory.setItem(15, healOther);

        player.openInventory(inventory);
    }

    public void openPlayerSelect(Player player) {
        Collection<? extends Player> online = Bukkit.getOnlinePlayers();
        int size = Math.max(9, (int) Math.ceil(online.size() / 9.0) * 9);
        size = Math.min(size, 54);

        Inventory inventory = Bukkit.createInventory(null, size, SELECT_TITLE);
        int slot = 0;
        for (Player target : online) {
            if (slot >= size) {
                break;
            }
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(target.getName());
            meta.setDisplayName(ChatColor.AQUA + target.getName());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to heal this player.");
            meta.setLore(lore);

            skull.setItemMeta(meta);
            inventory.setItem(slot++, skull);
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if (!MAIN_TITLE.equals(title) && !SELECT_TITLE.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        if (MAIN_TITLE.equals(title)) {
            handleMainMenuClick(player, clicked);
            return;
        }

        if (SELECT_TITLE.equals(title)) {
            handleSelectClick(player, clicked);
        }
    }

    private void handleMainMenuClick(Player player, ItemStack clicked) {
        Material type = clicked.getType();

        if (type == Material.GOLDEN_APPLE) {
            if (!player.hasPermission("healplus.self")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to heal yourself.");
                return;
            }
            HealUtil.healPlayer(player);
            player.sendMessage(ChatColor.GREEN + "You healed yourself.");
            player.closeInventory();
            return;
        }

        if (type == Material.SKULL_ITEM) {
            if (!player.hasPermission("healplus.others")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to heal others.");
                return;
            }
            openPlayerSelect(player);
        }
    }

    private void handleSelectClick(Player player, ItemStack clicked) {
        if (clicked.getType() != Material.SKULL_ITEM) {
            return;
        }

        if (!player.hasPermission("healplus.others")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to heal others.");
            player.closeInventory();
            return;
        }

        SkullMeta meta = (SkullMeta) clicked.getItemMeta();
        if (meta == null || meta.getOwner() == null) {
            player.sendMessage(ChatColor.RED + "Player head is missing data.");
            player.closeInventory();
            return;
        }

        Player target = Bukkit.getPlayerExact(meta.getOwner());
        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is no longer online.");
            player.closeInventory();
            return;
        }

        HealUtil.healPlayer(target);
        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been healed.");
        target.sendMessage(ChatColor.GREEN + "You have been healed.");
        player.closeInventory();
    }
}
