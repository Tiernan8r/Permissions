package me.Tiernanator.Permissions.Commands.Groups;

import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Players.GetPlayer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGroup implements CommandExecutor {

	// plugin has changed to static because the function references it.
	@SuppressWarnings("unused")
	private static PermissionsMain plugin;

	// the colour constants, values set in allocate()
	private static ChatColor highlight = Colour.HIGHLIGHT.getColour();
	private static ChatColor warning = Colour.WARNING.getColour();
	private static ChatColor good = Colour.GOOD.getColour();
	private static ChatColor bad = Colour.BAD.getColour();
	private static ChatColor informative = Colour.INFORMATIVE.getColour();

	public SetGroup(PermissionsMain main) {
		plugin = main;
	}

	/*
	 * This Command sets the specified players' group, only the console, ops and
	 * owners are allowed to use it.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		// Initially handles if the player inputs the command
		if (sender instanceof Player) {
			GroupAccessor groupAccessor = new GroupAccessor((Player) sender);
			Group senderGroup = groupAccessor.getPlayerGroup();
			// Group senderGroup = Group.getPlayerGroup((Player) sender);
			// only ops & the players in the highest group can use this
			if (!senderGroup.membersOP()) {
				sender.sendMessage(warning + "You can't use this command.");
				return false;
			}
		}
		// if the console didn't give a player's name and group
		if (args.length < 2) {
			sender.sendMessage(
					warning + "You must specify a Player and a GroupSaver");
			return false;
		}
		OfflinePlayer playerForPermission = GetPlayer.getOfflinePlayer(args[0],
				sender, warning, highlight);

		if (playerForPermission == null) {
			return false;
		}

		// the group to be added to is the second argument.
		String groupName = args[1];
		Group group = Group.getGroup(groupName);

		if (group == null) {
			// the group specified was not a valid one
			sender.sendMessage(bad + "That is not a group, the Groups are:");
			// tell them each group in turn
			for (Group g : Group.allGroups()) {
				sender.sendMessage(informative + " - " + g.getName() + ".");
			}
			return false;
		}

		GroupAccessor groupAccessor = new GroupAccessor(playerForPermission);
		Group currentGroup = groupAccessor.getPlayerGroup();

		if (currentGroup.equals(group)) {
			sender.sendMessage(bad + "The Player " + highlight
					+ playerForPermission.getName() + bad
					+ " is already in the Group " + highlight + group.getName()
					+ bad + ".");
			return false;
		}

		// reset their group to the specified one
		// Group.setPlayerGroup(playerForPermission, group);
		groupAccessor.setPlayerGroup(group);
		// inform them of the change
		if (playerForPermission.isOnline()) {
			playerForPermission.getPlayer()
					.sendMessage(good + "You have been added to the group: "
							+ highlight + group.getName());
		}
		sender.sendMessage(highlight + playerForPermission.getName() + good
				+ " was added to the group: " + informative + group.getName());
		return true;

	}

}