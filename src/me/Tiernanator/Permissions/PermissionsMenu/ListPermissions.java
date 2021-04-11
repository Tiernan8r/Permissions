package me.Tiernanator.Permissions.PermissionsMenu;

import me.Tiernanator.Permissions.PermissionsMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.File.ConfigAccessor;
import me.Tiernanator.Utilities.Items.ItemUtility;
import me.Tiernanator.Utilities.Menu.Menu;
import me.Tiernanator.Utilities.Menu.MenuAction;
import me.Tiernanator.Utilities.Menu.MenuEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ListPermissions implements CommandExecutor {
	
	private static ChatColor warning = Colour.WARNING.getColour();

	private PermissionsMain plugin;
	//sets the value of the plugin to the main class
	public ListPermissions(PermissionsMain main) {
		plugin = main;
	}
	
	//This Command sends a player a list of all their active Permissions
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(warning + "Only players can use this command.");
			return false;
		}
		Player player = (Player) sender;
 		
//		PermissionMenu.makeMenu(player);
		
		List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		
		Plugin[] allPlugins = plugin.getServer().getPluginManager().getPlugins();
		for(Plugin plugin : allPlugins) {
			
			ItemStack item = new ItemStack(Material.BEACON);
			
			String entryName = ChatColor.BLUE + "�L" + plugin.getName();
			
			List<MenuEntry> permissionEntries = new ArrayList<MenuEntry>();
			
			ConfigAccessor permissionAccessor = new ConfigAccessor(
					(JavaPlugin) plugin, "@permissions.yml");

			List<String> allPermissions = permissionAccessor.getConfig()
					.getStringList("Permissions." + plugin.getName() + ".*");
			for (String permission : allPermissions) {
				String description = "";
				description = permissionAccessor.getConfig().getString(
						"Permissions." + permission + ".description");
				
				ItemStack nameTag = new ItemStack(Material.NAME_TAG);
				ItemUtility.addLore(nameTag, ChatColor.DARK_PURPLE + description);
				
				String permissionName = ChatColor.YELLOW + permission;
				
				MenuEntry permissionEntry = new MenuEntry(permissionName, nameTag, MenuAction.NOTHING, null);
				permissionEntries.add(permissionEntry);
				
			}
			
			Menu permissionMenu = new Menu(plugin.getName() + "' Permissions", permissionEntries, 54, true);
			
			MenuEntry menuEntry = new MenuEntry(entryName, item, MenuAction.OPEN, permissionMenu);
			menuEntries.add(menuEntry);
		}
		
		Menu menu = new Menu("Permissions", menuEntries, 54, true);
		menu.makeMenu(player);
		
		return true;
	}
	
}
