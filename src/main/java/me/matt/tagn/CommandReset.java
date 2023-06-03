package me.matt.tagn;

import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.lang.Math;


public class CommandReset implements CommandExecutor {
    int xzMax = 40;
    int xzMin = -40;
    int yMin = 65;
    int height = 25;
    int yPillars = 25;
    int xPillars = 15;
    int zPillars = 15;
    int numParticles = 6;

    public int xzRand() {
        int r = (int) Math.floor(Math.random() * (2 * xzMax + 1)) - xzMax;
        return r;
    }

    public int yRand(){
        int r = (int) Math.floor(Math.random() * height) + yMin;
        return r;
    }

    public void fillArea(int x1, int y1, int z1, int x2, int y2, int z2, World w, Material m){
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    w.getBlockAt(x, y, z).setType(m);
                }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();

            fillArea(xzMin, yMin, xzMin, xzMax, yMin + height, xzMax, world, Material.AIR);

            for (int i = 0; i < xPillars; i++) {
                int y = yRand();
                int z = xzRand();
                for (int x = xzMin; x <= xzMax; x++) {
                    world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
                }
            }

            for (int i = 0; i < yPillars; i++) {
                int x = xzRand();
                int z = xzRand();
                for (int y = yMin; y <= yMin + height; y++) {
                    world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
                }
            }

            for (int i = 0; i < zPillars; i++) {
                int x = xzRand();
                int y = yRand();
                for (int z = xzMin; z <= xzMax; z++) {
                    world.getBlockAt(x, y, z).setType(Material.GRAY_WOOL);
                }
            }

            for (int i = 0; i < numParticles; i++) {
                int xp = xzRand();
                int zp = xzRand();
                int xv = (int) Math.floor(Math.random()*2) + 1;
                int zv = 3 - xv;
                xv *= (int) Math.floor(Math.random()*2)*2 - 1;
                zv *= (int) Math.floor(Math.random()*2)*2 - 1;
                for (int y = yMin - 1; y <= yMin + height - 2; y++) {
                    int x2 = xp - xv;
                    int z2 = zp - zv;
                    if (x2 >= xzMax) {
                        x2 = xzMax;
                        xv *= -1;
                        fillArea(xp, y, zp, x2, y + 1, z2, world, Material.GRAY_WOOL);
                        x2 = xp;
                    } else if (x2 <= xzMin) {
                        x2 = xzMin;
                        xv *= -1;
                        fillArea(xp, y, zp, x2, y + 1, z2, world, Material.GRAY_WOOL);
                        x2 = xp;
                    }
                    if (z2 >= xzMax) {
                        z2 = xzMax;
                        zv *= -1;
                        fillArea(xp, y, zp, x2, y + 1, z2, world, Material.GRAY_WOOL);
                        z2 = zp;
                    } else if (z2 <= xzMin) {
                        z2 = xzMin;
                        zv *= -1;
                        fillArea(xp, y, zp, x2, y + 1, z2, world, Material.GRAY_WOOL);
                        z2 = zp;
                    }

                    fillArea(xp, y, zp, x2, y + 1, z2, world, Material.GRAY_WOOL);
                    xp = x2;
                    zp = z2;
                }
            }

            fillArea(xzMin, yMin - 1, xzMin, xzMax, yMin - 1, xzMax, world, Material.CYAN_TERRACOTTA);

        }

        return true;
    }
}
