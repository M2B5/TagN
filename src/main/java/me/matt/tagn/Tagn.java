package me.matt.tagn;

import org.bukkit.plugin.java.JavaPlugin;

public final class Tagn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("reset").setExecutor(new CommandReset());
        this.getCommand("settagger").setExecutor(new CommandSetTagger(this));
        MyListener listener = new MyListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabled");
    }
}
