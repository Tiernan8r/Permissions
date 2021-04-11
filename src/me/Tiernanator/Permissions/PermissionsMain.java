package me.Tiernanator.Permissions;

import me.Tiernanator.Permissions.Commands.Groups.GetGroup;
import me.Tiernanator.Permissions.Commands.Groups.GroupDemote;
import me.Tiernanator.Permissions.Commands.Groups.GroupPromote;
import me.Tiernanator.Permissions.Commands.Groups.SetGroup;
import me.Tiernanator.Permissions.Commands.Permissions.TestPermission;
import me.Tiernanator.Permissions.Events.GroupEventHandler;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Permissions.PermissionHandlers.OnCommand;
import me.Tiernanator.Permissions.PermissionHandlers.OnEvent;
import me.Tiernanator.Permissions.PermissionsMenu.ListPermissions;
import me.Tiernanator.Utilities.File.Log;
import me.Tiernanator.Utilities.SQL.SQLServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionsMain extends JavaPlugin {

	private static Log log;
	
	public static Log getLog() {
		return log;
	}
	
	private static void setLog(Log l) {
		log = l;
	}
	
	@Override
	public void onEnable() {

		Log log = new Log(this);
		setLog(log);
		
		initialiseSQL();
		allocateConstants();

		registerCommands();
		registerEvents();

		Permission.initialisePluginPermissions();
		
		for(Player player : getServer().getOnlinePlayers()) {
			GroupAccessor groupAccessor = new GroupAccessor(player);
			Group group = groupAccessor.getPlayerGroup();
			Permission.setGroup(group, player);
		}
	}

	@Override
	public void onDisable() {
		log.close();

	}

	private void registerCommands() {
		// all the commands
		getCommand("permissions").setExecutor(new ListPermissions(this));
		getCommand("setGroup").setExecutor(new SetGroup(this));
		getCommand("promote").setExecutor(new GroupPromote(this));
		getCommand("demote").setExecutor(new GroupDemote(this));
		getCommand("testPermission").setExecutor(new TestPermission(this));
		getCommand("getGroup").setExecutor(new GetGroup(this));
		
	}

	private void registerEvents() {
		// the two events handle the player leaving & joining
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new GroupEventHandler(this), this);
		pm.registerEvents(new OnCommand(this), this);
		
		pm.registerEvents(new OnEvent(this), this);
		
		
	}

	private void allocateConstants() {
		GroupAccessor.setPlugin(this);
		Group.setPlugin(this);
		Permission.setPlugin(this);
		Group.initialiseGroups(this);
		GetGroup.setPlugin(this);
	}

	private void initialiseSQL() {
		
		String query = "CREATE TABLE IF NOT EXISTS PermissionUsers ( "
				+ "UUID varchar(36) NOT NULL, "
				+ "PlayerGroup varchar(255) NOT NULL,"
				+ "PRIMARY KEY (UUID)"
				+ ");";
		SQLServer.executeQuery(query);
		
	}

}
