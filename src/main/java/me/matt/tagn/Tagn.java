package me.matt.tagn;

import me.matt.tagn.Commands.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class Tagn extends JavaPlugin {
    private static Tagn instance;

    @Override
    public void onEnable() {
        instance = this;

        registerCommands();
        registerListeners();

        getLogger().info("Enabled");

        setupTeams();

        startMessages();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    private void registerCommands() {
        getCommand("reset").setExecutor(new CommandReset());
        getCommand("start").setExecutor(new CommandStartRound(this));
        getCommand("addwool").setExecutor(new CommandAddWool(this));
        getCommand("wool").setExecutor(new CommandWool(this));
        getCommand("discord").setExecutor(new CommandDiscord());
    }

    private void registerListeners() {
        MyListener listener = new MyListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void setupTeams() {
        executeConsoleCommand("team add infected");
        executeConsoleCommand("team add survivors");
        executeConsoleCommand("team modify infected color red");
        executeConsoleCommand("team modify survivors color green");
    }

    private void executeConsoleCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }

    public static void sendServerMessage(Player player, String message) {
        player.sendMessage(buildTagNMessage(message));
    }

    public static void serverBroadcast(String message) {
        Bukkit.getServer().broadcast(buildTagNMessage(message));
    }

    private static Component buildTagNMessage(String message) {
        return Component.text()
                .append(Component.text("[")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("TagN")
                        .color(TextColor.color(0x95DFDA))
                        .decoration(TextDecoration.BOLD, true))
                .append(Component.text("] - ")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text(message)
                        .color(TextColor.color(0x95DFDA)))
                .build();
    }

    public static Tagn getInstance() {
        return instance;
    }

    public void startMessages() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                serverBroadcast(ServerMessage.getString());
            }
        };
        runnable.runTaskTimerAsynchronously(this, 0, 1200);
    }

}
