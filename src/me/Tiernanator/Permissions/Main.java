package me.Tiernanator.Permissions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.File.Log;
import me.Tiernanator.Permissions.Commands.Groups.GetGroup;
import me.Tiernanator.Permissions.Commands.Groups.GroupDemote;
import me.Tiernanator.Permissions.Commands.Groups.GroupPromote;
import me.Tiernanator.Permissions.Commands.Groups.SetGroup;
import me.Tiernanator.Permissions.Commands.Permissions.OnCommand;
import me.Tiernanator.Permissions.Commands.Permissions.TestPermission;
import me.Tiernanator.Permissions.Events.GroupEventHandler;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;
import me.Tiernanator.Permissions.PermissionsMenu.ListPermissions;
import me.Tiernanator.SQL.SQLServer;
import me.Tiernanator.SQL.MySQL.MySQL;

public class Main extends JavaPlugin {

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
		try {
			getSQL().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
		
	}

	private void allocateConstants() {
		GroupAccessor.setPlugin(this);
		Group.setPlugin(this);
		Permission.setPlugin(this);
		Group.initialiseGroups(this);
		GetGroup.setPlugin(this);
	}

	private static MySQL mySQL;

	private void initialiseSQL() {
		
		mySQL = new MySQL(SQLServer.HOSTNAME, SQLServer.PORT, SQLServer.DATABASE,
				SQLServer.USERNAME, SQLServer.PASSWORD);

//		String query = "CREATE DATABASE IF NOT EXISTS permissions;";

		Connection connection = null;
		try {
			connection = mySQL.openConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

//		query = "USE permissions;";
		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";

		statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		query = "CREATE TABLE IF NOT EXISTS PermissionUsers ( "
				+ "UUID varchar(36) NOT NULL, "
				+ "PlayerGroup varchar(255) NOT NULL" + ");";

		statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static MySQL getSQL() {
		return mySQL;
	}

//	public static Connection getSQLConnection() {

//		try {
//			if (!getSQL().checkConnection()) {
//			return getSQL().openConnection();
//		} else {
//			return getSQL().getConnection();
//		}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//		Connection connection = null;
//		try {
//			if (!getSQL().checkConnection()) {
//				connection = getSQL().openConnection();
//			} else {
//				connection = getSQL().getConnection();
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return connection;
//	}
}
