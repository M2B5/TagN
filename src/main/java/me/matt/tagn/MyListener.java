package me.matt.tagn;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
public class MyListener implements Listener {
    private final JavaPlugin plugin;

    public MyListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        final TextComponent textComponent = Component.text("[").color(TextColor.color(0xAAAAAA))
                .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Welcome to TagN!")).color(TextColor.color(0xFFFFFF));

        player.sendMessage(textComponent);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return; // Ignore if the damage is not caused by a player
        }

        if (!(event.getEntity() instanceof Player)) {
            return; // Ignore if the target is not a player
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (attacker == CommandSetTagger.tagger) {
            CommandSetTagger.tagger = victim;

            victim.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                    .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                    .append(Component.text("You are the new tagger!")).color(TextColor.color(0xFFFFFF)));

            attacker.sendMessage(Component.text("[").color(TextColor.color(0xAAAAAA))
                    .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                    .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                    .append(Component.text("Success! You are no longer the tagger!")).color(TextColor.color(0xFFFFFF)));

            victim.setGameMode(GameMode.SPECTATOR);
            victim.teleport(new Location(victim.getWorld(), 0.5, 141.5, 0.5));

            new BukkitRunnable() {
                int countdown = 5;

                @Override
                public void run() {
                    if (countdown > 0) {
                        victim.sendTitle("", ChatColor.RED + String.valueOf(countdown), 0, 20, 0);
                        countdown--;
                    } else {
                        victim.sendTitle("", ChatColor.GREEN + "GO!", 0, 20, 10);
                        // Teleport the player to a specific location
                        Location teleportLocation = new Location(victim.getWorld(), 0, 85, 0);
                        victim.teleport(teleportLocation);
                        victim.setGameMode(GameMode.SURVIVAL);
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1));
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }
}
