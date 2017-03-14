package me.Tiernanator.Permissions.Commands.Permissions;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.Main;
import me.Tiernanator.Permissions.Permission;

public class ListPermissions implements CommandExecutor {
	
	//This is a constant colour that can be changed only once and will affect all
	// the plugins, it is found in Plugin_Constants in InformationColours
	private static ChatColor informative;
	private static ChatColor highlight;
	private static ChatColor good;
	private static ChatColor warning;

	//sets the value of the plugin to the main class
	public ListPermissions(Main main) {

	}
	
	//This Command sends a player a list of all their active Permissions

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// set the constants' values
		allocate();
		
		//only works for players
		if (!(sender instanceof Player)) {
			sender.sendMessage(warning + "Only players can use this command.");
			return false;
		}
		//get the player
		Player player = (Player) sender;
		
		
		Permission.listPermissions(player, good, highlight, informative);
		
		return true;
	}
	
	// set the colour constants' values
	private void allocate() {
				
		warning = Colour.WARNING.getColour();
		highlight = Colour.HIGHLIGHT.getColour();
		good = Colour.GOOD.getColour();
		informative = Colour.INFORMATIVE.getColour();
		
	}
}
