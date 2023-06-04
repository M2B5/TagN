package me.matt.tagn;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static me.matt.tagn.MyListener.fillInventory;

public class CommandStartRound implements CommandExecutor {

    public static Collection<Player> infected = new ArrayList<>();
    private final JavaPlugin plugin;

    public CommandStartRound(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void startRound(JavaPlugin plugin) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        int random = (int) Math.floor(Math.random() * players.size());
        Player firstInfected = (Player) players.toArray()[random];
        infect(firstInfected);
        enterArena(firstInfected, plugin);
    }

    public static void endRound() {
        infected.clear();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team join survivors " + player.getName());
            fillInventory(player, Material.LIME_WOOL);
        }
    }

    public static void infect(Player player) {
        infected.add(player);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team join infected " + player.getName());
        fillInventory(player, Material.RED_WOOL);
    }

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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        endRound();
        startRound(plugin);

        return true;
    }
}
