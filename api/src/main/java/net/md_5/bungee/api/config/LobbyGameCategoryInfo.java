package net.md_5.bungee.api.config;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LobbyGameCategoryInfo extends CategoryInfo {

	/**
	 * The map contains all game servers of this category
	 * The key specifies the games map while the value is
	 * a deque of servers.
	 * The null key is used for servers who aren't set to
	 * a map and may be set to one.
	 */
	final private Map<String, Deque<ServerInfo>> waitingGameServers;
	
	public LobbyGameCategoryInfo(String name) {
		super(name);
		this.waitingGameServers = new HashMap<String, Deque<ServerInfo>>();
	}
	
	@Override
	public CategoryType getType() {
		return CategoryType.LOBBY_GAME;
	}
	
	synchronized private Deque<ServerInfo> getDeque(String map) {
		Deque<ServerInfo> deque = this.waitingGameServers.get(map);
		
		if (deque == null) {
			deque = new ArrayDeque<ServerInfo>();
			this.waitingGameServers.put(map, deque);
		}
		
		return deque;
	}
	/**
	 * Add a server to the given map. If map is null the
	 * server will be set as free.
	 * The server's category has to be exactly this category.
	 * The method will return false if either the categories
	 * don't match or the server is already in the given category.
	 * In these cases the server hasn't been added.
	 * 
	 * @param map the map to add the server to, null for free server
	 * @param server the server to add, category has to be this
	 * @return whether the server has been added
	 */
	synchronized public boolean putGameServer(String map, ServerInfo server) {
		if (server.getCategory() != this)
			return false;
		Deque<ServerInfo> deque = getDeque(map);
		if (deque.contains(server))
			return false;
		return deque.offerLast(server);
	}
	
	/**
	 * Remove the given server from the specified map deque.
	 * A map of null represents free waiting servers.
	 * The server's category has to be equal to this.
	 * The method will return true if the server has been removed
	 * and false if the category doesn't match or the server
	 * wasn't in the deque.
	 * 
	 * @param map the map in which the server should be found, null for a free server
	 * @param server the server to remove
	 * @return true if the server has been removed.
	 */
	synchronized public boolean removeGameServer(String map, ServerInfo server) {
		if (server.getCategory() != this)
			return false;
		
		return getDeque(map).remove(server);
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
		return getDeque(map).peekFirst();
	}
	
	@Override
	public String dump() {
		String msg = super.dump();
		
		for (Entry<String, Deque<ServerInfo>> entry : waitingGameServers.entrySet()) {
			msg += entry.getKey() + "\n";
			
			for (ServerInfo server : entry.getValue())
				msg += "\t- " + server.getName() + "\n";
		}
		return msg;
	}
}
