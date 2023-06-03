package me.matt.tagn;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static me.matt.tagn.CommandSetTagger.*;
import static me.matt.tagn.Tagn.*;

public class MyListener implements Listener {
    private final JavaPlugin plugin;

    public MyListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void fillInventory(Player player, Material blockType) {
        ItemStack itemStack = new ItemStack(blockType, 64);

        // Fill the main inventory slots
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, itemStack.clone());
        }

        // Clear the offhand slot
        player.getInventory().setItemInOffHand(null);

        // Clear the armor slots
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        // Update the inventory for the player
        player.updateInventory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        sendServerMessage(player, "Welcome to TagN!");

        if (tagger != null) {
            fillInventory(player, Material.LIME_WOOL);

            player.teleport(new Location(player.getWorld(), 0, 85, 0));
            player.setGameMode(GameMode.SURVIVAL);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 1));
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "team join runners " + player.getName());
            player.setGlowing(true);
        } else {
            newTagger(player, null, false, plugin);
        }
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
            CommandSetTagger.newTagger(victim, attacker, true, plugin);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!(player.isOp())) {
            if (event.getBlock().getType() == Material.IRON_BLOCK || event.getBlock().getType() == Material.GLASS || event.getBlock().getType() == Material.CYAN_TERRACOTTA) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        int yMax = 82;
        if (event.getBlock().getLocation().getBlockY() > yMax) {
            if(!event.getPlayer().isOp()) {
                event.setCancelled(true);
                return;
            } else if (event.getBlock().getType() == Material.RED_WOOL || event.getBlock().getType() == Material.LIME_WOOL) {
                event.setCancelled(true);
                return;
            }
        }
        Player player = event.getPlayer();
        Material placedBlock = event.getItemInHand().getType();
        if (placedBlock != Material.AIR) {
            player.getInventory().addItem(new ItemStack(placedBlock, 1));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!(event.getPlayer().isOp())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!(event.getPlayer().isOp())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        selectNewTagger(plugin, player);
    }
}
