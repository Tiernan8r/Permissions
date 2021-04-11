package me.Tiernanator.Permissions.Events;

import me.Tiernanator.Permissions.Commands.Groups.GetGroup;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GroupEventHandler implements Listener {

	@SuppressWarnings("unused")
	private static PermissionsMain plugin;

	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor highlight = Colour.HIGHLIGHT.getColour();

	public GroupEventHandler(PermissionsMain main) {
		plugin = main;
	}

	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}
	
	@EventHandler
	public void applyGroupOnPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		Group group = null;
		GroupAccessor groupAccessor = new GroupAccessor(player);
		
		if(!player.hasPlayedBefore() || !groupAccessor.hasPlayerGroup()) {
			for(Group g : Group.allGroups()) {
				if(g.isDefault()) {
					group = g;
				}
			}
			groupAccessor.setPlayerGroup(group);
			player.sendMessage(good + "As it is your first time on the server, you have been added to the default group: " + highlight
						+ group.getName());
		} else {
			GetGroup.playerInformGroup(player);
		}
		
	}
}
