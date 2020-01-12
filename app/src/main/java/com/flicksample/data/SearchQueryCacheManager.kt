package com.flicksample.data

import android.content.Context
import android.content.SharedPreferences

object SearchQueryCacheManager {

    private const val SETTINGS_STORAGE_NAME = "sample"
    private const val QUERY = "queries"
    private lateinit var sharedPreferences: SharedPreferences
    private var cachedQuery: Set<String>? = null

    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SETTINGS_STORAGE_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefs(): SharedPreferences {
        return sharedPreferences
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun clearAll() {
        cachedQuery = emptySet()
        getEditor().clear().apply()
    }

    fun getQuery(): Set<String> {
        if (cachedQuery == null) {
            readQuery()
        }
        return cachedQuery!!
    }

    fun addQuery(query: String) {
        if (cachedQuery == null) {
            cachedQuery = mutableSetOf()
        }
        cachedQuery = cachedQuery!!.plus(query)
    }

    fun saveQuery() {
        getEditor().putStringSet(QUERY, cachedQuery).commit()
    }

    fun readQuery() {
        cachedQuery = getPrefs().getStringSet(QUERY, emptySet())
    }
}