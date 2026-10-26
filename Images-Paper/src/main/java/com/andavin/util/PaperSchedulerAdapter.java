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

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

/**
 * A {@link SchedulerAdapter} that uses the Paper scheduler API.
 * <p>
 * This class is only loaded when the Paper scheduler API is present, which
 * allows the rest of the plugin to remain compatible with older servers.
 *
 * @author Andavin
 */
public final class PaperSchedulerAdapter implements SchedulerAdapter {

    private static final long MILLIS_PER_TICK = 50L;
    private final GlobalRegionScheduler global = Bukkit.getGlobalRegionScheduler();
    private final RegionScheduler region = Bukkit.getRegionScheduler();
    private final AsyncScheduler async = Bukkit.getAsyncScheduler();

    private static long tickDelay(long ticks) {
        // The Paper schedulers reject initial delays that are less than one tick
        return Math.max(1L, ticks);
    }

    private static long tickPeriod(long ticks) {
        return Math.max(1L, ticks);
    }

    private static long millisDelay(long ticks) {
        return Math.max(1L, ticks * MILLIS_PER_TICK);
    }

    private static BukkitTask wrap(ScheduledTask task, Plugin plugin, boolean sync) {
        return task != null ? new PaperSchedulerTask(task, plugin, sync) : null;
    }

    private static void retired(Runnable retired) {

        if (retired != null) {
            retired.run();
        }
    }

    @Override
    public BukkitTask sync(Plugin plugin, Runnable run) {
        return wrap(this.global.run(plugin, task -> run.run()), plugin, true);
    }

    @Override
    public BukkitTask async(Plugin plugin, Runnable run) {
        return wrap(this.async.runNow(plugin, task -> run.run()), plugin, false);
    }

    @Override
    public BukkitTask later(Plugin plugin, Runnable run, long delay) {
        if (delay <= 0) {
            return this.sync(plugin, run);
        }

        return wrap(this.global.runDelayed(plugin, task -> run.run(), tickDelay(delay)), plugin, true);
    }

    @Override
    public BukkitTask laterAsync(Plugin plugin, Runnable run, long delay) {
        if (delay <= 0) {
            return this.async(plugin, run);
        }

        return wrap(this.async.runDelayed(plugin, task -> run.run(),
                millisDelay(delay), TimeUnit.MILLISECONDS), plugin, false);
    }

    @Override
    public BukkitTask repeat(Plugin plugin, Runnable run, long delay, long period) {
        return wrap(this.global.runAtFixedRate(plugin, task -> run.run(),
                tickDelay(delay), tickPeriod(period)), plugin, true);
    }

    @Override
    public BukkitTask repeatAsync(Plugin plugin, Runnable run, long delay, long period) {
        return wrap(this.async.runAtFixedRate(plugin, task -> run.run(),
                millisDelay(delay), millisDelay(period), TimeUnit.MILLISECONDS), plugin, false);
    }

    @Override
    public BukkitTask entity(Plugin plugin, Entity entity, Runnable run, Runnable retired) {

        ScheduledTask task = entity.getScheduler().run(plugin, scheduled -> run.run(), () -> retired(retired));
        if (task == null) {
            retired(retired);
        }

        return wrap(task, plugin, true);
    }

    @Override
    public BukkitTask entityLater(Plugin plugin, Entity entity, Runnable run,
                                  Runnable retired, long delay) {

        ScheduledTask task = entity.getScheduler().runDelayed(plugin,
                scheduled -> run.run(), () -> retired(retired), tickDelay(delay));
        if (task == null) {
            retired(retired);
        }

        return wrap(task, plugin, true);
    }

    @Override
    public BukkitTask entityRepeat(Plugin plugin, Entity entity, Runnable run,
                                   Runnable retired, long delay, long period) {

        ScheduledTask task = entity.getScheduler().runAtFixedRate(plugin,
                scheduled -> run.run(), () -> retired(retired), tickDelay(delay), tickPeriod(period));
        if (task == null) {
            retired(retired);
        }

        return wrap(task, plugin, true);
    }

    @Override
    public BukkitTask region(Plugin plugin, Location location, Runnable run) {
        return wrap(this.region.run(plugin, location, task -> run.run()), plugin, true);
    }

    @Override
    public BukkitTask regionLater(Plugin plugin, Location location, Runnable run, long delay) {
        return wrap(this.region.runDelayed(plugin, location, task -> run.run(),
                tickDelay(delay)), plugin, true);
    }

    @Override
    public BukkitTask regionRepeat(Plugin plugin, Location location, Runnable run, long delay, long period) {
        return wrap(this.region.runAtFixedRate(plugin, location, task -> run.run(),
                tickDelay(delay), tickPeriod(period)), plugin, true);
    }
}
