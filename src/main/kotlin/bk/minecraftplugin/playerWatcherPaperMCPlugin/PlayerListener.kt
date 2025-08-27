package bk.minecraftplugin.playerWatcherPaperMCPlugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.logging.Logger

class PlayerListener(val scope: CoroutineScope, val logger: Logger, val currentOnlinePlayers: CurrentOnlinePlayers) :
    Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendMessage("Welcome to the server ${player.name}")
        if (currentOnlinePlayers.addToList(player.name)) {
            scope.launch {
                try {
                    WebhookCaller.sendMessage(player.name, PlayerEvent.CONNECTED, currentOnlinePlayers.getCurrentList())
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
                    WebhookCaller.sendMessage(
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