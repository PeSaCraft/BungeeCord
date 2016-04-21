package net.md_5.bungee.api.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Synchronized;
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
	
	private final Collection<ProxiedPlayer> players = new ArrayList<>();
    
	public CategoryInfo(String name) {
		this.name = name;
		this.servers = new LinkedHashMap<ServerInfo, Integer>();
	}
	
	@Synchronized("servers")
    public int removeServer(ServerInfo server) {
		return this.servers.remove(server);
	}
	
	@Synchronized("servers")
    public void putServer(ServerInfo server) {
		this.servers.put(server, server.getPlayers().size());
		this.servers = MapUtils.sortByValue(this.servers);
	}
	
	@Synchronized("servers")
    public ServerInfo getServer() {
		return this.servers.keySet().iterator().next();
	}
	
	@Synchronized("servers")
    public Set<ServerInfo> getServers() {
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
}
