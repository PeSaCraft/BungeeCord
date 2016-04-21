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
public class CategorySwitchEvent extends Event {
	
	/**
	 * The switching player.
	 */
	final private ProxiedPlayer player;
	
	/**
	 * The category the player switches from.
	 */
	final private CategoryInfo fromCategory;
	
	/**
	 * The category the player switches to.
	 */
	final private CategoryInfo toCategory;

}
