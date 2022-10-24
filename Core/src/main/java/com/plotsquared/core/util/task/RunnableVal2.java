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
package com.plotsquared.core.util.task;

public abstract class RunnableVal2<T, U> implements Runnable {

    public T value1;
    public U value2;

    public RunnableVal2() {
    }

    public RunnableVal2(T value1, U value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public void run() {
        run(this.value1, this.value2);
    }

    public abstract void run(T value1, U value2);

}
