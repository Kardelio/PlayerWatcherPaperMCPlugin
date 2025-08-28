package bk.minecraftplugin.playerWatcherPaperMCPlugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.logging.Logger

class PlayerListener(
    val scope: CoroutineScope,
    val logger: Logger,
    val currentOnlinePlayers: CurrentOnlinePlayers,
    val configCaller: ConfigCaller
) :
    Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        scope.launch {
            configCaller.getCurrentConfig(true)
            val player = event.player
            player.sendMessage("Welcome to the server ${player.name}")
            if (currentOnlinePlayers.addToList(player.name)) {
                try {
                    WebhookCaller.sendMessageAboutConnectionEvent(
                        player.name,
                        PlayerEvent.CONNECTED,
                        currentOnlinePlayers.getCurrentList()
                    )
                } catch (e: Exception) {
                    logger.warning(e.message)
                }
            }
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        scope.launch {
            val nowConfig = configCaller.getCurrentConfig(true)
            if (nowConfig.config[RemoteConfigKeys.DEATH_POST] == true) {
                val player = event.player
                val location = event.player.location
                try {
                    WebhookCaller.sendMessage("${player.name} died at X: ${location.x.toInt()}, Y: ${location.y.toInt()}, Z: ${location.z.toInt()}")
                } catch (e: Exception) {
                    logger.warning(e.message)
                }
            }
        }
    }


    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        if (currentOnlinePlayers.removeFromList(player.name)) {
            scope.launch {
                try {
                    WebhookCaller.sendMessageAboutConnectionEvent(
                        player.name,
                        PlayerEvent.DISCONNECTED,
                        currentOnlinePlayers.getCurrentList()
                    )
                } catch (e: Exception) {
                    logger.warning(e.message)
                }
            }
        }
    }

}