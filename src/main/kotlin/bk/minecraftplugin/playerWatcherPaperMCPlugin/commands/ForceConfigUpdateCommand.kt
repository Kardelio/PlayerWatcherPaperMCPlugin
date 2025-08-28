package bk.minecraftplugin.playerWatcherPaperMCPlugin.commands

import bk.minecraftplugin.playerWatcherPaperMCPlugin.ConfigCaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ForceConfigUpdateCommand(val scope: CoroutineScope, val configCaller: ConfigCaller) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            scope.launch {
                configCaller.getCurrentConfig(true)
                sender.sendMessage("Config was forcefully updated!")
            }
        }
        return true
    }
}