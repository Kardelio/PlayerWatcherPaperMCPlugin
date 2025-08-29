package bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config

import bk.minecraftplugin.playerWatcherPaperMCPlugin.PlayerWatcherPaperMCPlugin

interface LocalConfig {
    fun saveString(key: String, value: String)
    fun saveLong(key: String, value: Long)

    fun containsValue(key: String): Boolean
    fun getAsString(key: String, defaultValue: String = ""): String
    fun getAsLong(key: String, defaultValue: Long = 0L): Long
}

class LocalConfigImpl(val plugin: PlayerWatcherPaperMCPlugin) : LocalConfig {

    init {
        plugin.config.options().copyDefaults()
        plugin.saveDefaultConfig()
    }

    override fun getAsString(key: String, defaultValue: String): String {
        return plugin.config.getString(key) ?: defaultValue
    }

    override fun containsValue(key: String): Boolean {
        return plugin.config.contains(key)
    }

    override fun saveString(key: String, value: String) {
        plugin.config.set(key, value)
        plugin.saveConfig()
    }

    override fun saveLong(key: String, value: Long) {
        plugin.config.set(key, value)
        plugin.saveConfig()
    }

    override fun getAsLong(key: String, defaultValue: Long): Long {
        return plugin.config.getLong(key, defaultValue)
    }
}