package net.md_5.bungee.api.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.Synchronized;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.util.MapUtils;

public class CategoryInfo {

	public static enum CategoryType {
		DIRECT,
		LOBBY_GAME;
	}
	/**
	 * The name of the category that will be displayed.
	 */
	@Getter
	final private String name;
	
	/**
	 * All servers which are part of the category.
	 */
	private Map<ServerInfo, Integer> servers;
	private boolean serversChanged;
	
	private final Collection<ProxiedPlayer> players = new ArrayList<>();
    
	public CategoryInfo(String name) {
		this.name = name;
		this.servers = new LinkedHashMap<ServerInfo, Integer>();
	}
	
	@Synchronized("servers")
    public int removeServer(ServerInfo server) {
		serversChanged = true;
		return this.servers.remove(server);
	}
	
	@Synchronized("servers")
    public void putServer(ServerInfo server) {
		putServer(server, server.getPlayers().size());
	}
	
	@Synchronized("servers")
    public void putServer(ServerInfo server, int players) {
		serversChanged = true;
		this.servers.put(server, players);
	}
	
	@Synchronized("servers")
    public void incrementServer(ServerInfo server) {
		serversChanged = true;
		this.servers.put(server, this.servers.get(server) + 1);
	}
	
	@Synchronized("servers")
    public void decrementServer(ServerInfo server) {
		serversChanged = true;
		this.servers.put(server, this.servers.get(server) - 1);
	}
	
	@Synchronized("servers")
    public ServerInfo getServer() {
		return getServers().iterator().next();
	}
	
	@Synchronized("servers")
    public Set<ServerInfo> getServers() {
		if (serversChanged) {
			this.servers = MapUtils.sortByValue(this.servers);
			serversChanged = false;
		}
		return this.servers.keySet();
	}
	
	public CategoryType getType() {
		return CategoryType.DIRECT;
	}
	
	/**
     * Get the set of all players on this server.
     *
     * @return an unmodifiable collection of all players on this server
     */
	@Synchronized("players")
    public Collection<ProxiedPlayer> getPlayers() {
    	return players;
    }
	

    /**
     * Whether the player can access this category. It will return false when
     * the player has no permission.
     *
     * @param player the player to check access for
     * @return whether access is granted to this server
     */
	public boolean canAccess(CommandSender player) {
		Preconditions.checkNotNull( player, "player" );
		return player.hasPermission( "bungeecord.category." + name );
	}
	
	public String dump() {
		String msg = getName() + " (" +getType() + ")/n";
		msg += "Servers\n";
		for (ServerInfo server : getServers())
			msg += "\t- " + server.getName() + " (" + servers.get(server) + ")\n";
		return msg;
	}
}
