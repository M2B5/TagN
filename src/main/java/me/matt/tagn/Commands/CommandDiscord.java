package me.matt.tagn.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDiscord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Component component = Component.text()
                .append(Component.text("Click here to join the discord!")
                        .color(TextColor.color(0xFFFFFF))
                        .decoration(TextDecoration.BOLD, true)
                        .clickEvent(ClickEvent.openUrl("https://discord.gg/Rqhk5uPUxZ")))
                .build();
        sender.sendMessage(component);
        return true;
    }
}
