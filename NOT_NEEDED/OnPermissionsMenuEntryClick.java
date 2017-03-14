package me.Tiernanator.Permissions.PermissionsMenu.NOT_NEEDED;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.Tiernanator.Permissions.Main;

public class OnPermissionsMenuEntryClick implements Listener {

	private static Main plugin;
	
	public OnPermissionsMenuEntryClick(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		Plugin[] allPlugins = plugin.getServer().getPluginManager().getPlugins();
		
		Inventory inv = event.getInventory();
		if (!(inv.getTitle().equals(PermissionMenu.mainMenuName))) {
			boolean isMenu = false;
			for(Plugin plugin : allPlugins) {
				if(inv.getTitle().equalsIgnoreCase(plugin.getName() + "'s Permissions")) {
					isMenu = true;
					break;
				}
			}
			
			if(!isMenu) {
				return;
			}
		}			

		ItemStack item = event.getCurrentItem();
		
		if(item == null) {
			return;
		}
		
		if(!item.hasItemMeta()) {
			return;
		}
		ItemMeta itemMeta = item.getItemMeta();
		
		if(!itemMeta.hasDisplayName()) {
			return;
		}
		
		String itemName = itemMeta.getDisplayName();
		
		Player player = (Player) event.getWhoClicked();
		int currentPage = PermissionMenu.getPlayerPage(player);
		
		int size = allPlugins.length;

		int totalPages = (int) Math.ceil(size / 45) + 1;
		
		
		if (item.getType() == Material.WOOD_BUTTON && itemName.equalsIgnoreCase(ChatColor.RED + "Previous Page")) {
			
			
			if((currentPage - 1) <= 0) {
				
				event.setCancelled(true);
				return;
			
			} else {
				
				event.setCancelled(true);
				PermissionMenu.setPlayerPage(player, currentPage - 1);
				player.closeInventory();
				PermissionMenu.makeMenu(player);
				
			}
			return;
		}
		
		if (item.getType() == Material.STONE_BUTTON && itemName.equalsIgnoreCase(ChatColor.GREEN + "Next Page")) {

			if(currentPage >= totalPages) {
				
				event.setCancelled(true);
				return;
			
			} else {
				
				event.setCancelled(true);
				PermissionMenu.setPlayerPage(player, currentPage + 1);
				player.closeInventory();
				PermissionMenu.makeMenu(player);
				
			}
			return;
		
		}
		
		if (item.getType() == Material.REDSTONE_TORCH_ON && itemName.contains("Page")) {
			event.setCancelled(true);
			return;
		}
		
		if(!item.hasItemMeta()) {
			event.setCancelled(true);
			return;
		}
		
		Plugin clickedPlugin = null;

		for(Plugin plugin : allPlugins) {
			String pluginName = ChatColor.BLUE + "§L" + plugin.getName();
			if(itemName.equalsIgnoreCase(pluginName)) {
				clickedPlugin = plugin;
				break;
			}
		}

		if(clickedPlugin == null) {
			event.setCancelled(true);
			return;
		}
		
		player.closeInventory();
		PermissionMenu.makeSubMenu(player, clickedPlugin);
		
		event.setCancelled(true);

	}

}
