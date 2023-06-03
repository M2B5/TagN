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
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static me.matt.tagn.MyListener.fillInventory;
import static me.matt.tagn.Tagn.sendServerMessage;

public class CommandSetTagger implements CommandExecutor {
    private final JavaPlugin plugin;
    public static Player tagger;
    private static BukkitTask timerTask;

    public CommandSetTagger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void newTagger(Player nTagger, Player oTagger, Boolean didTag, JavaPlugin plugin) {

        tagger = nTagger;

        fillInventory(nTagger, Material.RED_WOOL);

        if (oTagger != null) {
            fillInventory(oTagger, Material.LIME_WOOL);
            if (didTag) {
                sendServerMessage(oTagger, "Success! You are no longer the tagger!");
                sendServerMessage(nTagger, "You were tagged! Now you are the tagger!");
            } else {
                sendServerMessage(oTagger, "Took too long! New tagger selected!");
                sendServerMessage(nTagger, "Tagger ran out of time! You are the new tagger!");
            }
        } else {
            sendServerMessage(nTagger, "You have been randomly selected as tagger!");
        }

        Bukkit.getLogger().info("Tagger: " + nTagger.getName());

        nTagger.setGameMode(GameMode.SPECTATOR);
        nTagger.teleport(new Location(nTagger.getWorld(), 0, 141, 0));


        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown > 0) {
                    nTagger.sendTitle("", ChatColor.RED + String.valueOf(countdown), 0, 20, 0);
                    countdown--;
                } else {
                    nTagger.sendTitle("", ChatColor.GREEN + "GO!", 0, 20, 10);
                    // Teleport the player to a specific location
                    Location teleportLocation = new Location(nTagger.getWorld(), 0, 85, 0);
                    nTagger.teleport(teleportLocation);
                    nTagger.setGameMode(GameMode.SURVIVAL);
                    nTagger.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1));
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        timerTask = new BukkitRunnable() {
            int countdown = 120; // 2 minutes in seconds

            @Override
            public void run() {
                if (countdown > 0) {
                    countdown--;
                } else {
                    // Select a new tagger if the current tagger hasn't tagged anyone
                    if (tagger == nTagger) {
                        selectNewTagger(plugin, nTagger);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private static void selectNewTagger(JavaPlugin plugin, Player player) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        if (players.size() == 1) {
            return;
        }
        int randomIndex = (int) (Math.random() * players.size());
        Player p = (Player) players.toArray()[randomIndex];
        if (p == player) {
            selectNewTagger(plugin, player);
            return;
        }
        newTagger(p, player, false, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Collection<? extends Player> players = player.getWorld().getPlayers();
            int randomIndex = (int) (Math.random() * players.size());
            tagger = (Player) players.toArray()[randomIndex];

            Bukkit.getLogger().info("Tagger: " + tagger.getName());

            newTagger(tagger, null, null, plugin);
        }

        return true;
    }
}
