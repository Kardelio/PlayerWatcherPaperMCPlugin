package bk.minecraftplugin.playerWatcherPaperMCPlugin

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(val username: String, val content: String)

sealed interface PlayerEvent {
    object CONNECTED : PlayerEvent
    object DISCONNECTED : PlayerEvent
}

object WebhookCaller {

    const val BOT_NAME = "Player Watcher"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    //Not used for now
    private fun getMessageTextWithTag(playerName: String, event: PlayerEvent): String {
        return when (event) {
            PlayerEvent.CONNECTED -> "<@${playerName}> has logged onto the server :wave:"
            PlayerEvent.DISCONNECTED -> "<@${playerName}> has signed OUT of the server :people_hugging:"
        }
    }
    private fun getMessageText(playerName: String, event: PlayerEvent): String {
        return when (event) {
            PlayerEvent.CONNECTED -> "**$playerName** has logged onto the server :wave:"
            PlayerEvent.DISCONNECTED -> "**$playerName** has signed OUT of the server :people_hugging:"
        }
    }

    private fun getOtherOnlinePlayersMessage(allOnline: List<String>): String {
//        val others = allOnline.filter { it != playerName }.joinToString("\n") { "**${it}**" }
        val others = allOnline.joinToString("\n") { "**${it}**" }
        return if (others.isNotEmpty()) {
            "*Currently Online Players:*\n$others"
        } else {
            "*Currently Online Players:*\n:empty_nest:"
        }
    }

    suspend fun sendMessage(message: String){
        client.post(BuildConfig.WEBHOOK_URL) {
            contentType(ContentType.Application.Json)
            setBody(
                Message(
                    BOT_NAME,
                    message
                )
            )
        }
    }

    suspend fun sendMessageAboutConnectionEvent(playerName: String, event: PlayerEvent, currentOnline: List<String>) {
//        val playerId = UserMapper.getDiscordIdFromUsername(playerName)
        client.post(BuildConfig.WEBHOOK_URL) {
            contentType(ContentType.Application.Json)
            setBody(
                Message(
                    BOT_NAME,
                    "${getMessageText(playerName, event)}\n${getOtherOnlinePlayersMessage( currentOnline)}"
                )
            )
        }
    }
}