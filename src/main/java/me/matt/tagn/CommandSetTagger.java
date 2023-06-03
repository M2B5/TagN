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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CommandSetTagger implements CommandExecutor {
    private final JavaPlugin plugin;
    public static Player tagger;
    private static BukkitTask timerTask;

    public CommandSetTagger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void newTagger(Player nTagger, Player oTagger, Boolean didTag, JavaPlugin plugin) {
        if (oTagger != null) {
            if (didTag) {
                oTagger.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                        .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                        .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                        .append(Component.text("Success! You are no longer the tagger!")).color(TextColor.color(0xFFFFFF)));
            } else {
                oTagger.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                        .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                        .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                        .append(Component.text("You are no longer the tagger!")).color(TextColor.color(0xFFFFFF)));
            }
        }

        nTagger.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                .append(Component.text("You are the new tagger!")).color(TextColor.color(0xFFFFFF)));

        Bukkit.getLogger().info("Tagger: " + nTagger.getName());

        nTagger.setGameMode(GameMode.SPECTATOR);
        nTagger.teleport(new Location(nTagger.getWorld(), 0, 141, 0));

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        timerTask = new BukkitRunnable() {
            int countdown = 120; // 2 minutes in seconds

            @Override
            public void run() {
                if (countdown > 0) {
                    countdown--;
                } else {
                    // Select a new tagger if the current tagger hasn't tagged anyone
                    if (tagger == nTagger) {
                        selectNewTagger();
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private static void selectNewTagger() {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        int randomIndex = (int) (Math.random() * players.size());
        tagger = (Player) players.toArray()[randomIndex];
        newTagger(tagger, null, null, null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Collection<? extends Player> players = player.getWorld().getPlayers();
            int randomIndex = (int) (Math.random() * players.size());
            tagger = (Player) players.toArray()[randomIndex];
            tagger.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                    .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                    .append(Component.text("You are the new tagger!")).color(TextColor.color(0xFFFFFF)));

            Bukkit.getLogger().info("Tagger: " + tagger.getName());

            newTagger(tagger, null, null, plugin);
        }

        return true;
    }
}
