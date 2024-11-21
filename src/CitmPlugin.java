package org.citmb2.citmb;

import org.bukkit.plugin.java.JavaPlugin;

public final class CitmPlugin extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        // Plugin startup logic
        getLogger().info("Hello World!");
        getServer().getPluginManager().registerEvents(new ListenerClass(), this);

        getCommand("citm-admin").setExecutor(new CommandExcute());
        getCommand("citm-uie").setExecutor(new CommandExcute());
    }

    @Override
    public void onDisable()
    {
        //

    }
}

