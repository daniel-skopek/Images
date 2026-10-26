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

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An adapter from a Paper {@link ScheduledTask} to a {@link BukkitTask}
 * so that the existing scheduler utility API remains unchanged.
 *
 * @author Andavin
 */
final class PaperSchedulerTask implements BukkitTask {

    private static final AtomicInteger TASK_IDS = new AtomicInteger();
    private final ScheduledTask task;
    private final Plugin plugin;
    private final boolean sync;
    private final int id = TASK_IDS.getAndIncrement();

    PaperSchedulerTask(ScheduledTask task, Plugin plugin, boolean sync) {
        this.task = task;
        this.plugin = plugin;
        this.sync = sync;
    }

    @Override
    public int getTaskId() {
        return this.id;
    }

    @Override
    public Plugin getOwner() {
        return this.plugin;
    }

    @Override
    public boolean isSync() {
        return this.sync;
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    /**
     * Kept for compatibility with older Bukkit API versions.
     *
     * @return The plugin that owns this task.
     */
    @SuppressWarnings("deprecation")
    public Plugin getOwningPlugin() {
        return this.plugin;
    }
}
