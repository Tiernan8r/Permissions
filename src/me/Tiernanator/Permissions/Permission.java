package me.Tiernanator.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.File.ConfigAccessor;
import me.Tiernanator.File.Log;
import me.Tiernanator.Permissions.Group.Group;
import me.Tiernanator.Permissions.Group.GroupAccessor;

public class Permission {

	private static JavaPlugin plugin;

	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}

	public static void setGroup(Group group, Player player) {
		
		Log log = PermissionsMain.getLog();
		
		if(group == null) {
			log.log("Player " + player.getName() + "'s group is null!");
			return;
		}
		
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				log.log("Setting player " + player.getName() + "'s permissions from group " + group.getName() + ":");
				
				List<Group> allGroups = Group.allGroups();
				if (!(allGroups.contains(group))) {
					return;
				}
				log.log("- Clearing " + player.getName() + "'s previous permissions:");
				removePermissions(player, plugin);

				int thisGroupIndex = allGroups.indexOf(group);

				List<String> permissions = new ArrayList<String>();

				for (int i = 0; i <= thisGroupIndex; i++) {
					Group iGroup = allGroups.get(i);
					
					List<String> iPermissions = iGroup.getPermissions();
					for (String p : iPermissions) {
						if (!permissions.contains(p)) {
							permissions.add(p);
						}
					}

				}
				
				log.log("- Setting new permissions for player " + player.getName() + ":");
				setPermissions(player, permissions, plugin);
				player.setOp(group.membersOP());
				log.log("Done.");
				
			}
		};
		runnable.runTaskAsynchronously(plugin);

	}

	// public coz other classes use it, input is the player to remove
	// permissions.
	public static void removePermissions(Player player, JavaPlugin plugin) {

		boolean isOP = player.isOp();
		player.setOp(!isOP);

		GroupAccessor groupAccessor = new GroupAccessor(player);
		Group playerGroup = groupAccessor.getPlayerGroup();
		if (playerGroup == null) {
			player.setOp(isOP);
			return;
		}
		
		Set<PermissionAttachmentInfo> permissionsInfo = player
				.getEffectivePermissions();

		PermissionAttachment attachment = player.addAttachment(plugin);

		for (PermissionAttachmentInfo i : permissionsInfo) {
			attachment.setPermission(i.getPermission(), false);
			Log log = PermissionsMain.getLog();
			log.log(" - Removing permission " + i.getPermission() + " from player " + player.getName() + ".");

		}

		player.setOp(isOP);
		return;
	}

	// public coz other classes use it, input is the player to remove
	// permissions.
	public static void setPermissions(Player player, List<String> permissions,
			JavaPlugin plugin) {

		boolean isOP = false;
		isOP = player.isOp();
		player.setOp(!isOP);

		PermissionAttachment attachment = player.addAttachment(plugin);

		// Preliminary setting
		for (String permission : permissions) {
			attachment.setPermission(permission, true);
		}

		// Setting the actual permissions
		for (PermissionAttachmentInfo i : player.getEffectivePermissions()) {
			attachment.setPermission(i.getPermission(), true);
			Log log = PermissionsMain.getLog();
			log.log(" - Registering permission " + i.getPermission() + " for player " + player.getName() + ".");
		}

		player.setOp(isOP);
		return;
	}

	/**
	 * listPermission() sends a player a list of all their active permissions,
	 * it receives the input of a player and their group, and sends them a list
	 * of the permissions associated with that group.
	 */
	public static void listPermissions(Player player, ChatColor good,
			ChatColor highlight, ChatColor informative) {

		Set<PermissionAttachmentInfo> permissions = player
				.getEffectivePermissions();
		GroupAccessor groupAccessor = new GroupAccessor(player);
		Group playerGroup = groupAccessor.getPlayerGroup();
		player.sendMessage(good + "You are in the group: " + highlight
				+ playerGroup.getName());
		player.sendMessage(good + "And you have the Permissions: ");
		for (PermissionAttachmentInfo i : permissions) {
			String permission = i.getPermission();
			player.sendMessage(informative + " - " + permission);
		}
	}

	public static void initialisePluginPermissions() {
		
		Log log = PermissionsMain.getLog();

		Plugin[] allPlugins = plugin.getServer().getPluginManager()
				.getPlugins();
		for (Plugin pl : allPlugins) {
			ConfigAccessor permissionAccessor = new ConfigAccessor(
					(JavaPlugin) pl, "@permissions.yml");

			List<String> allPermissions = permissionAccessor.getConfig()
					.getStringList("Permissions." + pl.getName() + ".*");
			for (String permission : allPermissions) {
				String description = "";
				description = permissionAccessor.getConfig().getString(
						"Permissions." + permission + ".description");
				try {
					if (description.equalsIgnoreCase("")) {
						continue;
					}
				} catch (Exception e) {
					log.log(Level.WARNING, "Could not find entry "
							+ permission + " for plugin " + pl.getName());
					continue;
				}
				int groupNumber = permissionAccessor.getConfig()
						.getInt("Permissions." + permission + ".group") - 1;
				if (groupNumber < 0) {
					groupNumber = 0;
				}
				
				Group group = Group.allGroups().get(groupNumber);
				group.addPermission(permission);
			}
		}
	}

	public static String getPermissionForCommand(String commandName) {

		Plugin[] allPlugins = plugin.getServer().getPluginManager()
				.getPlugins();
		for (Plugin pl : allPlugins) {
			ConfigAccessor permissionAccessor = new ConfigAccessor(
					(JavaPlugin) pl, "@permissions.yml");

			List<String> allPermissions = permissionAccessor.getConfig()
					.getStringList("Permissions." + pl.getName() + ".*");
			for (String permission : allPermissions) {
				String permissionsCommand = "";
				permissionsCommand = permissionAccessor.getConfig().getString(
						"Permissions." + permission + ".command");
				try {
					if (permissionsCommand.equalsIgnoreCase("")) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				if(permissionsCommand.equalsIgnoreCase(commandName)) {
					return permission;
				}
			}
		}
		return null;
	}

}
