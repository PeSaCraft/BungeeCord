package net.md_5.bungee.conf;

import com.google.common.base.Preconditions;
import gnu.trove.map.TMap;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import lombok.Getter;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.util.CaseInsensitiveMap;
import net.md_5.bungee.util.CaseInsensitiveSet;

/**
 * Core configuration for the proxy.
 */
@Getter
public class Configuration implements ProxyConfig
{

    /**
     * Time before users are disconnected due to no network activity.
     */
    private int timeout = 30000;
    /**
     * UUID used for metrics.
     */
    private String uuid = UUID.randomUUID().toString();
    /**
     * Set of all listeners.
     */
    private Collection<ListenerInfo> listeners;
    /**
     * Set of all categories.
     */
    private TMap<String, CategoryInfo> categories;
    /**
     * Should we check minecraft.net auth.
     */
    private boolean onlineMode = true;
    /**
     * Whether we log proxy commands to the proxy log
     */
    private boolean logCommands;
    private int playerLimit = -1;
    private Collection<String> disabledCommands;
    private int throttle = 4000;
    private boolean ipForward;
    private Favicon favicon;
    private int compressionThreshold = 256;

    public void load()
    {
        ConfigurationAdapter adapter = ProxyServer.getInstance().getConfigurationAdapter();
        adapter.load();

        File fav = new File( "server-icon.png" );
        if ( fav.exists() )
        {
            try
            {
                favicon = Favicon.create( ImageIO.read( fav ) );
            } catch ( IOException | IllegalArgumentException ex )
            {
                ProxyServer.getInstance().getLogger().log( Level.WARNING, "Could not load server icon", ex );
            }
        }

        listeners = adapter.getListeners();
        timeout = adapter.getInt( "timeout", timeout );
        uuid = adapter.getString( "stats", uuid );
        onlineMode = adapter.getBoolean( "online_mode", onlineMode );
        logCommands = adapter.getBoolean( "log_commands", logCommands );
        playerLimit = adapter.getInt( "player_limit", playerLimit );
        throttle = adapter.getInt( "connection_throttle", throttle );
        ipForward = adapter.getBoolean( "ip_forward", ipForward );
        compressionThreshold = adapter.getInt( "network_compression_threshold", compressionThreshold );

        disabledCommands = new CaseInsensitiveSet( (Collection<String>) adapter.getList( "disabled_commands", Arrays.asList( "disabledcommandhere" ) ) );

        Preconditions.checkArgument( listeners != null && !listeners.isEmpty(), "No listeners defined." );

        Map<String, CategoryInfo> newCategories = adapter.getCategories();
        Preconditions.checkArgument( newCategories != null && !newCategories.isEmpty(), "No servers defined" );

        if ( categories == null )
        {
        	categories = new CaseInsensitiveMap<>( newCategories );
        } else
        {
            for ( CategoryInfo oldCategory : categories.values() )
            {
                // Don't allow servers to be removed
                Preconditions.checkArgument( newCategories.containsKey( oldCategory.getName() ), "Category %s removed on reload!", oldCategory.getName() );
            }

            // Add new servers
            for ( Map.Entry<String, CategoryInfo> newCategory : newCategories.entrySet() )
            {
                if ( !categories.containsValue( newCategory.getValue() ) )
                {
                    categories.put( newCategory.getKey(), newCategory.getValue() );
                }
            }
        }

        for ( ListenerInfo listener : listeners )
        {
            for ( int i = 0; i < listener.getCategoryPriority().size(); i++ )
            {
                String category = listener.getCategoryPriority().get( i );
                Preconditions.checkArgument( categories.containsKey( category ), "Category %s (priority %s) is not defined", category, i );
            }
            for ( String category : listener.getForcedHosts().values() )
                if ( !categories.containsKey( category ) )
                    ProxyServer.getInstance().getLogger().log( Level.WARNING, "Forced host category {0} is not defined", category );
        }
    }

    @Override
    @Deprecated
    public String getFavicon()
    {
        return getFaviconObject().getEncoded();
    }

    @Override
    public Favicon getFaviconObject()
    {
        return favicon;
    }
}
