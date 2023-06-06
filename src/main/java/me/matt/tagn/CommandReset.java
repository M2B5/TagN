package me.matt.tagn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CommandReset implements CommandExecutor {
    private static final int XZ_MAX = 40;
    private static final int XZ_MIN = -40;
    private static final int Y_MIN = 65;
    private static final int HEIGHT = 15;
    private static final int Y_PILLARS = 40;
    private static final int X_PILLARS = 30;
    private static final int Z_PILLARS = 30;
    private static final int NUM_PARTICLES = 30;
    private final Random random = new Random();

    private int getRandomXZ() {
        return random.nextInt(2 * XZ_MAX + 1) - XZ_MAX;
    }

    private int getRandomY() {
        return random.nextInt(HEIGHT) + Y_MIN;
    }

    private void generatePillarsX(World world) {
        for (int i = 0; i < X_PILLARS; i++) {
            int y = getRandomY();
            int z = getRandomXZ();

            for (int x = XZ_MIN; x <= XZ_MAX; x++) {
                world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
            }
        }
    }

    private void generatePillarsY(World world) {
        for (int i = 0; i < Y_PILLARS; i++) {
            int x = getRandomXZ();
            int z = getRandomXZ();

            for (int y = Y_MIN; y <= Y_MIN + HEIGHT; y++) {
                world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
            }
        }
    }

    private void generatePillarsZ(World world) {
        for (int i = 0; i < Z_PILLARS; i++) {
            int x = getRandomXZ();
            int y = getRandomY();

            for (int z = XZ_MIN; z <= XZ_MAX; z++) {
                world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
            }
        }
    }

    private void generateParticles(World world) {
        for (int i = 0; i < NUM_PARTICLES; i++) {
            int xp = getRandomXZ();
            int zp = getRandomXZ();
            int xv = random.nextInt(2) + 1;
            int zv = 3 - xv;
            xv *= random.nextInt(2) * 2 - 1;
            zv *= random.nextInt(2) * 2 - 1;

            for (int y = Y_MIN - 1; y <= Y_MIN + HEIGHT - 2; y++) {
                int x2 = xp - xv;
                int z2 = zp - zv;

                if (x2 >= XZ_MAX) {
                    x2 = XZ_MAX;
                    xv *= -1;
                    fillArea(world, xp, y, zp, x2, y + 1, z2, Material.GRAY_WOOL);
                    x2 = xp;
                } else if (x2 <= XZ_MIN) {
                    x2 = XZ_MIN;
                    xv *= -1;
                    fillArea(world, xp, y, zp, x2, y + 1, z2, Material.GRAY_WOOL);
                    x2 = xp;
                }

                if (z2 >= XZ_MAX) {
                    z2 = XZ_MAX;
                    zv *= -1;
                    fillArea(world, xp, y, zp, x2, y + 1, z2, Material.GRAY_WOOL);
                    z2 = zp;
                } else if (z2 <= XZ_MIN) {
                    z2 = XZ_MIN;
                    zv *= -1;
                    fillArea(world, xp, y, zp, x2, y + 1, z2, Material.GRAY_WOOL);
                    z2 = zp;
                }

                fillArea(world, xp, y, zp, x2, y + 1, z2, Material.GRAY_WOOL);
                xp = x2;
                zp = z2;
            }
        }
    }

    private void generateBorders(World world) {
        fillArea(world, XZ_MIN, Y_MIN - 1, XZ_MIN, XZ_MAX, Y_MIN - 1, XZ_MAX, Material.CYAN_TERRACOTTA);
        fillArea(world, XZ_MIN - 1, Y_MIN - 1, XZ_MIN - 1, XZ_MAX + 1, Y_MIN + HEIGHT + 1, XZ_MIN - 1, Material.IRON_BLOCK);
        fillArea(world, XZ_MIN - 1, Y_MIN - 1, XZ_MAX + 1, XZ_MAX + 1, Y_MIN + HEIGHT + 1, XZ_MAX + 1, Material.IRON_BLOCK);
        fillArea(world, XZ_MIN - 1, Y_MIN - 1, XZ_MIN - 1, XZ_MIN - 1, Y_MIN + HEIGHT + 1, XZ_MAX + 1, Material.IRON_BLOCK);
        fillArea(world, XZ_MAX + 1, Y_MIN - 1, XZ_MIN - 1, XZ_MAX + 1, Y_MIN + HEIGHT + 1, XZ_MAX + 1, Material.IRON_BLOCK);
        fillArea(world, XZ_MIN - 1, Y_MIN + HEIGHT + 2, XZ_MIN - 1, XZ_MAX + 1, Y_MIN + HEIGHT + 10, XZ_MIN - 1, Material.BARRIER);
        fillArea(world, XZ_MIN - 1, Y_MIN + HEIGHT + 2, XZ_MAX + 1, XZ_MAX + 1, Y_MIN + HEIGHT + 10, XZ_MAX + 1, Material.BARRIER);
        fillArea(world, XZ_MIN - 1, Y_MIN + HEIGHT + 2, XZ_MIN - 1, XZ_MIN - 1, Y_MIN + HEIGHT + 10, XZ_MAX + 1, Material.BARRIER);
        fillArea(world, XZ_MAX + 1, Y_MIN + HEIGHT + 2, XZ_MIN - 1, XZ_MAX + 1, Y_MIN + HEIGHT + 10, XZ_MAX + 1, Material.BARRIER);
        fillArea(world, XZ_MAX + 1, Y_MIN + HEIGHT + 10, XZ_MAX + 1, XZ_MIN - 1, Y_MIN + HEIGHT + 10, XZ_MIN - 1, Material.BARRIER);
    }

    private void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material material) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        World world = Bukkit.getWorld("world");

        // Clear the area
        fillArea(world, XZ_MIN, Y_MIN, XZ_MIN, XZ_MAX, Y_MIN + HEIGHT + 3, XZ_MAX, Material.AIR);

        generatePillarsX(world);
        generatePillarsY(world);
        generatePillarsZ(world);
        generateParticles(world);
        generateBorders(world);

        return true;
    }
}
