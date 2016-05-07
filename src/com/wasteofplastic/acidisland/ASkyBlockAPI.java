/*******************************************************************************
 * This file is part of ASkyBlock.
 *
 *     ASkyBlock is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ASkyBlock is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with ASkyBlock.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package com.wasteofplastic.acidisland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.wasteofplastic.acidisland.panels.SetBiome;

/**
 * Provides a programming interface
 * 
 * @author tastybento
 */
public class ASkyBlockAPI {
    private static ASkyBlockAPI instance = new ASkyBlockAPI(ASkyBlock.getPlugin());

    /**
     * @return the instance
     */
    public static ASkyBlockAPI getInstance() {
        return instance;
    }

    private ASkyBlock plugin;

    private ASkyBlockAPI(ASkyBlock plugin) {
        this.plugin = plugin;
    }

    /**
     * @param playerUUID
     * @return HashMap of all of the known challenges with a boolean marking
     *         them as complete (true) or incomplete (false). This is a copy of the challenges
     *         and changing this list will not affect the actual list.
     */
    public HashMap<String, Boolean> getChallengeStatus(UUID playerUUID) {
        return new HashMap<String, Boolean>(plugin.getPlayers().getChallengeStatus(playerUUID));
    }

    public Location getHomeLocation(UUID playerUUID) {
        return plugin.getPlayers().getHomeLocation(playerUUID,1);
    }

    /**
     * Returns the island level from the last time it was calculated. Note this
     * does not calculate the island level.
     * 
     * @param playerUUID
     * @return the last level calculated for the island or zero if none.
     */
    public int getIslandLevel(UUID playerUUID) {
        return plugin.getPlayers().getIslandLevel(playerUUID);
    }


    /**
     * Calculates the island level. Only the fast calc is supported.
     * The island calculation runs async and fires an IslandLevelEvent when completed
     * or use getIslandLevel(playerUUID). See https://gist.github.com/tastybento/e81d2403c03f2fe26642
     * for example code.
     * 
     * @param playerUUID
     * @return true if player has an island, false if not
     */
    public boolean calculateIslandLevel(UUID playerUUID) {
        if (plugin.getPlayers().inTeam(playerUUID) && !plugin.getPlayers().hasIsland(playerUUID)) {		
            new LevelCalcByChunk(plugin, playerUUID, null, false);
            return true;
        }
        return false;
    }

    /**
     * Provides the location of the player's island, either the team island or
     * their own
     * 
     * @param playerUUID
     * @return Location of island
     */
    public Location getIslandLocation(UUID playerUUID) {
        return plugin.getPlayers().getIslandLocation(playerUUID);
    }

    /**
     * Returns the owner of an island from the location.
     * Uses the grid lookup and is quick
     * 
     * @param location
     * @return UUID of owner
     */
    public UUID getOwner(Location location) {
        return plugin.getPlayers().getPlayerFromIslandLocation(location);
    }

    /**
     * Get Team Leader
     * 
     * @param playerUUID
     * @return UUID of Team Leader or null if there is none. Use inTeam to
     *         check.
     */
    public UUID getTeamLeader(UUID playerUUID) {
        return plugin.getPlayers().getTeamLeader(playerUUID);
    }

    /**
     * Get a list of team members. This is a copy and changing the return value
     * will not affect the membership.
     * 
     * @param playerUUID
     * @return List of team members, including the player. Empty if there are
     *         none.
     */
    public List<UUID> getTeamMembers(UUID playerUUID) {
        return new ArrayList<UUID>(plugin.getPlayers().getMembers(playerUUID));
    }

    /**
     * Provides location of the player's warp sign
     * 
     * @param playerUUID
     * @return Location of sign or null if one does not exist
     */
    public Location getWarp(UUID playerUUID) {
        return plugin.getWarpSignsListener().getWarp(playerUUID);
    }

    /**
     * Get the owner of the warp at location
     * 
     * @param location
     * @return Returns name of player or empty string if there is no warp at
     *         that spot
     */
    public String getWarpOwner(Location location) {
        return plugin.getWarpSignsListener().getWarpOwner(location);
    }

    /**
     * Status of island ownership. Team members do not have islands of their
     * own, only leaders do.
     * 
     * @param playerUUID
     * @return true if player has an island, false if the player does not.
     */
    public boolean hasIsland(UUID playerUUID) {
        return plugin.getPlayers().hasIsland(playerUUID);
    }

    /**
     * @param playerUUID
     * @return true if in a team
     */
    public boolean inTeam(UUID playerUUID) {
        return plugin.getPlayers().inTeam(playerUUID);
    }

    /**
     * Determines if an island is at a location in this area location. Also
     * checks if the spawn island is in this area. Checks for bedrock within
     * limits and also looks in the file system. Quite processor intensive.
     * 
     * @param location
     * @return true if there is an island in that location, false if not
     */
    public boolean islandAtLocation(Location location) {
        return plugin.getGrid().islandAtLocation(location);
    }

    /**
     * Checks to see if a player is trespassing on another player's island. Both
     * players must be online.
     * 
     * @param owner
     *            - owner or team member of an island
     * @param target
     * @return true if they are on the island otherwise false.
     */
    public boolean isOnIsland(Player owner, Player target) {
        return plugin.getGrid().isOnIsland(owner, target);
    }

    /**
     * Lists all the known warps. As each player can have only one warp, the
     * player's UUID is used. It can be displayed however you like to other
     * users. This is a copy of the set and changing it will not affect the
     * actual set of warps.
     * 
     * @return String set of warps
     */
    public Set<UUID> listWarps() {
        return new HashSet<UUID>(plugin.getWarpSignsListener().listWarps());
    }

    /**
     * Forces the warp panel to update and the warp list event to fire so that
     * the warps can be sorted how you like.
     */
    public void updateWarpPanel() {
        plugin.getWarpPanel().updatePanel();
    }

    /**
     * Checks if a specific location is within the protected range of an island
     * owned by the player
     * 
     * @param player
     * @param location
     * @return true if the location is on an island owner by player
     */
    public boolean locationIsOnIsland(final Player player, final Location location) {
        return plugin.getGrid().locationIsOnIsland(player, location);
    }

    /**
     * Finds out if location is within a set of island locations and returns the
     * one that is there or null if not. The islandTestLocations should be the center
     * location of an island. The check is done to see if loc is inside the protected
     * range of any of the islands given. 
     * 
     * @param islandTestLocations
     * @param loc
     * @return the island location that is in the set of locations or null if
     *         none
     */
    public Location locationIsOnIsland(final Set<Location> islandTestLocations, final Location loc) {
        return plugin.getGrid().locationIsOnIsland(islandTestLocations, loc);
    }

    /**
     * Checks if an online player is on their island, on a team island or on a
     * coop island
     * 
     * @param player
     *            - the player who is being checked
     * @return - true if they are on their island, otherwise false
     */
    public boolean playerIsOnIsland(Player player) {
        return plugin.getGrid().playerIsOnIsland(player);
    }

    /**
     * Sets all blocks in an island to a specified biome type
     * 
     * @param islandLoc
     * @param biomeType
     * @return true if the setting was successful
     */
    public boolean setIslandBiome(Location islandLoc, Biome biomeType) {
        Island island = plugin.getGrid().getIslandAt(islandLoc);
        if (island != null) {
            new SetBiome(plugin, island, biomeType);
            return true;
        }
        return false;
    }

    /**
     * Sets a message for the player to receive next time they login
     * 
     * @param playerUUID
     * @param message
     * @return true if player is offline, false if online
     */
    public boolean setMessage(UUID playerUUID, String message) {
        return plugin.getMessages().setMessage(playerUUID, message);
    }

    /**
     * Sends a message to every player in the team that is offline If the player
     * is not in a team, nothing happens.
     * 
     * @param playerUUID
     * @param message
     */
    public void tellOfflineTeam(UUID playerUUID, String message) {
        plugin.getMessages().tellOfflineTeam(playerUUID, message);
    }

    /**
     * Player is in a coop or not
     * 
     * @param player
     * @return true if player is in a coop, otherwise false
     */
    public boolean isCoop(Player player) {
        if (CoopPlay.getInstance().getCoopIslands(player).isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Find out which coop islands player is a part of
     * 
     * @param player
     * @return set of locations of islands or empty if none
     */
    public Set<Location> getCoopIslands(Player player) {
        return new HashSet<Location>(CoopPlay.getInstance().getCoopIslands(player));
    }

    /**
     * Provides spawn location
     * @return Location of spawn's central point
     */
    public Location getSpawnLocation() {
        return plugin.getGrid().getSpawn().getCenter();
    }

    /**
     * Provides the spawn range
     * @return spawn range
     */
    public int getSpawnRange() {
        return plugin.getGrid().getSpawn().getProtectionSize();
    }

    /**
     * Checks if a location is at spawn or not
     * @param location
     * @return true if at spawn
     */
    public boolean isAtSpawn(Location location) {
        return plugin.getGrid().isAtSpawn(location);
    }

    /**
     * Get the island overworld
     * @return the island overworld
     */
    public World getIslandWorld() {
        return ASkyBlock.getIslandWorld();
    }

    /**
     * Get the nether world
     * @return the nether world
     */
    public World getNetherWorld() {
        return ASkyBlock.getNetherWorld();
    }

    /**
     * Whether the new nether is being used or not
     * @return true if new nether is being used
     */
    public boolean isNewNether() {
        return Settings.newNether;
    }

    /**
     * Get the top ten list
     * @return Top ten list
     */
    public Map<UUID, Integer> getTopTen() {
        return new HashMap<UUID, Integer>(TopTen.getTopTenList());
    }

    /**
     * Obtains a copy of the island object owned by playerUUID
     * @param playerUUID
     * @return copy of Island object
     */
    public Island getIslandOwnedBy(UUID playerUUID) {
        return (Island)plugin.getGrid().getIsland(playerUUID).clone();
    }

    /**
     * Returns a copy of the Island object for an island at this location or null if one does not exist
     * @param location
     * @return copy of Island object
     */
    public Island getIslandAt(Location location) {
        return (Island)plugin.getGrid().getIslandAt(location);
    }

    /**
     * @return how many islands are in the world (that the plugin knows of)
     */
    public int getIslandCount() {
        return plugin.getGrid().getIslandCount();
    }

    /**
     * Get a copy of the ownership map of islands
     * @return Hashmap of owned islands with owner UUID as a key
     */
    public HashMap<UUID, Island> getOwnedIslands() {
        //System.out.println("DEBUG: getOwnedIslands");
        if (plugin.getGrid() != null) {
            HashMap<UUID, Island> islands = plugin.getGrid().getOwnedIslands();
            if (islands != null) {
                //plugin.getLogger().info("DEBUG: getOwnedIslands is not null");
                return new HashMap<UUID, Island>(islands);
            }
            //plugin.getLogger().info("DEBUG: getOwnedIslands is null");
        }
        return new HashMap<UUID, Island>();

    }
}
