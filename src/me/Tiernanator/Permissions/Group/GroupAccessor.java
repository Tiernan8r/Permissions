package me.Tiernanator.Permissions.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Permissions.Main;
import me.Tiernanator.Permissions.Permission;
import me.Tiernanator.Permissions.Events.CustomEvents.CustomGroupChangeEvent;

public class GroupAccessor {

	private static Main plugin;
	public static void setPlugin(Main main) {
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

		Connection connection = Main.getSQL().getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		ResultSet resultSet = null;
		try {
//			resultSet = statement.executeQuery(query);
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String groupName = null;
		try {
			groupName = resultSet.getString("PlayerGroup");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return groupName;
		// String playerUUID = player.getUniqueId().toString();
		//
		// String query = "SELECT PlayerGroup FROM PermissionUsers WHERE UUID =
		// '" + playerUUID + "';";
		//
		// Connection connection = Main.getSQL().getConnection();
		// Statement statement = null;
		// try {
		// statement = connection.createStatement();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// ResultSet resultSet = null;
		// try {
		// resultSet = statement.executeQuery(query);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//
		// try {
		// if (!resultSet.isBeforeFirst()) {
		// return null;
		// }
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		// try {
		// resultSet.next();
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		//
		// String groupName = null;
		// try {
		// groupName = resultSet.getString("PlayerGroup");
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// return groupName;

	}

	public Group getPlayerGroup() {
		String groupName = getPlayerGroupName();
		Group group = Group.getGroup(groupName);
		return group;
	}

	public void setPlayerGroup(Group group) {

		String playerUUID = getPlayer().getUniqueId().toString();
		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {
				if (!hasPlayerGroup()) {

					Connection connection = Main.getSQL().getConnection();
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection.prepareStatement(
								"INSERT INTO PermissionUsers (UUID, PlayerGroup) VALUES (?, ?);");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.setString(1, playerUUID);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.setString(2, group.getName());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {

					String query = "UPDATE PermissionUsers SET PlayerGroup = '"
							+ group.getName() + "' WHERE UUID = '" + playerUUID
							+ "';";

					Connection connection = Main.getSQL().getConnection();
					Statement statement = null;
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
				}
			}
		};
		runnable.runTaskAsynchronously(plugin);
		// String playerUUID = player.getUniqueId().toString();
		//
		// if(!hasPlayerGroup(player)) {
		//
		// Connection connection = Main.getSQL().getConnection();
		// PreparedStatement preparedStatement = null;
		// try {
		// preparedStatement = connection.prepareStatement(
		// "INSERT INTO PermissionUsers (UUID, PlayerGroup) VALUES (?, ?);");
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// preparedStatement.setString(1, playerUUID);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// preparedStatement.setString(2, group.getName());
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// preparedStatement.executeUpdate();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// } else {
		// String query = "UPDATE PermissionUsers SET PlayerGroup = '"
		// + group.getName() + "' WHERE UUID = '" + playerUUID + "';";
		//
		// Connection connection = Main.getSQLConnection();
		// Statement statement = null;
		// try {
		// statement = connection.createStatement();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// statement.execute(query);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
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

	private boolean hasPlayerGroup = false;
	public boolean hasPlayerGroup() {

		String playerUUID = getPlayer().getUniqueId().toString();

		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {

				String query = "SELECT * FROM PermissionUsers WHERE UUID = '"
						+ playerUUID + "';";

				Connection connection = Main.getSQL().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ResultSet resultSet = null;
				try {
					resultSet = statement.executeQuery(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (!resultSet.isBeforeFirst()) {
						// return false;
						GroupAccessor.this.hasPlayerGroup = false;
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					resultSet.next();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				String hasResult = null;
				try {
					hasResult = resultSet.getString("PlayerGroup");
				} catch (SQLException e) {
					e.printStackTrace();
				}

				boolean hasValue = !(hasResult == null);
				GroupAccessor.this.hasPlayerGroup = hasValue;
				// if(hasResult == null) {
				// return false;
				// } else {
				// return true;
				// }

			}
		};
		runnable.runTaskAsynchronously(plugin);
		return GroupAccessor.this.hasPlayerGroup;
		// String query = "SELECT * FROM PermissionUsers WHERE UUID = '" +
		// playerUUID + "';";
		//
		// Connection connection = Main.getSQL().getConnection();
		// Statement statement = null;
		// try {
		// statement = connection.createStatement();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// ResultSet resultSet = null;
		// try {
		// resultSet = statement.executeQuery(query);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// if (!resultSet.isBeforeFirst()) {
		// return false;
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// resultSet.next();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//
		// String hasResult = null;
		// try {
		// hasResult = resultSet.getString("PlayerGroup");
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		//
		// if(hasResult == null) {
		// return false;
		// } else {
		// return true;
		// }

	}

}
