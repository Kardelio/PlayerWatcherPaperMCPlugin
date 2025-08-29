package bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config

import bk.minecraftplugin.playerWatcherPaperMCPlugin.BuildConfig
import bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config.LocalConfig
import bk.minecraftplugin.playerWatcherPaperMCPlugin.utils.CurrentSystemTime
import bk.minecraftplugin.playerWatcherPaperMCPlugin.utils.CurrentSystemTimeImpl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

data class RemoteConfig(
    val config: Map<String, Boolean> = mutableMapOf()
) {
    fun printOutConfig(): String {
        return config.mapNotNull { (key, value) ->
            "${key} -> *${value}*"
        }.joinToString("\n")
    }

    fun getValue(key: String, defaultValue: Boolean = false): Boolean {
        return config[key] ?: defaultValue
    }
}

class NoStoredConfigException(message: String) : Exception(message)

interface RemoteConfigCaller {
    suspend fun getCurrentConfig(forceUpdate: Boolean = false): RemoteConfig
    fun getStoredConfigOnly(): RemoteConfig?
}

class RemoteConfigCallerImpl(val localConfig: LocalConfig, val currentSystemTime: CurrentSystemTime = CurrentSystemTimeImpl()) : RemoteConfigCaller {

    companion object Companion {
        const val TIME_TILL_STALE_MILLIS = (60 * 60 * 1000).toLong() //3600000

        //(60 * 60 * 1000).toLong()
        const val LAST_TIME_REQUESTED_KEY = "last_time_requested_key"
//        60000 = 1 min
//        3600000 = 1 hour
    }

    var storedConfig: RemoteConfig? = null
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private fun storeLastTimeRequested() {
        localConfig.saveLong(LAST_TIME_REQUESTED_KEY, currentSystemTime.getCurrentSystemTimeMillis())
    }

    private fun getLastTimeRequested(): Long {
        return localConfig.getAsLong(LAST_TIME_REQUESTED_KEY)
    }

    private fun isLastTimeRequestedStale(): Boolean {
        val currentTimeMillis = currentSystemTime.getCurrentSystemTimeMillis()
        val stale = currentTimeMillis - getLastTimeRequested() >= TIME_TILL_STALE_MILLIS
        if (stale) {
            println(" ==> isLastTimeRequestedStale IS STALE!")
        }
        return stale
    }

    override fun getStoredConfigOnly(): RemoteConfig? {
        return storedConfig
    }

    override suspend fun getCurrentConfig(forceUpdate: Boolean): RemoteConfig {
        if (storedConfig != null && !forceUpdate && !isLastTimeRequestedStale()) {
            println("::::::::: Config was NOT null, returning as is...")
            return storedConfig!!
        } else {
            println("::::::::: Config was null or Forced, getting now")
            val updated = getRemoteConfig()
            storedConfig = updated
            return updated
        }
    }

    private suspend fun getRemoteConfig(): RemoteConfig {
        try {
            val response = client.get(BuildConfig.CONFIG_URL).body<String>()
            val foundConfigurations = mutableMapOf<String, Boolean>()
            response.split("\n").forEach {
                val (key, value) = it.split(",")
                val parsedBooleanValue = value[0] == '1'
                foundConfigurations[key] = parsedBooleanValue
            }
            return RemoteConfig(foundConfigurations)

        } catch (e: Exception) {
            println(" ERR: ${e.message} (getRemoteConfig)")
            return storedConfig ?: throw NoStoredConfigException("No Stored config")
        }
    }

}