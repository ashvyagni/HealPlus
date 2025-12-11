package me.flamexmystix.healplus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HealPlus extends JavaPlugin {

    private HealGUI healGUI;

    @Override
    public void onEnable() {
        this.healGUI = new HealGUI(this);

        HealCommand healCommand = new HealCommand(this, healGUI);
        if (getCommand("heal") != null) {
            getCommand("heal").setExecutor(healCommand);
            getCommand("heal").setTabCompleter(healCommand);
        } else {
            getLogger().severe("Command 'heal' not defined in plugin.yml");
        }

        Bukkit.getPluginManager().registerEvents(healGUI, this);
        getLogger().info("HealPlus enabled.");
    }

    public HealGUI getHealGUI() {
        return healGUI;
    }
}

