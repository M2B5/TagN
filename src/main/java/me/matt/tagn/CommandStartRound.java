package me.matt.tagn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static me.matt.tagn.MyListener.fillInventory;
import static me.matt.tagn.Tagn.serverBroadcast;

public class CommandStartRound implements CommandExecutor {

    public static Collection<Player> infected = new ArrayList<>();
    private final JavaPlugin plugin;

    public static BukkitTask gameTimerTask;

    public CommandStartRound(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void startRound(JavaPlugin plugin) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        int random = (int) Math.floor(Math.random() * players.size());
        Player firstInfected = (Player) players.toArray()[random];
        serverBroadcast("New round starting...");
        Component[] components = new Component[8];
        components[0] = Component.text("");
        components[1] = Component.text()
                .append(Component.text(" " + firstInfected.getName())
                        .color(TextColor.color(0xE9114E))
                        .decoration(TextDecoration.BOLD, true))
                .build();
        components[2] = Component.text("");
        components[3] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("They are the first infected!")
                        .color(TextColor.color(0xFFFFFF)))
                .build();
        components[4] = Component.text("");
        components[5] = Component.text("");
        components[6] = Component.text("");
        components[7] = Component.text("");
        for (Player player : players) {
            ImageCode.displayPlayerFaceWithText(player, firstInfected, components);
        }
        infect(firstInfected);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "reset");
        enterArena(firstInfected, plugin);
        if (gameTimerTask != null) {
            gameTimerTask.cancel();
        }
        gameTimerTask = gameTimer(plugin);
    }

    public static void endRound() {
        infected.clear();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team join survivors " + player.getName());
            fillInventory(player, CommandWool.getWool(player));
        }
    }

    public static void infect(Player player) {
        infected.add(player);

        // Check if all players are infected
        int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        boolean allInfected = infected.size() == onlinePlayers;

        if (allInfected && !(onlinePlayers < 2)) {
            serverBroadcast("All players are infected!");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "start");
            return;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team join infected " + player.getName());
        fillInventory(player, Material.RED_WOOL);
    }

    //teleport player into arena with brief slow falling effect
    public static void enterArena(Player player, JavaPlugin plugin) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(player.getWorld(), 0, 141, 0));
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown > 0) {
                    player.sendTitle("", ChatColor.RED + String.valueOf(countdown), 0, 20, 0);
                    countdown--;
                } else {
                    player.sendTitle("", ChatColor.GREEN + "GO!", 0, 20, 10);
                    // Teleport the player to a specific location
                    Location teleportLocation = new Location(player.getWorld(), 0, 85, 0);
                    player.teleport(teleportLocation);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1));
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public static BukkitTask gameTimer(JavaPlugin plugin) {
        return new BukkitRunnable() {
            private int secondsPassed = 0;
            private int gameLength = 300;
            private boolean roundEnded = false;

            @Override
            public void run() {
                if (!roundEnded) {
                    if (infected.size() <= 1) {
                        if (secondsPassed >= 60) {
                            serverBroadcast("Time's up! Survivors win!");
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "start");
                            roundEnded = true;
                        }
                    } else {
                        Player firstInfected = infected.iterator().next();
                        if (firstInfected != null && !firstInfected.isDead()) {
                            boolean hasInfectedPlayer = false;
                            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                if (player != firstInfected && !infected.contains(player)) {
                                    hasInfectedPlayer = true;
                                    break;
                                }
                            }
                            if (!hasInfectedPlayer) {
                                serverBroadcast("First infected took too long! Starting a new round...");
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "start");
                                roundEnded = true;
                            }
                        }
                    }
                }

                if (secondsPassed >= gameLength) {
                    serverBroadcast("Time's up! Survivors win!");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "start");
                    roundEnded = true;
                }

                secondsPassed++;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        endRound();
        startRound(plugin);

        return true;
    }
}
