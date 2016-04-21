package net.md_5.bungee.module.cmd.category;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Map;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Command to list and switch a player between available categories.
 */
public class CommandCategory extends Command implements TabExecutor
{

    public CommandCategory()
    {
        super( "category", "bungeecord.command.category" );
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if ( !( sender instanceof ProxiedPlayer ) )
            return;
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Map<String, CategoryInfo> categories = ProxyServer.getInstance().getCategories();
        if ( args.length == 0 )
        {
            player.sendMessage( ProxyServer.getInstance().getTranslation( "current_category", player.getServer().getInfo().getCategory().getName() ) );
            TextComponent categoryList = new TextComponent( ProxyServer.getInstance().getTranslation( "category_list" ) );
            categoryList.setColor( ChatColor.GOLD );
            boolean first = true;
            for ( CategoryInfo category : categories.values() )
            {
                if ( category.canAccess( player ) )
                {
                    TextComponent categoryTextComponent = new TextComponent( first ? category.getName() : ", " + category.getName() );
                    int count = category.getPlayers().size();
                    categoryTextComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder( count + ( count == 1 ? " player" : " players" ) + "\n" )
                            .append( "Click to connect to the category" ).italic( true )
                            .create() ) );
                    categoryTextComponent.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/category " + category.getName() ) );
                    categoryList.addExtra( categoryTextComponent );
                    first = false;
                }
            }
            player.sendMessage( categoryList );
        } else
        {
            CategoryInfo category = categories.get( args[0] );
            if ( category == null )
                player.sendMessage( ProxyServer.getInstance().getTranslation( "no_category" ) );
            else if ( !category.canAccess( player ) )
                player.sendMessage( ProxyServer.getInstance().getTranslation( "no_category_permission" ) );
            else
                player.connectToCategory( category );
        }
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender sender, final String[] args)
    {
        return ( args.length > 1 ) ? Collections.EMPTY_LIST : Iterables.transform( Iterables.filter( ProxyServer.getInstance().getServers().values(), new Predicate<ServerInfo>()
        {
            private final String lower = ( args.length == 0 ) ? "" : args[0].toLowerCase();

            @Override
            public boolean apply(ServerInfo input)
            {
                return input.getName().toLowerCase().startsWith( lower ) && input.canAccess( sender );
            }
        } ), new Function<ServerInfo, String>()
        {
            @Override
            public String apply(ServerInfo input)
            {
                return input.getName();
            }
        } );
    }
}
