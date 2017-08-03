package me.Tiernanator.Permissions.PermissionHandlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Tiernanator.Permissions.Permission;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Events.EventCallEvent;

public class OnEvent implements Listener {

	@SuppressWarnings("unused")
	private static PermissionsMain plugin;

	private ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();

	public OnEvent(PermissionsMain main) {
		plugin = main;
	}

	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerCommand(EventCallEvent event) {
		
		Player player = event.getPlayer();
		Event calledEvent = event.getCalledEvent();
		
		String permission = Permission.getPermissionForEvent(calledEvent);
		
		if(permission == null) {
			return;
		}
		String eventName = event.getCalledEvent().getEventName();
		
		if(!player.hasPermission(permission)) {
			player.sendMessage(warning + "You lack the permission " + highlight + permission + warning + " to call the event  " + informative + eventName + warning + ".");
			event.trySetCancel(true);
		}
		
	}
	
}
