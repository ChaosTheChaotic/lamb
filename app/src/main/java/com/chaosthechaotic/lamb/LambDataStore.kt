package com.chaosthechaotic.lamb

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "lamb_user_prefs")

object LambDataStore {

    class PrefItem<T>(val key: Preferences.Key<T>, val default: T) {
        fun getVal(context: Context): Flow<T> {
            return context.userDataStore.data.map { preferences -> preferences[key] ?: default }
        }

        suspend fun setVal(context: Context, value: T) {
            context.userDataStore.edit { preferences -> preferences[key] = value }
        }
    }

    val pollCroc = PrefItem(
        key = booleanPreferencesKey("poll_croc"),
        default = false
    )
}