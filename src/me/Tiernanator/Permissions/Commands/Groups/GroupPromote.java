package me.Tiernanator.Permissions.Commands.Groups;

import me.Tiernanator.Permissions.Events.CustomEvents.CustomGroupChangeEvent;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Players.GetPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GroupPromote implements CommandExecutor {

	private static PermissionsMain plugin;
	
	// Colour Constants
	private static ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private static ChatColor warning = Colour.WARNING.getColour();
	private static ChatColor good = Colour.GOOD.getColour();
	private static ChatColor informative = Colour.INFORMATIVE.getColour();

	public GroupPromote(PermissionsMain main) {
		plugin = main;
	}

	/*
	 * This Command Increases the players GroupSaver by 1, the opposite to
	 * GroupDemote
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// if the sender didn't specify a player
		if (args.length < 1) {

			// tell the sender too, you know...
			sender.sendMessage(warning + "Please specify a player.");
			return false;
		}

		// if the console wants to promote a player
		if (!(sender instanceof Player)) {
			
			Player player = GetPlayer.getPlayer(args[0], sender, warning, highlight);
			
			if (player == null) {
				return false;
			}
			// the function returns false if the player couldn't be promoted,
			// if success is null, the player doesn't have a group assigned
			boolean success = promotePlayer(player);
			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group playerGroup = groupAccessor.getPlayerGroup();
			
			if (success) {

				sender.sendMessage(highlight + player.getName() + good
						+ " was promoted.");

			} else {

				sender.sendMessage(highlight + player.getName() + warning
						+ " could not be promoted, they are part of "
						+ informative + playerGroup.getName() + warning + ".");

			}
		}

		// if a player made the command
		if (sender instanceof Player) {

			// another player was specified

			// get the player sender
			Player playerSender = (Player) sender;
			// get the specified player
			Player playerToPromote = GetPlayer.getPlayer(args[0],
					playerSender, warning, highlight);

			if (playerToPromote == null) {
				return false;
			}
			
			if(playerSender.equals(playerToPromote)) {
				playerSender.sendMessage(warning + "You can't promote yourself...");
				return false;
			}

			// Get the sender's group and the
			// player to promote's group
			GroupAccessor senderGroupAccessor = new GroupAccessor(playerSender);
			Group senderGroup = senderGroupAccessor.getPlayerGroup();
			GroupAccessor playerToPromoteGroupAccessor = new GroupAccessor(playerToPromote);
			Group playerToPromoteGroup = playerToPromoteGroupAccessor.getPlayerGroup();
//			Group senderGroup = Group.getPlayerGroup(playerSender);
//			Group playerToPromoteGroup = Group.getPlayerGroup(playerToPromote);
			
			List<Group> allGroups = Group.allGroups();
			int senderIndex = allGroups.indexOf(senderGroup);
			int playerToPromoteIndex = allGroups.indexOf(playerToPromoteGroup);

			if (senderIndex == 0) {
				playerSender.sendMessage(
						warning + "You can only promote a player from a lower group, and you are in the lowest group.");
				return false;
			}
			
			if(playerToPromoteIndex > senderIndex) {
				// Inform the sender of the action
				playerSender.sendMessage(highlight + playerToPromote.getName()
						+ warning + " cannot be promoted, they are in a higher group than you.");

				return true;
			}

			if (playerToPromoteIndex == allGroups.size() - 1) {
				playerSender.sendMessage(highlight + playerToPromote.getName()
						+ warning
						+ " could not be promoted, they are a member of the final group.");

				return false;
			}
			
			if(playerToPromoteIndex == senderIndex) {
				// Inform the sender of the action
				playerSender.sendMessage(highlight + playerToPromote.getName()
						+ warning + " cannot be promoted, they are in the same group as you.");

				return true;
			}
			
			if((senderIndex == allGroups.size() - 1) || playerToPromoteIndex < senderIndex) {
				// demote Function
				promotePlayer(playerToPromote);
				// Inform the sender of the action
				playerSender.sendMessage(highlight + playerToPromote.getName()
						+ good + " was promoted.");

				return true;
			}
			
			playerSender.sendMessage(highlight + playerToPromote.getName()
			+ warning + " cannot be promoted they are in a higher group than you.");

			return true;
		}
		return false;

	}

	// the promote player function
	public static boolean promotePlayer(Player player) {
		// the usual:
		// check if the player has the group permission,
		// if so, remove all permissions, add permissions for the next group
		// tier
		// then inform the player of the change.
		
		List<Group> allGroups = Group.allGroups();
		GroupAccessor groupAccessor = new GroupAccessor(player);
		Group playerGroup = groupAccessor.getPlayerGroup();
		
		int groupIndex = allGroups.indexOf(playerGroup);
		
		if(groupIndex == allGroups.size() - 1) {
			return false;
		}
		
		int newIndex = groupIndex + 1;
		
		Group newGroup = allGroups.get(newIndex);
		
		// set permissions for the lower group
		groupAccessor.setPlayerGroup(newGroup);
		// inform them of the change
		player.sendMessage(good + "You have been promoted to the group: "
				+ highlight + newGroup.getName() + good + ".");
		
		CustomGroupChangeEvent groupChangeEvent = new CustomGroupChangeEvent(player, newGroup);
		plugin.getServer().getPluginManager().callEvent(groupChangeEvent);
		
		return true;

	}
}