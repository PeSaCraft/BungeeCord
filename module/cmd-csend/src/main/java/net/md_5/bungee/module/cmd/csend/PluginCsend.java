package net.md_5.bungee.module.cmd.csend;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginCsend extends Plugin
{

    @Override
    public void onEnable()
    {
        getProxy().getPluginManager().registerCommand( this, new CommandCsend() );
    }
}
