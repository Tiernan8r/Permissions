package me.Tiernanator.Permissions.Commands.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.Main;
import me.Tiernanator.Permissions.Permission;

public class OnCommand implements Listener {

	@SuppressWarnings("unused")
	private static Main plugin;

	private ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();

	public OnCommand(Main main) {
		plugin = main;
	}

	public static void setPlugin(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerUseCommand(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();

		String wholeString = event.getMessage();
		String commandName;
		if(wholeString.contains(" ")) {
			commandName = wholeString.substring(0, wholeString.indexOf(" "));
		} else {
			commandName = wholeString;
		}
		
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
