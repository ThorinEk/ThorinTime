package dev.gustavpersson.thorintime;

import dev.gustavpersson.thorintime.commands.Ptime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThorinTime extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("ThorinTime is initializing");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("ptime").setExecutor(new Ptime(getConfig()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
