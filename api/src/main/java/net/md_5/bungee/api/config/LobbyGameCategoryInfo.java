package net.md_5.bungee.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.md_5.bungee.util.MapUtils;

public class LobbyGameCategoryInfo extends CategoryInfo {

	/**
	 * The map contains all game servers of this category
	 * The key specifies the games map while the value is
	 * a map of the servers to their current players. The
	 * null key is used for free game servers.
	 */
	final private Map<String, Map<ServerInfo, Integer>> waitingGameServers;
	
	/**
	 * A list containing all maps where the available servers
	 * have changed. These need to be sorted again.
	 */
	final private List<String> waitingGameServersChanged;
	
	public LobbyGameCategoryInfo(String name) {
		super(name);
		
		this.waitingGameServers = new HashMap<String, Map<ServerInfo, Integer>>();
		this.waitingGameServersChanged = new ArrayList<String>();
	}
	
	@Override
	public CategoryType getType() {
		return CategoryType.LOBBY_GAME;
	}
	
	synchronized private Map<ServerInfo, Integer> getServers(String mapName) {
		Map<ServerInfo, Integer> map = this.waitingGameServers.get(mapName);
		
		if (map == null) {
			map = new HashMap<ServerInfo, Integer>();
			this.waitingGameServers.put(mapName, map);
		}
		else {
			if (waitingGameServersChanged.remove(mapName)) {
				map = MapUtils.sortByValueReverse(map);
				this.waitingGameServers.put(mapName, map);
			}
		}
		
		return map;
	}

	/**
	 * Add a server to the given map. If map is null the
	 * server will be set as free.
	 * The server's category has to be exactly this category.
	 * The method will throw an exception if the category doesn't
	 * match.
	 * 
	 * @param map the map to add the server to, null for free server
	 * @param server the server to add, category has to be this
	 * @param players the amount of players on the server
	 * @throws IllegalArgumentException thrown if the category doesn't match
	 */
	synchronized public void putGameServer(String map, ServerInfo server, int players) {
		if (server.getCategory() != this)
			throw new IllegalArgumentException("The servers category doesn't match! Expected: " + this.getName() + " Given: " + server.getCategory().getName());

		Map<ServerInfo, Integer> servers = getServers(map);
		servers.put(server, players);
		waitingGameServersChanged.add(map);
	}
	
	/**
	 * Remove the given server from the specified map.
	 * A map of null represents free waiting servers.
	 * The server's category has to be equal to this.
	 * The method will throw an exception if the
	 * category doesn't match.
	 * 
	 * @param map the map in which the server should be found, null for a free server
	 * @param server the server to remove
	 * @throws IllegalArgumentException thrown if the category doesn't match
	 */
	synchronized public void removeGameServer(String map, ServerInfo server) {
		if (server.getCategory() != this)
			throw new IllegalArgumentException("The servers category doesn't match! Expected: " + this.getName() + " Given: " + server.getCategory().getName());

		Map<ServerInfo, Integer> servers = getServers(map);
		servers.remove(server);
		waitingGameServersChanged.add(map);
	}
	
	/**
	 * Get a server for the given map. Passing null will get a
	 * free server. If there is no server available null is
	 * returned
	 * 
	 * @param map the map to get a server for, null for a free server
	 * @return the found server or null if none exists
	 */
	synchronized public ServerInfo getGameServer(String map) {
		return getServers(map).keySet().iterator().next();
	}
	
	@Override
	public String dump() {
		String msg = super.dump();
		
		for (Entry<String, Map<ServerInfo, Integer>> entry : waitingGameServers.entrySet()) {
			msg += entry.getKey() + "\n";
			
			for (Entry<ServerInfo, Integer> serverEntry : entry.getValue().entrySet())
				msg += "\t- " + serverEntry.getKey().getName() + "(" + serverEntry.getValue() + ")\n";
		}
		return msg;
	}
}
