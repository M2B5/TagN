package me.matt.tagn;

import net.kyori.adventure.text.Component;

public class ServerMessage {
    private static String[] strings = {
            "Use /discord to join the discord!",
            "Visit our shop using /buy to help support the server!",
            "Want to help the server grow? Invite your friends or /ad in the minehut lobby!"
    };


    public static String getString() {
        //return a random component
        int i = (int) (Math.random() * strings.length);
        return strings[i];
    }
}
