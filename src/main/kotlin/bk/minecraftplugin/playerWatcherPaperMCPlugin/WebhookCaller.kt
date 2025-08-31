package bk.minecraftplugin.playerWatcherPaperMCPlugin

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

enum class DiscordColors(val code: Int) {
    RED(hexToDecimal("#db393b")),
    GREEN(hexToDecimal("#6cc90e")),
    BLUE(hexToDecimal("#41a4d8")),
    LIGHT_BLUE(hexToDecimal("#33bfb8")),
    YELLOW(hexToDecimal("#fecc2f")),
    ORANGE(hexToDecimal("#f8a228")),
}

fun hexToDecimal(hexColor: String): Int {
    val cleanHex = hexColor.removePrefix("#")
    if (cleanHex.length != 6 || !cleanHex.all {
            it.isDigit() || it in 'a'..'f' || it in 'A'..'F'
        }) {
        throw IllegalArgumentException(
            "Invalid hex color string. Please use a 6-digit format like 'FF0000' or '#FF0000'."
        )
    }
    return cleanHex.toLong(16).toInt()
}

@Serializable
data class EmbeddedObject(val description: String, val color: Int)

@Serializable
data class Message(val username: String, val content: String? = null, val embeds: List<EmbeddedObject> = emptyList())

sealed interface PlayerEvent {
    object CONNECTED : PlayerEvent
    object DISCONNECTED : PlayerEvent
}

object WebhookCaller {

    const val BOT_NAME = "Player Watcher"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }

    // Not used for now
    private fun getMessageTextWithTag(playerName: String, event: PlayerEvent): String {
        return when (event) {
            PlayerEvent.CONNECTED -> "<@$playerName> has logged onto the server :wave:"
            PlayerEvent.DISCONNECTED -> "<@$playerName> has signed OUT of the server :people_hugging:"
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
        val others = allOnline.joinToString("\n") { "**$it**" }
        return if (others.isNotEmpty()) {
            "*Currently Online Players:*\n$others"
        } else {
            "*Currently Online Players:*\n:empty_nest:"
        }
    }

    suspend fun sendMessage(message: String, embedsColor: Int? = null) {
        try {
            if (embedsColor != null) {
                client.post(BuildConfig.WEBHOOK_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Message(
                            BOT_NAME,
                            embeds = listOf(
                                EmbeddedObject(
                                    color = embedsColor,
                                    description = message
                                )
                            )
                        )
                    )
                }
            } else {
                client.post(BuildConfig.WEBHOOK_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Message(
                            BOT_NAME,
                            content = message
                        )
                    )
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    suspend fun sendMessageAboutConnectionEventWithTags(
        playerName: String,
        event: PlayerEvent,
        currentOnline: List<String>
    ) {
        val playerId = UserMapper.getDiscordIdFromUsername(playerName)
        client.post(BuildConfig.WEBHOOK_URL) {
            contentType(ContentType.Application.Json)
            setBody(
                Message(
                    BOT_NAME,
                    "${getMessageTextWithTag(playerId, event)}\n${getOtherOnlinePlayersMessage(currentOnline)}"
                )
            )
        }
    }

    suspend fun sendMessageAboutConnectionEvent(playerName: String, event: PlayerEvent, currentOnline: List<String>) {
//        val playerId = UserMapper.getDiscordIdFromUsername(playerName)
//        client.post(BuildConfig.WEBHOOK_URL) {
//            contentType(ContentType.Application.Json)
//            setBody(
//                Message(
//                    BOT_NAME,
//                    wrapMessageInColor(
//                        "${getMessageText(playerName, event)}\n${getOtherOnlinePlayersMessage(currentOnline)}",
//                        DiscordColor.RED
//                    )
//                )
//            )
//        }
        sendMessage(
            "${getMessageText(playerName, event)}\n\n${getOtherOnlinePlayersMessage(currentOnline)}",
            embedsColor = if (event == PlayerEvent.CONNECTED) DiscordColors.GREEN.code else DiscordColors.RED.code
        )
    }
}
