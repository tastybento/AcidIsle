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

package com.wasteofplastic.acidisland.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.wasteofplastic.acidisland.ASkyBlock;
import com.wasteofplastic.acidisland.Settings;

public class WorldLoader implements Listener {
    //private ASkyBlock plugin;
    private boolean worldLoaded = false;

    /**
     * Class to force world loading before plugins.
     * @param aSkyBlock
     */
    public WorldLoader(ASkyBlock aSkyBlock) {
        //this.plugin = aSkyBlock;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (worldLoaded) {
            return;
        }
        //plugin.getLogger().info("DEBUG: " + event.getWorld().getName());
        if (event.getWorld().getName().equals(Settings.worldName) || event.getWorld().getName().equals(Settings.worldName + "_nether")) {
            return;
        }
        // Load the world
        worldLoaded = true;
        ASkyBlock.getIslandWorld();
    }
}
