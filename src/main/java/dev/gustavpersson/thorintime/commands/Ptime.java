package dev.gustavpersson.thorintime.commands;

import dev.gustavpersson.thorintime.Chat;
import dev.gustavpersson.thorintime.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ptime implements TabExecutor {

    private final FileConfiguration config;

    public Ptime(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            Chat.sendMessage(player, config.getString("args-missing-message"));
            return false;
        }

        Player target;

        if (args.length == 1) {
            target = player;

            if (!player.hasPermission(Permissions.pTimeSelf)) {
                Chat.sendMessage(player, config.getString("no-permission-message").replace("{permission}", Permissions.pTimeSelf));
                return false;
            }
        }
        else if (args.length == 2) {
            if (!player.hasPermission(Permissions.pTimeOthers)) {
                Chat.sendMessage(player, config.getString("no-permission-message").replace("{permission}", Permissions.pTimeOthers));
                return false;
            }

            target = Bukkit.getPlayer(args[1]);
             if (target == null) {
                Chat.sendMessage(player, config.getString("player-not-found-message").replace("{name}", args[1]));
                return false;
            }
        }
        else {
            Chat.sendMessage(player, config.getString("too-many-args-message").replace("{name}", args[1]));
            return false;
        }

        boolean isUsingRelativeTime = false;

        if (args[0].equalsIgnoreCase("day")) {
            long morningTime = 0;
            target.setPlayerTime(morningTime, isUsingRelativeTime);
            Chat.sendMessage(target, config.getString("time-set-message").replace("{time}", "day"));
            if (target != player) {
                Chat.sendMessage(player, config.getString("time-set-other-message").replace("{time}", "day").replace("{player}", target.getName()));
            }
        }
        else if (args[0].equalsIgnoreCase("night")) {
            long nightTime = 13000;
            target.setPlayerTime(nightTime, isUsingRelativeTime);
            Chat.sendMessage(target, config.getString("time-set-message").replace("{time}", "night"));
            if (target != player) {
                Chat.sendMessage(player, config.getString("time-set-other-message").replace("{time}", "day").replace("{player}", target.getName()));
            }
        }
        else if (args[0].equalsIgnoreCase("reset")) {
            target.resetPlayerTime();
            Chat.sendMessage(target, config.getString("time-reset-message"));
            if (target != player) {
                Chat.sendMessage(player, config.getString("time-reset-other-message").replace("{player}", target.getName()));
            }
        }
        else {
            Chat.sendMessage(player, config.getString("invalid-argument-message"));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return new ArrayList<>(Arrays.asList("day", "night", "reset"));
        }

        if (args.length == 2) {

            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
            Bukkit.getOnlinePlayers().toArray(players);

            for (Player player : players) {
                playerNames.add(player.getName());
            }

            return playerNames;
        }

        return Collections.emptyList();
    }
}
