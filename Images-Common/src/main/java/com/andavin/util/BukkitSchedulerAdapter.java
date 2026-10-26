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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * The fallback scheduler adapter for servers that do not provide
 * the Paper or Folia scheduler API.
 *
 * @author Andavin
 */
public final class BukkitSchedulerAdapter implements SchedulerAdapter {

    private static BukkitScheduler scheduler() {
        return Bukkit.getScheduler();
    }

    @Override
    public BukkitTask sync(Plugin plugin, Runnable run) {
        return scheduler().runTask(plugin, run);
    }

    @Override
    public BukkitTask async(Plugin plugin, Runnable run) {
        return scheduler().runTaskAsynchronously(plugin, run);
    }

    @Override
    public BukkitTask later(Plugin plugin, Runnable run, long delay) {
        return scheduler().runTaskLater(plugin, run, delay);
    }

    @Override
    public BukkitTask laterAsync(Plugin plugin, Runnable run, long delay) {
        return scheduler().runTaskLaterAsynchronously(plugin, run, delay);
    }

    @Override
    public BukkitTask repeat(Plugin plugin, Runnable run, long delay, long period) {
        return scheduler().runTaskTimer(plugin, run, delay, period);
    }

    @Override
    public BukkitTask repeatAsync(Plugin plugin, Runnable run, long delay, long period) {
        return scheduler().runTaskTimerAsynchronously(plugin, run, delay, period);
    }

    @Override
    public BukkitTask entity(Plugin plugin, Entity entity, Runnable run, Runnable retired) {
        return this.sync(plugin, run);
    }

    @Override
    public BukkitTask entityLater(Plugin plugin, Entity entity, Runnable run, Runnable retired, long delay) {
        return this.later(plugin, run, delay);
    }

    @Override
    public BukkitTask entityRepeat(Plugin plugin, Entity entity, Runnable run,
                                   Runnable retired, long delay, long period) {
        return this.repeat(plugin, run, delay, period);
    }

    @Override
    public BukkitTask region(Plugin plugin, Location location, Runnable run) {
        return this.sync(plugin, run);
    }

    @Override
    public BukkitTask regionLater(Plugin plugin, Location location, Runnable run, long delay) {
        return this.later(plugin, run, delay);
    }

    @Override
    public BukkitTask regionRepeat(Plugin plugin, Location location, Runnable run, long delay, long period) {
        return this.repeat(plugin, run, delay, period);
    }
}
