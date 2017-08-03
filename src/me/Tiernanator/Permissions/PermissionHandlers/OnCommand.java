package me.Tiernanator.Permissions.PermissionHandlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Tiernanator.Permissions.Permission;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Events.CommandCallEvent;

public class OnCommand implements Listener {

	@SuppressWarnings("unused")
	private static PermissionsMain plugin;

	private ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();

	public OnCommand(PermissionsMain main) {
		plugin = main;
	}

	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerCommand(CommandCallEvent event) {

		Player player = event.getPlayer();

		String commandName = event.getCalledCommandName(); 
		
		String permission = Permission.getPermissionForCommand(commandName);
		
		if(permission == null) {
			return;
		}
		
		if(!player.hasPermission(permission)) {
			player.sendMessage(warning + "You lack the permission " + highlight + permission + warning + " to use the command  " + informative + commandName + warning + ".");
			event.setCancelled(true);
		}
		
	}
	
}
