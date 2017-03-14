package me.Tiernanator.Permissions.Group;

import org.bukkit.event.Listener;

//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.entity.Player;
//import org.bukkit.event.Listener;
//
//import me.Tiernanator.Config.ConfigAccessor;
//import me.Tiernanator.Permissions.Main;
//
public class GroupsFromDisc implements Listener {
//
//	private static Main plugin;
//
//	public GroupsFromDisc(Main main) {
//		plugin = main;
//	}
//
//	public static void setPlugin(Main main) {
//		plugin = main;
//	}
//
//	public static String getPlayerGroup(Player player) {
//		ConfigAccessor userAccessor = new ConfigAccessor(plugin, "users.yml");
//
//		String playerUUID = player.getUniqueId().toString();
//
//		String playerGroup = userAccessor.getConfig()
//				.getString("Users." + playerUUID + ".group");
//		if(playerGroup == null) {
//			return null;
//		} else {
//			return playerGroup;
//		}
//		
//	}
//
//	public static void setPlayerGroup(Player player, Group group) {
//		
//		ConfigAccessor userAccessor = new ConfigAccessor(plugin, "users.yml");
//
//		String playerUUID = player.getUniqueId().toString();
//
//		String playerGroup = group.getName();
//
//		userAccessor.getConfig().set("Users." + playerUUID + ".group",
//				playerGroup);
//		userAccessor.getConfig().set("Users." + playerUUID + ".name",
//				player.getName());
//		userAccessor.saveConfig();
//	}
//
//	public static List<String> getGroupTypes() {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		return permissionAccessor.getConfig()
//				.getStringList("Groups.Types");
//	}
//	
//	public static boolean getGroupIsDefault(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		boolean isDefault = permissionAccessor.getConfig()
//				.getBoolean("Groups." + group.getName() + ".default");
//		return isDefault;
//	}
//	
//	public static void setGroupIsDefault(Group group, boolean isDefault) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig().set("Groups." + group.getName() + ".default", isDefault);
//		permissionAccessor.saveConfig();
//	}
//
//	public static String getGroupPrefix(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		String prefix = permissionAccessor.getConfig()
//				.getString("Groups." + group.getName() + ".prefix");
//
//		if (prefix.equals(null) || prefix.equals(" ")) {
//			prefix = "";
//		}
//
//		return prefix;
//	}
//	
//	public static void setGroupPrefix(Group group, String prefix) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig()
//				.set("Groups." + group.getName() + ".prefix", prefix);
//		permissionAccessor.saveConfig();
//	}
//
//	public static String getGroupSuffix(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		String suffix = permissionAccessor.getConfig()
//				.getString("Groups." + group.getName() + ".suffix");
//
//		if (suffix.equals(null) || suffix.equals(" ")) {
//			suffix = "";
//		}
//		return suffix;
//	}
//	
//	public static void setGroupSuffix(Group group, String suffix) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig()
//				.set("Groups." + group.getName() + ".suffix", suffix);
//		permissionAccessor.saveConfig();
//	}
//
//	public static boolean getGroupCanBuild(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		boolean canBuild = permissionAccessor.getConfig()
//				.getBoolean("Groups." + group.getName() + ".build");
//		return canBuild;
//	}
//	
//	public static void setGroupCanBuild(Group group, boolean canBuild) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig().set("Groups." + group.getName() + ".build", canBuild);
//		permissionAccessor.saveConfig();
//	}
//
//	public static boolean getGroupMembersOP(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		boolean membersOP = permissionAccessor.getConfig()
//				.getBoolean("Groups." + group.getName() + ".op");
//		return membersOP;
//	}
//	
//	public static void setGroupMembersOP(Group group, boolean membersOP) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig().set("Groups." + group.getName() + ".op", membersOP);
//		permissionAccessor.saveConfig();
//	}
//	
//	public static List<String> getGroupPermissions(Group group) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		List<String> permissions = permissionAccessor.getConfig()
//				.getStringList("Groups." + group.getName() + ".permissions");
//
//		return permissions;
//	}
//	
//	public static void setGroupPermissions(Group group, List<String> permissions) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		permissionAccessor.getConfig().set("Groups." + group.getName() + ".permissions", permissions);
//		permissionAccessor.saveConfig();
//	}
//
//	public static void addGroupPermissions(Group group, List<String> morePermissions) {
//
//		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
//				"permissions.yml");
//		List<String> permissions = new ArrayList<String>();
//		permissions = permissionAccessor.getConfig().getStringList("Groups." + group.getName() + ".permissions");
//		
//		for(String i : morePermissions) {
//			if(!(permissions.contains(i))) {
//				permissions.add(i);
//			}
//		}
//		
//		permissionAccessor.getConfig().set("Groups." + group.getName() + ".permissions", permissions);
//		permissionAccessor.saveConfig();
//	}
//	
}
