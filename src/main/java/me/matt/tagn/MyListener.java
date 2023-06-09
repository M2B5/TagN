package me.matt.tagn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.matt.tagn.Commands.CommandStartRound.*;
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

        Component[] components = new Component[8];
        components[0] = Component.text("");
        components[1] = Component.text()
                .append(Component.text(" Welcome to ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("TagN!")
                        .color(TextColor.color(0x95DFDA))
                        .decoration(TextDecoration.BOLD, true))
                .build();
        components[2] = Component.text("");
        components[3] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("Players glowing ")
                        .color(NamedTextColor.WHITE))
                .append(Component.text("red ")
                        .color(NamedTextColor.RED))
                .append(Component.text("are infected!")
                        .color(NamedTextColor.WHITE))
                .build();
        components[4] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("Players glowing ")
                        .color(NamedTextColor.WHITE))
                .append(Component.text("green ")
                        .color(NamedTextColor.GREEN))
                .append(Component.text("are survivors!")
                        .color(NamedTextColor.WHITE))
                .build();
        components[5] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("Infected should tag survivors.")
                        .color(NamedTextColor.WHITE))
                .build();
        components[6] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("Survivors should run from infected.")
                        .color(NamedTextColor.WHITE))
                .build();
        components[7] = Component.text()
                .append(Component.text(" ⋙ ")
                        .color(NamedTextColor.GRAY))
                .append(Component.text("/discord ")
                        .color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("to join our Discord!")
                        .color(NamedTextColor.WHITE))
                .build();
        ImageCode.displayPlayerFaceWithText(player, player, components);

        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1));

        infect(player);
        enterArena(player, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return; // Ignore if the damage is not caused by a player or the target is not a player
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (infected.contains(attacker) && !infected.contains(victim)) {
            serverBroadcast(victim.getName() + " has been infected by " + attacker.getName() + "!");
            infect(victim);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp()) {
            Material blockType = event.getBlock().getType();
            if (blockType == Material.IRON_BLOCK || blockType == Material.GLASS || blockType == Material.CYAN_TERRACOTTA) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        int yMax = 82;

        Player player = event.getPlayer();
        Material placedBlock = event.getItemInHand().getType();

        if (event.getBlock().getLocation().getBlockY() > yMax) {
            if (placedBlock == Material.LIME_WOOL || placedBlock == Material.RED_WOOL || !player.isOp()) {
                event.setCancelled(true);
                return;
            }
        }

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
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        if (message.contains("https://") || message.contains("http://") || message.contains("www.") || message.contains(".com")) {
            // Do something when a link is detected
            if (event.getPlayer().isOp()) {
                //loop all players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ImageCode.displayImage(player, message);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        Player player = event.getPlayer();
        if (infected.contains(player)) {
            infected.remove(player);
        }
        newRoundTest(plugin);
    }

    public static void newRoundTest(JavaPlugin plugin) {
        if (infected.size() == 0) {
            serverBroadcast("Infected player left the game, starting new round.");
            endRound();
            startRound(plugin);
            return;
        }

        boolean allInfected = true;
        int survivorCount = 0;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!infected.contains(p)) {
                allInfected = false;
                survivorCount++;
            }
        }
        if (allInfected) {
            serverBroadcast("All survivors have been infected, starting new round.");
            endRound();
            Bukkit.getScheduler().runTaskLater(plugin, () -> startRound(plugin), 5L);
        } else if (survivorCount == 1) {
            serverBroadcast("The last survivor left the game, starting new round.");
            endRound();
            Bukkit.getScheduler().runTaskLater(plugin, () -> startRound(plugin), 5L);
        }
    }
}
