package me.Tiernanator.Permissions.Commands.Groups;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Utilities.Players.GetPlayer;

public class GetGroup implements CommandExecutor {

	@SuppressWarnings("unused")
	private static PermissionsMain plugin;

	// group arrays
	static List<String> groups;

	// I told you they recurred...(I did in TestPermission anyway)
	private static ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private static ChatColor warning = Colour.WARNING.getColour();
	private static ChatColor good = Colour.GOOD.getColour();
	private static ChatColor bad = Colour.BAD.getColour();
	private static ChatColor informative = Colour.INFORMATIVE.getColour();

	// this has to stay the Main class won't be happy.
	public GetGroup(PermissionsMain main) {
		plugin = main;
	}

	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}

	// this Command Sends the player a message with their GroupSaver display name.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// set the constants

		// only Players can check for themselves
		if (!(sender instanceof Player)) {
			if (args.length < 1) {
				sender.sendMessage(
						warning + "You must specify the name of a Player.");
				return false;
			}
			String playerName = args[0];

			Player player = GetPlayer.getPlayer(playerName,
					sender, warning, highlight);

			if (player == null) {
				return false;
			}
			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group playerGroup = groupAccessor.getPlayerGroup();

			sender.sendMessage(
					good + "The player " + informative + player.getName() + good
							+ " is in the group: " + highlight + playerGroup.getName());
			return true;

		}
		if (args.length < 1) {
			Player player = (Player) sender;
			// uses the getGroup() function found below
			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group playerGroup = groupAccessor.getPlayerGroup();
//			String group = Group.getPlayerGroupName(player);
			if (playerGroup == null) {
				player.sendMessage(bad
						+ "I have absolutely no clue what group you belong to!!");
				return false;

			}

			sender.sendMessage(
					good + "You are in the group: " + highlight + playerGroup.getName());

			return true;
		} else {
			String playerName = args[0];

			Player player = GetPlayer.getPlayer(playerName, (Player) sender,
					warning, highlight);

			if (player == null) {
				return false;
			}

			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group playerGroup = groupAccessor.getPlayerGroup();

			if (player.equals((Player) sender)) {
				sender.sendMessage(good + "You are in the group: " + highlight
						+ playerGroup);
				return true;
			}

			sender.sendMessage(
					good + "The player " + informative + player.getName() + good
							+ " is in the group: " + highlight + playerGroup);
			return true;
		}
	}
	
	public static void playerInformGroup(Player player) {
		GroupAccessor groupAccessor = new GroupAccessor(player);
		Group playerGroup = groupAccessor.getPlayerGroup();
		player.sendMessage(good + "You are in the group: " + highlight
					+ playerGroup.getName());
		
	}

}
