package me.matt.tagn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CommandSetTagger implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandSetTagger(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public static Player tagger;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Collection<Player> players = player.getWorld().getPlayers();
            int random = (int) (Math.random() * players.size());
            Player tagger = (Player) players.toArray()[random];
            tagger.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                    .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                    .append(Component.text("You are the new tagger!")).color(TextColor.color(0xFFFFFF)));

            Bukkit.getLogger().info("Tagger: " + tagger.getName());

            tagger.setGameMode(GameMode.SPECTATOR);
            tagger.teleport(new Location(tagger.getWorld(), 0, 141, 0));
            new BukkitRunnable() {
                int countdown = 5;

                @Override
                public void run() {
                    if (countdown > 0) {
                        tagger.sendTitle("", ChatColor.RED + String.valueOf(countdown), 0, 20, 0);
                        countdown--;
                    } else {
                        tagger.sendTitle("", ChatColor.GREEN + "GO!", 0, 20, 10);
                        // Teleport the player to a specific location
                        Location teleportLocation = new Location(tagger.getWorld(), 0, 85, 0);
                        tagger.teleport(teleportLocation);
                        tagger.setGameMode(GameMode.SURVIVAL);
                        tagger.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1));
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }

        return true;
    }
}
