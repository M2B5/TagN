package me.matt.tagn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageCode {
    public static void displayPlayerFace(Player player) {
        String imageUrl = "https://minotar.net/avatar/" + player.getName() + "/8";
        displayImage(player, imageUrl);
    }
    public static void displayPlayerFaceWithText(Player recipient, Player face, Component[] components) {
        String imageUrl = "https://minotar.net/avatar/" + face.getName() + "/8";
        displayImageWithText(recipient, imageUrl, components);
    }
    public static void displayImage(Player player, String imageUrl) {
        int[][] imgArray = convertImageToArray(imageUrl);
        TextColor[][] imgTextColors = convertArrayToTextColors(imgArray);
        for (int y = 0; y < imgTextColors.length; y++) {
            Component line = Component.text().build();
            for (int x = 0; x < imgTextColors[y].length; x++) {
                TextColor textColor = imgTextColors[y][x];
                line = line.append(createColoredSquare(textColor));
            }
            player.sendMessage(line);
        }
    }

    public static void displayImageWithText(Player player, String imageUrl, Component[] components) {
        int[][] imgArray = convertImageToArray(imageUrl);
        if (components.length != imgArray.length) {
            return;
        }
        TextColor[][] imgTextColors = convertArrayToTextColors(imgArray);
        for (int y = 0; y < imgTextColors.length; y++) {
            Component line = Component.text().build();
            for (int x = 0; x < imgTextColors[y].length; x++) {
                TextColor textColor = imgTextColors[y][x];
                line = line.append(createColoredSquare(textColor));
            }
            line = line.append(components[y]);
            player.sendMessage(line);
        }
    }
    private static int[][] convertImageToArray(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            int width = image.getWidth();
            int height = image.getHeight();

            int[][] imageArray = new int[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    imageArray[y][x] = pixel;
                }
            }

            return imageArray;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static TextColor[][] convertArrayToTextColors(int[][] imageArray) {
        int height = imageArray.length;
        int width = imageArray[0].length;

        TextColor[][] textColors = new TextColor[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = imageArray[y][x];

                // Extract color components
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Create a TextColor based on RGB values
                TextColor textColor = TextColor.color(red, green, blue);

                textColors[y][x] = textColor;
            }
        }

        return textColors;
    }

    private static Component createColoredSquare(TextColor textColor) {
        return Component.text()
                .append(Component.text("\u2588").color(textColor))
                .build();
    }
}

