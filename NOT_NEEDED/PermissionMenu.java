package me.Tiernanator.Permissions.PermissionsMenu.NOT_NEEDED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Config.ConfigAccessor;
import me.Tiernanator.Permissions.Main;
import me.Tiernanator.Utilities.Items.Item;


public class PermissionMenu {

	public static String mainMenuName = "Permissions";

	public static HashMap<Player, Integer> pageNumber = new HashMap<Player, Integer>();

	public static boolean hasMenu(Player player) {
		return pageNumber.containsKey(player);
	}

	public static int getPlayerPage(Player player) {

		if(!hasMenu(player)) {
			return 1;
		} else {
			return pageNumber.get(player);
		}

	}

	public static void setPlayerPage(Player player, int number) {

		if(hasMenu(player)) {
			pageNumber.remove(player);
		}
		pageNumber.put(player, number);
	}

	private static Main plugin;
	
	public static void setPlugin(Main main) {
		plugin = main;
	}
	
	
	public static void makeMenu(Player player) {

		Plugin[] allPlugins = plugin.getServer().getPluginManager().getPlugins();

		int size = allPlugins.length;

		int totalPages = (int) Math.ceil(size / 45) + 1;

		int inventorySize = 54;
		Inventory inventory = Bukkit.createInventory(null, inventorySize,
				mainMenuName);

		int pageNumber = getPlayerPage(player);

		if(pageNumber > totalPages) {
			return;
		}
		
		ItemStack redstoneTorch = new ItemStack(Material.REDSTONE_TORCH_ON);
		Item.renameItem(redstoneTorch,
				ChatColor.AQUA + "Page " + Integer.toString(pageNumber) + "/"
						+ Integer.toString(totalPages));
		ItemStack stoneButton = new ItemStack(Material.STONE_BUTTON);
		Item.renameItem(stoneButton, ChatColor.GREEN + "Next Page");
		ItemStack woodButton = new ItemStack(Material.WOOD_BUTTON);
		Item.renameItem(woodButton, ChatColor.RED + "Previous Page");

		inventory.setItem(3, woodButton);
		inventory.setItem(4, redstoneTorch);
		inventory.setItem(5, stoneButton);

		List<Plugin> thisPagesEntries = new ArrayList<Plugin>();
		int entryNumber = (pageNumber - 1) * 45;
		for (int i = entryNumber; i < entryNumber + 45; i++) {
			try {
				thisPagesEntries.add(allPlugins[i]);
			} catch (Exception e) {
				continue;
			}
		}

		for (int i = 0; i < 45; i++) {

			try {
				thisPagesEntries.get(i);
			} catch (Exception e) {
				continue;
			}
			
			ItemStack item = new ItemStack(Material.BEACON);
			
			Item.renameItem(item, ChatColor.BLUE + "§L" + thisPagesEntries.get(i).getName());
			
			inventory.setItem(i + 9, item);
		}
		player.openInventory(inventory);

	}
	
	public static void makeSubMenu(Player player, Plugin plugin) {

		ConfigAccessor permissionAccessor = new ConfigAccessor(
				(JavaPlugin) plugin, "@permissions.yml");

		List<String> allPermissions = permissionAccessor.getConfig()
				.getStringList("Permissions." + plugin.getName() + ".*");
		for (String permission : allPermissions) {
			String description = "";
			description = permissionAccessor.getConfig().getString(
					"Permissions." + permission + ".description");
			try {
				if (description.equalsIgnoreCase("")) {
					continue;
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		int size = allPermissions.size();

		int totalPages = (int) Math.ceil(size / 45) + 1;

		int inventorySize = 54;
		Inventory inventory = Bukkit.createInventory(null, inventorySize,
				plugin.getName() + "'s Permissions");

		int pageNumber = getPlayerPage(player);

		if(pageNumber > totalPages) {
			return;
		}
		
		ItemStack redstoneTorch = new ItemStack(Material.REDSTONE_TORCH_ON);
		Item.renameItem(redstoneTorch,
				ChatColor.AQUA + "Page " + Integer.toString(pageNumber) + "/"
						+ Integer.toString(totalPages));
		ItemStack stoneButton = new ItemStack(Material.STONE_BUTTON);
		Item.renameItem(stoneButton, ChatColor.GREEN + "Next Page");
		ItemStack woodButton = new ItemStack(Material.WOOD_BUTTON);
		Item.renameItem(woodButton, ChatColor.RED + "Previous Page");

		inventory.setItem(3, woodButton);
		inventory.setItem(4, redstoneTorch);
		inventory.setItem(5, stoneButton);

		List<String> thisPagesEntries = new ArrayList<String>();
		int entryNumber = (pageNumber - 1) * 45;
		for (int i = entryNumber; i < entryNumber + 45; i++) {
			try {
				thisPagesEntries.add(allPermissions.get(i));
			} catch (Exception e) {
				continue;
			}
		}

		for (int i = 0; i < 45; i++) {

			try {
				thisPagesEntries.get(i);
			} catch (Exception e) {
				continue;
			}
			
			ItemStack item = new ItemStack(Material.NAME_TAG);
			
			Item.renameItem(item, ChatColor.YELLOW + thisPagesEntries.get(i));
			
			inventory.setItem(i + 9, item);
		}
		player.openInventory(inventory);

	}
	
}
