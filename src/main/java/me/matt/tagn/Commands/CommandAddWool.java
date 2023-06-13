package me.matt.tagn.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandAddWool implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandAddWool(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("You cannot run this command!");
            return false;
        }

        if (args.length != 2) {
            return false;
        }

        Player player = Bukkit.getServer().getPlayer(args[0]);
        String color = args[1];

        switch (color) {
            case "blue":
                player.addAttachment(plugin, "tagn.wool.blue", true);
                break;
            case "pink":
                player.addAttachment(plugin, "tagn.wool.pink", true);
                break;
            case "white":
                player.addAttachment(plugin, "tagn.wool.white", true);
                break;
        }

        return true;
    }
}
