package me.matt.tagn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tagn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("reset").setExecutor(new CommandReset());
        this.getCommand("start").setExecutor(new CommandStartRound(this));
        MyListener listener = new MyListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        getLogger().info("Enabled");

        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team add infected");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team add survivors");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team modify infected color red");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team modify survivors color green");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabled");
    }

    public static void sendServerMessage(Player player, String string) {
        player.sendMessage(
                Component.text()
                        .append(Component.text("[")
                                .color(TextColor.color(0xAAAAAA)))
                        .append(Component.text("TagN")
                                .color(TextColor.color(0xE9114E))
                                .decoration(TextDecoration.BOLD, true))
                        .append(Component.text("] - ")
                                .color(TextColor.color(0xAAAAAA)))
                        .append(Component.text(string)
                                .color(TextColor.color(0xFFFFFF)))
                        .build()
        );
    }

    public static void serverBroadcast(String string) {
        Bukkit.getServer().broadcast(Component.text()
                .append(Component.text("[")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("TagN")
                        .color(TextColor.color(0xE9114E))
                        .decoration(TextDecoration.BOLD, true))
                .append(Component.text("] - ")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text(string)
                        .color(TextColor.color(0xFFFFFF)))
                .build());
    }
}
