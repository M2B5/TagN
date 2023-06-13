package me.matt.tagn.Commands;

import me.matt.tagn.Tagn;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class CommandWool implements CommandExecutor {
    public final FileConfiguration dataConfig;
    private final File dataFile;
    private final Tagn plugin;

    public CommandWool(Tagn plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "wool.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("wool.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot run this command!");
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /wool <color> e.g. /wool light_blue");
            return false;
        }

        Player player = (Player) sender;
        String color = args[0].toLowerCase();

        if (color.equals("blue") || color.equals("cyan") || color.equals("light_blue")) {
            if (player.hasPermission("tagn.wool.blue")) {
                player.sendMessage("Wool updated!");
                dataConfig.set(player.getUniqueId().toString(), color);
            } else {
                player.sendMessage("You do not have permission to use this color!");
                return false;
            }
        } else if (color.equals("pink") || color.equals("magenta") || color.equals("purple")) {
            if (player.hasPermission("tagn.wool.pink")) {
                player.sendMessage("Wool updated!");
                dataConfig.set(player.getUniqueId().toString(), color);
            } else {
                player.sendMessage("You do not have permission to use this color!");
                return false;
            }
        } else if (color.equals("white") || color.equals("yellow") || color.equals("orange")) {
            if (player.hasPermission("tagn.wool.white")) {
                player.sendMessage("Wool updated!");
                dataConfig.set(player.getUniqueId().toString(), color);
            } else {
                player.sendMessage("You do not have permission to use this color!");
                return false;
            }
        } else {
            dataConfig.set(player.getUniqueId().toString(), "lime");
        }

        try {
            dataConfig.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static Material getWool(Player player) {
        Tagn plugin = Tagn.getInstance();
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "wool.yml"));

        try {
            String color = dataConfig.getString(player.getUniqueId().toString());

            if (Objects.equals(color, "blue")) {
                return Material.BLUE_WOOL;
            }
            if (Objects.equals(color, "cyan")) {
                return Material.CYAN_WOOL;
            }
            if (Objects.equals(color, "light_blue")) {
                return Material.LIGHT_BLUE_WOOL;
            }
            if (Objects.equals(color, "pink")) {
                return Material.PINK_WOOL;
            }
            if (Objects.equals(color, "magenta")) {
                return Material.MAGENTA_WOOL;
            }
            if (Objects.equals(color, "purple")) {
                return Material.PURPLE_WOOL;
            }
            if (Objects.equals(color, "white")) {
                return Material.WHITE_WOOL;
            }
            if (Objects.equals(color, "yellow")) {
                return Material.YELLOW_WOOL;
            }
            if (Objects.equals(color, "orange")) {
                return Material.ORANGE_WOOL;
            }
            if (Objects.equals(color, "lime")) {
                return Material.LIME_WOOL;
            }
            if (color == null) {
                return Material.LIME_WOOL;
            }
            return Material.LIME_WOOL;
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("NullPointerException: " + e.getMessage());
            return Material.LIME_WOOL;
        }
    }
}
