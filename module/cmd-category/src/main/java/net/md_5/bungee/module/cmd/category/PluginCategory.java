package net.md_5.bungee.module.cmd.category;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginCategory extends Plugin
{

    @Override
    public void onEnable()
    {
        getProxy().getPluginManager().registerCommand( this, new CommandCategory() );
    }
}
