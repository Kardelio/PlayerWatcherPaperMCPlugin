package bk.minecraftplugin.playerWatcherPaperMCPlugin

import bk.minecraftplugin.playerWatcherPaperMCPlugin.commands.ForceConfigUpdateCommand
import bk.minecraftplugin.playerWatcherPaperMCPlugin.commands.LotteryCommand
import bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config.LocalConfig
import bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config.LocalConfigImpl
import bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config.RemoteConfigCaller
import bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config.RemoteConfigCallerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.bukkit.plugin.java.JavaPlugin

// fun main(args: Array<String>) = runBlocking{
//    GlobalScope.launch {
//        val configCaller = ConfigCaller()
//        val config = configCaller.getCurrentConfig()
//        println("Current: ${config}")
//        delay(2000)
//        val config2 = configCaller.getCurrentConfig()
//        println("Current2: ${config2}")
//        val config3 = configCaller.getCurrentConfig(true)
//        println("Current3: ${config3}")
//    }.join()
// }

object Commands {
    const val LOTTERY_CMD = "lottery"
    const val CONFIG_UPDATE_CMD = "config-update"
}

class PlayerWatcherPaperMCPlugin : JavaPlugin() {

    private lateinit var pluginScope: CoroutineScope
    private lateinit var currentOnlinePlayers: CurrentOnlinePlayers

    private lateinit var configCaller: RemoteConfigCaller
    private lateinit var localConfig: LocalConfig

    override fun onEnable() {
        pluginScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        logger.info("==> Player Watcher starting up (Version: ${this.pluginMeta.version})")
//        this.config.options().copyDefaults()
//        this.saveDefaultConfig()
        localConfig = LocalConfigImpl(this)
        configCaller = RemoteConfigCallerImpl(localConfig)
        pluginScope.launch {
            try {
                WebhookCaller.sendMessage(
                    "**Player Watcher starting up (Version: ${this@PlayerWatcherPaperMCPlugin.pluginMeta.version})**"
                )
            } catch (e: Exception) {
                logger.warning(e.message)
            }
            val config = configCaller.getCurrentConfig(true)
            try {
                WebhookCaller.sendMessage(
                    "**Current Config:**\n${config.printOutConfig()}",
                    embedsColor = DiscordColors.LIGHT_BLUE.code
                )
            } catch (e: Exception) {
                logger.warning(e.message)
            }
        }

        getCommand(Commands.CONFIG_UPDATE_CMD)?.setExecutor(ForceConfigUpdateCommand(pluginScope, configCaller))

        // Optional Commands based on Remote Config
        getCommand(Commands.LOTTERY_CMD)?.setExecutor(LotteryCommand(localConfig, configCaller))

        currentOnlinePlayers = CurrentOnlinePlayers()

        server.pluginManager.registerEvents(
            PlayerListener(pluginScope, logger, currentOnlinePlayers, configCaller),
            this
        )
    }

    override fun onDisable() {
        logger.info("==> Player Watcher spinning down")
    }
}
