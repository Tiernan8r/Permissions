package me.Tiernanator.Permissions.Commands.Groups;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Permissions.Events.CustomEvents.CustomGroupChangeEvent;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Utilities.Players.GetPlayer;

public class GroupDemote implements CommandExecutor {

	private static PermissionsMain plugin;

	// Yet again the whole constants craic
	private static ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private static ChatColor warning = Colour.WARNING.getColour();
	private static ChatColor good = Colour.GOOD.getColour();
	private static ChatColor informative = Colour.INFORMATIVE.getColour();

	public GroupDemote(PermissionsMain main) {
		plugin = main;
	}

	/*
	 * This Command lowers a player group by one, as you will see, it does so by
	 * unsetting all permissions, then giving the the permissions for the lower
	 * tier group.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		// the command can't work if the sender did not specify a player.
		if (args.length < 1) {

			// tell the sender it's terrible
			sender.sendMessage(warning + "Please specify a player.");
			return false;

		}

		// if the console wants to promote a player
		if (!(sender instanceof Player)) {

			// get the specified player
			Player player = GetPlayer.getPlayer(args[0],
						sender, warning,
						highlight);
			if (player == null) {
				return false;
			}

			// if success is false, the player couldn't be demoted, if success
			// is null
			// the player does not have a group assigned to it
			boolean success = demotePlayer(player);
			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group playerGroup = groupAccessor.getPlayerGroup();
			
			if (success) {

				sender.sendMessage(highlight + player.getName() + good
						+ " was demoted to the group: " + informative
						+ playerGroup.getName() + good + ".");

			} else if (Boolean.valueOf(success) == null) {

				sender.sendMessage(highlight + player.getName() + warning
						+ " does not have an assigned group, I think you should go check this out!");

			} else {

				sender.sendMessage(highlight + player.getName() + warning
						+ " could not be demoted, they are in the group "
						+ informative + playerGroup.getName() + warning + ".");

			}
		}

		// if a player made the command
		if (sender instanceof Player) {

			// If a player specified another player:

			// get the player sender
			Player playerSender = (Player) sender;
			// get the specified player
			Player playerToDemote = GetPlayer.getPlayer(args[0],
					(Player) sender, warning, highlight);

			//Can't use a player that doesn't exist
			if (playerToDemote == null) {
				return false;
			}
			
			//players can only demote other players
			if(playerSender.equals(playerToDemote)) {
				playerSender.sendMessage(warning + "You can't demote yourself...");
				return false;
			}
			
			// get the group of the sender and playerToDemote
			GroupAccessor senderGroupAccessor = new GroupAccessor(playerSender);
			Group senderGroup = senderGroupAccessor.getPlayerGroup();
			GroupAccessor playerToDemoteGroupAccessor = new GroupAccessor(playerToDemote);
			Group playerToDemoteGroup = playerToDemoteGroupAccessor.getPlayerGroup();
//			Group senderGroup = Group.getPlayerGroup(playerSender);
//			Group playerToDemoteGroup = Group.getPlayerGroup(playerToDemote);

			List<Group> allGroups = Group.allGroups();
			int senderIndex = allGroups.indexOf(senderGroup);
			int playerToDemoteIndex = allGroups.indexOf(playerToDemoteGroup);

			if (senderIndex == 0) {
				playerSender.sendMessage(
						warning + "You can only demote a player from your group or lower, and you are in the lowest group.");
				return false;
			}

			//if the player is already in the lowest group:
			if (playerToDemoteIndex == 0) {
				playerSender.sendMessage(highlight + playerToDemote.getName()
						+ warning
						+ " could not be demoted, they are already part of the lowest group.");

				return false;
			}
			
			//if the sender is in the final group, has a higher group than the playerToDemote or they are in
			// the same group:
			if((senderIndex == allGroups.size() - 1) || playerToDemoteIndex < senderIndex || playerToDemoteIndex == senderIndex) {
				// demote Function
				demotePlayer(playerToDemote);
				GroupAccessor groupAccessor = new GroupAccessor(playerToDemote);
				Group playerGroup = groupAccessor.getPlayerGroup();
				// Inform the sender of the action
				playerSender.sendMessage(highlight + playerToDemote.getName()
						+ good + " was demoted to " + highlight
						+ playerGroup.getName() + good + ".");

				return true;
			}
			
			playerSender.sendMessage(highlight + playerToDemote.getName()
			+ warning + " cannot be demoted they are in a higher group than you.");

			return true;
		}
		return false;
	}

	// /this function handles player demotion
	public static boolean demotePlayer(Player player) {

		List<Group> allGroups = Group.allGroups();
		GroupAccessor groupAccessor = new GroupAccessor(player);
		Group playerGroup = groupAccessor.getPlayerGroup();

		int groupIndex = allGroups.indexOf(playerGroup);

		//can't go lower than 0.
		if (groupIndex == 0) {
			return false;
		}

		//lower the group
		int newIndex = groupIndex - 1;

		Group newGroup = allGroups.get(newIndex);

		// set permissions for the lower group
		groupAccessor.setPlayerGroup(newGroup);
//		Group.setPlayerGroup(player, newGroup);
		// inform them of the change
		player.sendMessage(good + "You have been demoted to the group: "
				+ highlight + newGroup.getName() + good + ".");
		
		CustomGroupChangeEvent groupChangeEvent = new CustomGroupChangeEvent(player, newGroup);
		plugin.getServer().getPluginManager().callEvent(groupChangeEvent);
		
		return true;

	}

}
