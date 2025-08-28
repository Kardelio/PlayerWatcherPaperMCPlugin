package bk.minecraftplugin.playerWatcherPaperMCPlugin

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
}

interface RemoteConfigCaller {
    suspend fun getCurrentConfig(forceUpdate: Boolean = false): RemoteConfig
    suspend fun getRemoteConfig(): RemoteConfig
    fun getStoredConfigOnly(): RemoteConfig?
}

class ConfigCaller() : RemoteConfigCaller {

    var storedConfig: RemoteConfig? = null
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override fun getStoredConfigOnly(): RemoteConfig? {
        return storedConfig
    }

    override suspend fun getCurrentConfig(forceUpdate: Boolean): RemoteConfig {
        if (storedConfig != null && !forceUpdate) {
            println("::::::::: Config was NOT null, returning as is...")
            return storedConfig!!
        } else {
            println("::::::::: Config was null or Forced, getting now")
            val updated = getRemoteConfig()
            storedConfig = updated
            return updated
        }
    }

    override suspend fun getRemoteConfig(): RemoteConfig {
        val response = client.get(BuildConfig.CONFIG_URL).body<String>()
        val foundConfigurations = mutableMapOf<String, Boolean>()
        response.split("\n").forEach {
            val (key, value) = it.split(",")
            val parsedBooleanValue = value[0] == '1'
            foundConfigurations[key] = parsedBooleanValue
        }
        return RemoteConfig(foundConfigurations)
    }

}