package net.md_5.bungee.api;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class AbstractReconnectHandler implements ReconnectHandler
{

    @Override
    public CategoryInfo getCategory(ProxiedPlayer player)
    {
        CategoryInfo category = getForcedHost( player.getPendingConnection() );
        if ( category == null )
        {
        	category = getStoredCategory(player);
        	if ( category == null )
        		category = ProxyServer.getInstance().getCategeoryInfo(player.getPendingConnection().getListener().getDefaultCategory() );

            Preconditions.checkState( category != null, "Default category not defined" );
        }

        return category;
    }

    public static CategoryInfo getForcedHost(PendingConnection con)
    {
        if ( con.getVirtualHost() == null )
            return null;

        String forced = con.getListener().getForcedHosts().get( con.getVirtualHost().getHostString() );

        if ( forced == null && con.getListener().isForceDefault() )
        {
            forced = con.getListener().getDefaultCategory();
        }
        return ProxyServer.getInstance().getCategeoryInfo( forced );
    }

    protected abstract CategoryInfo getStoredCategory(ProxiedPlayer player);
}
