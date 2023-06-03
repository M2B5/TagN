package me.matt.tagn;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
public class MyListener implements Listener {
    private final JavaPlugin plugin;

    public MyListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void fillInventory(Player player, Material blockType) {
        player.getInventory().clear();
        ItemStack block = new ItemStack(blockType);
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, block);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        final TextComponent textComponent = Component.text("[").color(TextColor.color(0xAAAAAA))
                .append(Component.text("TagN").color(TextColor.color(0xE9114E)).decoration(TextDecoration.BOLD, true))
                .append(Component.text("] - ").color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Welcome to TagN!")).color(TextColor.color(0xFFFFFF));

        player.sendMessage(textComponent);

        fillInventory(player, Material.LIME_WOOL);
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
            if (event.getBlock().getType() == Material.IRON_BLOCK || event.getBlock().getType() == Material.GLASS) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!(player.isOp())) {
            ItemStack placedBlock = event.getItemInHand();
            if (placedBlock.getType() != Material.AIR) {
                player.getInventory().addItem(placedBlock);
            }
        }
    }
}
