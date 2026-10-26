/*
 * MIT License
 *
 * Copyright (c) 2020 Mark
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.andavin.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * A bridge between the plugin's scheduling utilities and the
 * scheduler implementation available on the running server.
 *
 * @author Andavin
 */
public interface SchedulerAdapter {

    BukkitTask sync(Plugin plugin, Runnable run);

    BukkitTask async(Plugin plugin, Runnable run);

    BukkitTask later(Plugin plugin, Runnable run, long delay);

    BukkitTask laterAsync(Plugin plugin, Runnable run, long delay);

    BukkitTask repeat(Plugin plugin, Runnable run, long delay, long period);

    BukkitTask repeatAsync(Plugin plugin, Runnable run, long delay, long period);

    BukkitTask entity(Plugin plugin, Entity entity, Runnable run, Runnable retired);

    BukkitTask entityLater(Plugin plugin, Entity entity, Runnable run, Runnable retired, long delay);

    BukkitTask entityRepeat(Plugin plugin, Entity entity, Runnable run,
                            Runnable retired, long delay, long period);

    BukkitTask region(Plugin plugin, Location location, Runnable run);

    BukkitTask regionLater(Plugin plugin, Location location, Runnable run, long delay);

    BukkitTask regionRepeat(Plugin plugin, Location location, Runnable run, long delay, long period);
}
