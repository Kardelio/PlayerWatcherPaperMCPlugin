package bk.minecraftplugin.playerWatcherPaperMCPlugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.bukkit.plugin.java.JavaPlugin

class PlayerWatcherPaperMCPlugin : JavaPlugin() {

    private lateinit var pluginScope: CoroutineScope
    private lateinit var currentOnlinePlayers: CurrentOnlinePlayers

    override fun onEnable() {
        pluginScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        logger.info("==> Player Watcher starting up")

        currentOnlinePlayers = CurrentOnlinePlayers()

        server.pluginManager.registerEvents(PlayerListener(pluginScope, logger, currentOnlinePlayers), this)
    }

    override fun onDisable() {
        logger.info("==> Player Watcher spinning down")
    }
}
