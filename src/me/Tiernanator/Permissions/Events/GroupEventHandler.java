package me.Tiernanator.Permissions.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Permissions.Main;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;

public class GroupEventHandler implements Listener {

	@SuppressWarnings("unused")
	private static Main plugin;

	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor highlight = Colour.HIGHLIGHT.getColour();

	public GroupEventHandler(Main main) {
		plugin = main;
	}

	public static void setPlugin(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void applyGroupOnPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		Group group = null;
		GroupAccessor groupAccessor = new GroupAccessor(player);
		
		if(!player.hasPlayedBefore()) {
			for(Group g : Group.allGroups()) {
				if(g.isDefault()) {
					group = g;
				}
			}
			groupAccessor.setPlayerGroup(group);
//			Group.setPlayerGroup(player, group);
			player.sendMessage(good + "As it is your first time on the server, you have been added to the default group: " + highlight
						+ group.getName());
		} else {
			Group playerGroup = groupAccessor.getPlayerGroup();
//			groupAccessor.setPlayerGroup(playerGroup);
//			Group.setPlayerGroup(player, group);
			player.sendMessage(good + "You are in the Group: " + highlight + playerGroup.getName());
		}
		
	}
}
