package net.md_5.bungee.module.reconnect.yaml;

import net.md_5.bungee.api.AbstractReconnectHandler;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.util.CaseInsensitiveMap;
import org.yaml.snakeyaml.Yaml;

public class YamlReconnectHandler extends AbstractReconnectHandler
{

    private final Yaml yaml = new Yaml();
    private final File file = new File( "locations.yml" );
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /*========================================================================*/
    private CaseInsensitiveMap< String> data;

    @SuppressWarnings("unchecked")
    public YamlReconnectHandler()
    {
        try
        {
            file.createNewFile();
            try ( FileReader rd = new FileReader( file ) )
            {
                Map map = yaml.loadAs( rd, Map.class );
                if ( map != null )
                {
                    data = new CaseInsensitiveMap<>( map );
                }
            }
        } catch ( Exception ex )
        {
            file.renameTo( new File( "locations.yml.old" ) );
            ProxyServer.getInstance().getLogger().log( Level.WARNING, "Could not load reconnect locations, resetting them" );
        }

        if ( data == null )
        {
            data = new CaseInsensitiveMap<>();
        }
    }

    @Override
    protected CategoryInfo getStoredCategory(ProxiedPlayer player)
    {
        CategoryInfo category = null;
        lock.readLock().lock();
        try
        {
        	category = ProxyServer.getInstance().getCategeoryInfo( data.get( key( player ) ) );
        } finally
        {
            lock.readLock().unlock();
        }
        return category;
    }

    @Override
    public void setCategory(ProxiedPlayer player)
    {
        lock.writeLock().lock();
        try
        {
            data.put( key( player ), ( player.getReconnectCategory() != null ) ? player.getReconnectCategory().getName() : player.getServer().getInfo().getCategory().getName() );
        } finally
        {
            lock.writeLock().unlock();
        }
    }

    private String key(ProxiedPlayer player)
    {
        InetSocketAddress host = player.getPendingConnection().getVirtualHost();
        return player.getName() + ";" + host.getHostString() + ":" + host.getPort();
    }

    @Override
    public void save()
    {
        Map<String, String> copy = new HashMap<>();
        lock.readLock().lock();
        try
        {
            copy.putAll( data );
        } finally
        {
            lock.readLock().unlock();
        }

        try ( FileWriter wr = new FileWriter( file ) )
        {
            yaml.dump( copy, wr );
        } catch ( IOException ex )
        {
            ProxyServer.getInstance().getLogger().log( Level.WARNING, "Could not save reconnect locations", ex );
        }
    }

    @Override
    public void close()
    {
    }
}
