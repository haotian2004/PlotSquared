/*
 * PlotSquared, a land and world management plugin for Minecraft.
 * Copyright (C) IntellectualSites <https://intellectualsites.com>
 * Copyright (C) IntellectualSites team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.bukkit.listener;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.world.PlotAreaManager;
import com.plotsquared.core.plot.world.SinglePlotArea;
import com.plotsquared.core.plot.world.SinglePlotAreaManager;
import com.plotsquared.core.util.ReflectionUtils;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.plotsquared.core.util.ReflectionUtils.getRefClass;

public class SingleWorldListener implements Listener {

    private final Method methodGetHandleChunk;
    private Field shouldSave = null;

    public SingleWorldListener() throws Exception {
        ReflectionUtils.RefClass classCraftChunk = getRefClass("{cb}.CraftChunk");
        this.methodGetHandleChunk = classCraftChunk.getMethod("getHandle").getRealMethod();
        try {
            if (PlotSquared.platform().serverVersion()[1] < 17) {
                ReflectionUtils.RefClass classChunk = getRefClass("{nms}.Chunk");
                if (PlotSquared.platform().serverVersion()[1] == 13) {
                    this.shouldSave = classChunk.getField("mustSave").getRealField();
                } else {
                    this.shouldSave = classChunk.getField("s").getRealField();
                }
            } else if (PlotSquared.platform().serverVersion()[1] == 17) {
                ReflectionUtils.RefClass classChunk = getRefClass("net.minecraft.world.level.chunk.Chunk");
                this.shouldSave = classChunk.getField("r").getRealField();
            } else if (PlotSquared.platform().serverVersion()[1] == 18) {
                ReflectionUtils.RefClass classChunk = getRefClass("net.minecraft.world.level.chunk.IChunkAccess");
                this.shouldSave = classChunk.getField("b").getRealField();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void markChunkAsClean(Chunk chunk) {
        try {
            Object nmsChunk = methodGetHandleChunk.invoke(chunk);
            if (shouldSave != null) {
                this.shouldSave.set(nmsChunk, false);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void handle(ChunkEvent event) {
        World world = event.getWorld();
        String name = world.getName();
        PlotAreaManager man = PlotSquared.get().getPlotAreaManager();
        if (!(man instanceof SinglePlotAreaManager)) {
            return;
        }
        if (!SinglePlotArea.isSinglePlotWorld(name)) {
            return;
        }

        markChunkAsClean(event.getChunk());
    }

    //    @EventHandler
    //    public void onPopulate(ChunkPopulateEvent event) {
    //        handle(event);
    //    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        handle(event);
    }

}
