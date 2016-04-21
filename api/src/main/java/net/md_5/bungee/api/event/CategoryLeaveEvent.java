package net.md_5.bungee.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Data
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class CategoryLeaveEvent extends Event {
	
	/**
	 * The leaving player.
	 */
	final private ProxiedPlayer player;
	
	/**
	 * The category the player leaves.
	 */
	final private CategoryInfo category;
}
