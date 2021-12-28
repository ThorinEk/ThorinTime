package dev.gustavpersson.thorintime;

import dev.gustavpersson.thorintime.commands.Ptime;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThorinTime extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("ptime").setExecutor(new Ptime(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
