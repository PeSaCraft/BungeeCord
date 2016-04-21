package net.md_5.bungee.api.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.CategoryInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * Represents a player getting kicked from a server.
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ServerKickEvent extends Event implements Cancellable
{

    /**
     * Cancelled status.
     */
    private boolean cancelled;
    /**
     * Player being kicked.
     */
    private final ProxiedPlayer player;
    /**
     * The server the player was kicked from, should be used in preference to
     * {@link ProxiedPlayer#getServer()}.
     */
    private final ServerInfo kickedFrom;
    /**
     * Kick reason.
     */
    private BaseComponent[] kickReasonComponent;
    /**
     * Category to send player to if this event is cancelled.
     */
    private CategoryInfo cancelCategory;
    /**
     * State in which the kick occured.
     */
    private State state;

    public enum State
    {

        CONNECTING, CONNECTED, UNKNOWN;
    }

    @Deprecated
    public ServerKickEvent(ProxiedPlayer player, BaseComponent[] kickReasonComponent, CategoryInfo cancelCategory)
    {
        this( player, kickReasonComponent, cancelCategory, State.UNKNOWN );
    }

    @Deprecated
    public ServerKickEvent(ProxiedPlayer player, BaseComponent[] kickReasonComponent, CategoryInfo cancelCategory, State state)
    {
        this( player, player.getServer().getInfo(), kickReasonComponent, cancelCategory, state );
    }

    public ServerKickEvent(ProxiedPlayer player, ServerInfo kickedFrom, BaseComponent[] kickReasonComponent, CategoryInfo cancelCategory, State state)
    {
        this.player = player;
        this.kickedFrom = kickedFrom;
        this.kickReasonComponent = kickReasonComponent;
        this.cancelCategory = cancelCategory;
        this.state = state;
    }

    @Deprecated
    public String getKickReason()
    {
        return BaseComponent.toLegacyText( kickReasonComponent );
    }

    @Deprecated
    public void setKickReason(String reason)
    {
        kickReasonComponent = TextComponent.fromLegacyText( reason );
    }
}
