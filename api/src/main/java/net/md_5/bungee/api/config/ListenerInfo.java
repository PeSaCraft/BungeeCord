package net.md_5.bungee.api.config;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * Class representing the configuration of a server listener. Used for allowing
 * multiple listeners on different ports.
 */
@Data
public class ListenerInfo
{

    /**
     * Host to bind to.
     */
    private final InetSocketAddress host;
    /**
     * Displayed MOTD.
     */
    private final String motd;
    /**
     * Max amount of slots displayed on the ping page.
     */
    private final int maxPlayers;
    /**
     * Number of players to be shown on the tab list.
     */
    private final int tabListSize;
    /**
     * List of categories in order of join attempt. First attempt is the first
     * element, second attempt is the next element, etc etc.
     */
    private final List<String> categoryPriority;
    /**
     * Whether reconnect locations will be used, or else the user is simply
     * transferred to the default server on connect.
     */
    private final boolean forceDefault;
    /**
     * A list of host to category name mappings which will force a user to be
     * transferred depending on the host they connect to.
     */
    private final Map<String, String> forcedHosts;
    /**
     * The type of tab list to use
     */
    private final String tabListType;
    /**
     * Whether to set the local address when connecting to servers.
     */
    private final boolean setLocalAddress;
    /**
     * Whether to pass the ping through when we can reliably get the target
     * server (force default server).
     */
    private final boolean pingPassthrough;
    /**
     * What port to run udp query on.
     */
    private final int queryPort;
    /**
     * Whether to enable udp query.
     */
    private final boolean queryEnabled;

    /**
     * Gets the highest priority category to join.
     *
     * @return default category
     * @deprecated replaced by {@link #categoryPriority}
     */
    @Deprecated
    public String getDefaultCategory()
    {
        return categoryPriority.get( 0 );
    }

    /**
     * Gets the second highest priority category to join, or else the highest
     * priority.
     *
     * @return fallback category
     * @deprecated replaced by {@link #categoryPriority}
     */
    @Deprecated
    public String getFallbackCategory()
    {
        return ( categoryPriority.size() > 1 ) ? categoryPriority.get( 1 ) : getDefaultCategory();
    }
}
