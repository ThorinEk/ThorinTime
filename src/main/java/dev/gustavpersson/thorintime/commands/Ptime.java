package dev.gustavpersson.thorintime.commands;

import dev.gustavpersson.thorintime.Permissions;
import dev.gustavpersson.thorintime.ThorinTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ptime implements TabExecutor {

    private final ThorinTime plugin;

    public Ptime(ThorinTime plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("args-missing-message")));
            return false;
        }

        Player target;

        if (args.length == 1) {
            target = player;

            if (!player.hasPermission(Permissions.pTimeSelf)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission-message")).replace("{permission}", Permissions.pTimeSelf));
                return false;
            }
        }
        else if (args.length == 2) {
            if (!player.hasPermission(Permissions.pTimeOthers)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission-message")).replace("{permission}", Permissions.pTimeOthers));
                return false;
            }

            target = Bukkit.getPlayer(args[1]);
             if (target == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("player-not-found-message")).replace("{name}", args[1]));
                return false;
            }
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("too-many-args-message")).replace("{name}", args[1]));
            return false;
        }

        boolean isUsingRelativeTime = false;

        if (args[0].equalsIgnoreCase("day")) {
            long morningTime = 0;
            target.setPlayerTime(morningTime, isUsingRelativeTime);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time-set-message")).replace("{name}", "day"));
        }
        else if (args[0].equalsIgnoreCase("night")) {
            long nightTime = 13000;
            target.setPlayerTime(nightTime, isUsingRelativeTime);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time-set-message")).replace("{name}", "night"));
        }
        else if (args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerTime();
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time-reset-message")));
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("invalid-argument-message")));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Arrays.asList("day", "night", "reset"));
        }
        return Collections.emptyList();
    }
}
