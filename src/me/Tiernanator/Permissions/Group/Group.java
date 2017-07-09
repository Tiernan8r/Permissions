package me.Tiernanator.Permissions.Group;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.File.ConfigAccessor;
import me.Tiernanator.Permissions.PermissionsMain;

public class Group {
	
	private static PermissionsMain plugin;
	public static void setPlugin(PermissionsMain main) {
		plugin = main;
	}
	
	private static List<Group> allGroups = new ArrayList<Group>();

	public static List<Group> allGroups() {
		return allGroups;
	}

	private static void addGroup(Group group) {
		if (!groupExists(group)) {
			allGroups.add(group);
		}
	}

	public static void removeGroup(Group group) {
		
		if (groupExists(group)) {
			allGroups.remove(group);
		}
	}

	public static boolean groupExists(Group group) {
		return allGroups().contains(group);
	}

	public static Group getGroup(String groupName) {
		for (Group group : allGroups()) {
			if (group.getName().equalsIgnoreCase(groupName)) {
				return group;
			}
		}
		return null;
	}

	private String name;
	private String prefix;
	private String suffix;
	private boolean canBuild;
	private boolean membersOP;
	private boolean isDefault;
	private List<String> permissions;

	public Group(String name, boolean isDefault, String prefix, String suffix, boolean canBuild, boolean membersOP, List<String> permissions) {
		
		this.name = name;
		this.isDefault = isDefault;
		this.prefix = prefix;
		this.suffix = suffix;
		this.canBuild = canBuild;
		this.membersOP = membersOP;
		this.permissions = permissions;

	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig()
				.set("Groups." + this.getName() + ".prefix", prefix);
		permissionAccessor.saveConfig();
	}
	
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig()
				.set("Groups." + this.getName() + ".suffix", suffix);
		permissionAccessor.saveConfig();
	}
	
	public boolean canBuild() {
		return this.canBuild;
	}
	
	public void setCanBuild(boolean canBuild) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig().set("Groups." + this.getName() + ".build", canBuild);
		permissionAccessor.saveConfig();
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}
	
	public void setIsDefault(boolean isDefault) {
		
		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig().set("Groups." + this.getName() + ".default", isDefault);
		permissionAccessor.saveConfig();
	}
	
	public boolean membersOP() {
		return this.membersOP;
	}

	public void setMembersOP(boolean membersOP) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig().set("Groups." + this.getName() + ".op", membersOP);
		permissionAccessor.saveConfig();
	}
	
	public List<String> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<String> permissions) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		permissionAccessor.getConfig().set("Groups." + this.getName() + ".permissions", permissions);
		permissionAccessor.saveConfig();
	}

	public void addPermissions(List<String> morePermissions) {

		for(String permission : morePermissions) {
			addPermission(permission);
		}
		
	}
	
	public void addPermission(String extraPermission) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		List<String> permissions = new ArrayList<String>();
		permissions = permissionAccessor.getConfig().getStringList("Groups." + this.getName() + ".permissions");
		
		if(!permissions.contains(extraPermission)) {
			permissions.add(extraPermission);
		}
		
		permissionAccessor.getConfig().set("Groups." + this.getName() + ".permissions", permissions);
		permissionAccessor.saveConfig();
	}
	
	public static List<String> getGroupTypes() {

		ConfigAccessor permissionAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		return permissionAccessor.getConfig()
				.getStringList("Groups.Types");
	}
	
	public static void initialiseGroups(JavaPlugin plugin) {

		ConfigAccessor groupAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		List<String> allGroupNames = groupAccessor.getConfig()
				.getStringList("Groups.Types");
		for (String groupName : allGroupNames) {

			String name = groupName;
			String prefix = groupAccessor.getConfig()
					.getString("Groups." + groupName + ".prefix");
			String suffix = groupAccessor.getConfig()
					.getString("Groups." + groupName + ".suffix");
			boolean isDefault = groupAccessor.getConfig()
					.getBoolean("Groups." + groupName + ".default");
			boolean membersOP = groupAccessor.getConfig()
					.getBoolean("Groups." + groupName + ".op");
			boolean canBuild = groupAccessor.getConfig()
					.getBoolean("Groups." + groupName + ".build");
			List<String> permissions = groupAccessor.getConfig()
					.getStringList("Groups." + groupName + ".permissions");

			Group group = new Group(name, isDefault, prefix, suffix, canBuild, membersOP, permissions);
			addGroup(group);
			
			
		}
	}

	public static void saveGroups(JavaPlugin plugin) {

		ConfigAccessor groupAccessor = new ConfigAccessor(plugin,
				"permissions.yml");
		
		List<String> allGroupNames = new ArrayList<String>();
		for(Group group : allGroups()) {
			allGroupNames.add(group.getName());
		}
		groupAccessor.getConfig().set("Groups.Types", allGroupNames);
		
		for (Group group : allGroups()) {

			String name = group.getName();
			
			List<String> permissions = group.getPermissions();
			groupAccessor.getConfig().set("Groups." + name + ".permissions", permissions);

		}
		groupAccessor.saveConfig();
	}
	
}
