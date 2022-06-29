package com.jacyzhou.testjacyzhou.service

import java.util.concurrent.LinkedBlockingQueue

object TaskManager {
    private const val TAG = "TaskManager"

    // 使用并发容器LinkedBlockingQueue
    private val taskQueue: LinkedBlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>()

    private var isStart: Boolean = false

    // 添加任务到任务队列
    fun addTask(task: Runnable): Boolean {
        if (taskQueue.size > 2) {
            return false
        }
        return taskQueue.offer(task)
    }

    fun start() {
        if (isStart) {
            return
        }
        isStart = true
        Thread {
            while (true) {
                // 如果取不到将一直阻塞，不占用cpu
                val task = taskQueue.take()
                task.run()
            }
        }.start()
    }


}