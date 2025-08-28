package bk.minecraftplugin.playerWatcherPaperMCPlugin

import bk.minecraftplugin.playerWatcherPaperMCPlugin.commands.TestCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.bukkit.plugin.java.JavaPlugin

class PlayerWatcherPaperMCPlugin : JavaPlugin() {

    private lateinit var pluginScope: CoroutineScope
    private lateinit var currentOnlinePlayers: CurrentOnlinePlayers

    override fun onEnable() {
        pluginScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        logger.info("==> Player Watcher starting up (Version: ${this.pluginMeta.version})")
//        config.options()

        //huh?
        config.options().copyDefaults()
        saveDefaultConfig()

        getCommand("test")?.setExecutor(TestCommand(this))

        currentOnlinePlayers = CurrentOnlinePlayers()

        server.pluginManager.registerEvents(PlayerListener(pluginScope, logger, currentOnlinePlayers), this)

        pluginScope.launch {
            try {
                WebhookCaller.sendMessage("Player Watcher starting up (Version: ${this@PlayerWatcherPaperMCPlugin.pluginMeta.version})")
            } catch (e: Exception) {
                logger.warning(e.message)
            }
        }
    }

    override fun onDisable() {
        logger.info("==> Player Watcher spinning down")
    }
}
