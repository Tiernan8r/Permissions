package me.Tiernanator.Permissions.Commands.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.PermissionsMain;


//The whole command is a bit redundant due to listPermission(), but it's here for convenience.
public class TestPermission implements CommandExecutor {

	// these colour constants will recur throughout the plugins
	private ChatColor bad;
	private ChatColor good;
	private ChatColor warning;
	
	public TestPermission(PermissionsMain main) {

	}

	// This Command tests if the player has the specified permission, and informs them
	// if they do or not.
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// same again
		allocate();
		
		//only Players can have permissions...
		if(!(sender instanceof Player)) {
			sender.sendMessage(warning + "Only players can use this command.");
			return false;
		}
		// get the player
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(bad + "You must provide the permission to test!");
			return false;
		}
		
		//get the inputted permission
		String permission = args[0];
		//if the player has the inputed permission, it tells them it does,
		// if not vice versa.
		if(player.hasPermission(permission)) {
			//the good bad craic is just those colour constants.
			player.sendMessage(good + "You have this Permission.");
			
		} else {
			player.sendMessage(bad + "You do not have this Permission.");
			
		}
		return true;
	}
	
	private void allocate() {
				
		warning = Colour.WARNING.getColour();
		good = Colour.GOOD.getColour();
		bad = Colour.BAD.getColour();
	
	}

}
