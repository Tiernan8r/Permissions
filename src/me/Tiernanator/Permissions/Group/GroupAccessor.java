package me.Tiernanator.Permissions.Group;

import me.Tiernanator.Permissions.Events.CustomEvents.CustomGroupChangeEvent;
import me.Tiernanator.Permissions.Permission;
import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.SQL.SQLServer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GroupAccessor {

	private static PermissionsMain plugin;
	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}

	private OfflinePlayer player;
	public GroupAccessor(OfflinePlayer player) {
		this.player = player;
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public String getPlayerGroupName() {

		String playerUUID = getPlayer().getUniqueId().toString();
		String query = "SELECT PlayerGroup FROM PermissionUsers WHERE UUID = '"
				+ playerUUID + "';";

		return SQLServer.getString(query, "PlayerGroup");

	}

	public Group getPlayerGroup() {
		String groupName = getPlayerGroupName();
		Group group = Group.getGroup(groupName);
		return group;
	}

	public void setPlayerGroup(Group group) {

		String playerUUID = getPlayer().getUniqueId().toString();
		if (!hasPlayerGroup()) {
			
			String statement = "INSERT INTO PermissionUsers (UUID, PlayerGroup) VALUES (?, ?);";
			Object[] values = new Object[]{playerUUID, group.getName()};
			SQLServer.executePreparedStatement(statement, values);
		
		} else {

			String statement = "UPDATE PermissionUsers SET PlayerGroup = ? WHERE UUID = ?;";
			Object[] values = new Object[]{group.getName(), playerUUID};
			SQLServer.executePreparedStatement(statement, values);

		}
		OfflinePlayer player = getPlayer();
		if (player.isOnline()) {
			Permission.setGroup(group, (Player) player);

			player.setOp(group.membersOP());

			CustomGroupChangeEvent groupChangeEvent = new CustomGroupChangeEvent(
					(Player) player, group);
			plugin.getServer().getPluginManager().callEvent(groupChangeEvent);

		}

	}

	public void setPlayerGroup(String groupName) {
		Group group = Group.getGroup(groupName);
		if (group == null) {
			return;
		}
		setPlayerGroup(group);
	}

	public boolean hasPlayerGroup() {
		return getPlayerGroup() != null;
	}

}
