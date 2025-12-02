package com.chaosthechaotic.lamb

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PrefHelper(private val ctx: Context) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var jobs = mutableMapOf<String, Job>()

    fun <T> getValBlocking(item: LambDataStore.PrefItem<T>): T {
        return runBlocking { item.getVal(ctx).first() }
    }

    fun <T> getValueAsync(item: LambDataStore.PrefItem<T>, callback: (T) -> Unit) {
        val job = scope.launch {
            val value = item.getVal(ctx).first()
            callback(value)
        }
        jobs[item.key.name] = job
    }

    fun cancelAsyncOp(item: LambDataStore.PrefItem<*>) {
        jobs[item.key.name]?.cancel()
        jobs.remove(item.key.name)
    }

    fun cancelAllAsyncOps() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }

    fun <T> setValAutoCo(prefItem: LambDataStore.PrefItem<T>, value: T) {
        scope.launch {
            prefItem.setVal(ctx, value)
        }
    }
}

fun <T> LambDataStore.PrefItem<T>.getValBlocking(context: Context): T {
    return PrefHelper(context).getValBlocking(this)
}

fun <T> LambDataStore.PrefItem<T>.getValAsync(context: Context, callback: (T) -> Unit) {
    PrefHelper(context).getValueAsync(this, callback)
}

fun <T> LambDataStore.PrefItem<T>.setValAutoCo(context: Context, value: T) {
    PrefHelper(context).setValAutoCo(this, value)
}