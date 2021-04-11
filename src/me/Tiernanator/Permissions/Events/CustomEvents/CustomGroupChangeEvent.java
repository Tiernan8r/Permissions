package me.Tiernanator.Permissions.Events.CustomEvents;

import me.Tiernanator.Permissions.Group.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//This is the custom player create portal event with a flint and steel event that is called by PlayerFlintAndSteelInteract, it just contains functions that
//return all the values needed

public final class CustomGroupChangeEvent extends Event {
	
	//handlers is a variable "handled"(...) by the server
    private static final HandlerList handlers = new HandlerList();
    //the player who got a new group
    private Player player;
    //The new group
    private Group group;
    

    //constructor for the event that sets the variables
    public CustomGroupChangeEvent(Player player, Group newGroup) {
        this.player = player;
        this.group = newGroup;
    }

    //get the player who done it
    public Player getPlayer() {
        return player;
    }
    //get the new group
    public Group getGroup() {
    	return this.group;
    }

    
    //the next two are necessary for the server to use the event
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}