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

package com.wasteofplastic.acidisland.events;

import java.util.UUID;

import com.wasteofplastic.acidisland.Island;

/**
 * This event is fired when an island level is calculated
 * 
 * @author tastybento
 * 
 */
public class IslandLevelEvent extends ASkyBlockEvent {
    private int level;

    /**
     * @param player
     * @param island
     * @param level
     */
    public IslandLevelEvent(UUID player, Island island, int level) {
        super(player, island);
        this.level = level;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

}
