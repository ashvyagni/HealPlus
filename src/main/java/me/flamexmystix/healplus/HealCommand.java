package me.flamexmystix.healplus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor, TabCompleter {

    private final HealPlus plugin;
    private final HealGUI healGUI;

    public HealCommand(HealPlus plugin, HealGUI healGUI) {
        this.plugin = plugin;
        this.healGUI = healGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            handleSelfHeal(sender);
            return true;
        }

        if ("gui".equalsIgnoreCase(args[0])) {
            handleGuiCommand(sender);
            return true;
        }

        handleTargetHeal(sender, args[0]);
        return true;
    }

    private void handleSelfHeal(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console must specify a player to heal.");
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("healplus.self")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to heal yourself.");
            return;
        }

        HealUtil.healPlayer(player);
        player.sendMessage(ChatColor.GREEN + "You healed yourself.");
    }

    private void handleGuiCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot use the heal GUI.");
            return;
        }

        Player player = (Player) sender;
        boolean hasSelf = player.hasPermission("healplus.self");
        boolean hasOthers = player.hasPermission("healplus.others");

        if (!hasSelf && !hasOthers) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use the heal GUI.");
            return;
        }

        healGUI.openMainMenu(player);
    }

    private void handleTargetHeal(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + targetName + "' not found.");
            return;
        }

        boolean selfTarget = sender instanceof Player && ((Player) sender).getUniqueId().equals(target.getUniqueId());
        String permission = selfTarget ? "healplus.self" : "healplus.others";

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to heal " + (selfTarget ? "yourself." : "others."));
            return;
        }

        HealUtil.healPlayer(target);
        target.sendMessage(ChatColor.GREEN + "You have been healed.");
        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been healed.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if (sender instanceof Player && (sender.hasPermission("healplus.self") || sender.hasPermission("healplus.others"))) {
                suggestions.add("gui");
            }

            if (sender.hasPermission("healplus.others")) {
                suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList()));
            }

            return suggestions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}
