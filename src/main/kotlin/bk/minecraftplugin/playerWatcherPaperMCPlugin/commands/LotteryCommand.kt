package bk.minecraftplugin.playerWatcherPaperMCPlugin.commands

import bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config.LocalConfig
import bk.minecraftplugin.playerWatcherPaperMCPlugin.lottery.LotteryWheel
import bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config.RemoteConfigCaller
import bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config.RemoteConfigKeys
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * /test
 *
 * /kill @e[type=minecraft:zombie]
 */


class LotteryCommand(val localConfig: LocalConfig, val config: RemoteConfigCaller) : CommandExecutor {


    companion object Companion {
        //24 hours
        val LOTTERY_DELAY_TIME_LONG = (24 * 60 * 60 * 1000).toLong()
        //60 seconds
//        val LOTTERY_DELAY_TIME_LONG = (60 * 1000).toLong()
//        val LOTTERY_DELAY_TIME_LONG = (1000).toLong()
    }


    private fun checkPlayerCanPlay(player: Player): Boolean {

//        if (config.contains("players." + player.uniqueId.toString() + ".last_lottery_time")) {
        if (localConfig.containsValue("players." + player.uniqueId.toString() + ".last_lottery_time")) {
            val lastLotteryTime: Long =
                localConfig.getAsLong("players." + player.uniqueId.toString() + ".last_lottery_time")
            val currentTimeMillis = System.currentTimeMillis()

            // Check if a day has passed (24 hours * 60 minutes * 60 seconds * 1000 milliseconds).
//            val oneDayInMillis = (24 * 60 * 60 * 1000).toLong()

            if (currentTimeMillis - lastLotteryTime < LOTTERY_DELAY_TIME_LONG) {
                // Less than one day has passed. Send a cooldown message to the player.
                val timeLeftInMillis = LOTTERY_DELAY_TIME_LONG - (currentTimeMillis - lastLotteryTime)
                val hours = timeLeftInMillis / (60 * 60 * 1000)
                val minutes = (timeLeftInMillis % (60 * 60 * 1000)) / (60 * 1000)
                val seconds = (timeLeftInMillis % (60 * 1000)) / 1000

                player.sendMessage("§cYou can only use the lottery once every 24 hours!")
                player.sendMessage("§cTime remaining: " + hours + "h " + minutes + "m " + seconds + "s")
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    /*
    §0	Black
§1	Dark Blue
§2	Dark Green
§3	Dark Aqua
§4	Dark Red
§5	Dark Purple
§6	Gold
§7	Gray
§8	Dark Gray
§9	Blue
§a	Green
§b	Aqua
§c	Red
§d	Light Purple
§e	Yellow
§f	White
§k	Obfuscated (random)
§l	Bold
§m	~~Strikethrough~~
§n	Underline
§o	Italic
§r	Reset (clears all previous formatting)
     */

    private fun playerPlaysLottery(player: Player) {
        val wheel = LotteryWheel()
        wheel.addItem(ItemStack(Material.DIAMOND, 1), 0.05)
        wheel.addItem(ItemStack(Material.DIRT, 1), 0.75)
        wheel.addItem(ItemStack(Material.IRON_INGOT, 1), 0.2)
        val prize = wheel.spin()
        player.world.dropItem(player.location, prize)
        player.sendMessage("§aGood Luck...")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
//        if (config.getStoredConfigOnly()?.config[RemoteConfigKeys.LOTTERY] == true) {
        if (config.getStoredConfigOnly()?.getValue(RemoteConfigKeys.LOTTERY) == true) {

            println("Config Allows this command...")
            if (sender is Player) {
                val currentTimeMillis = System.currentTimeMillis()
                val can = checkPlayerCanPlay(sender)
                if (can) {
                    playerPlaysLottery(sender)
                    localConfig.saveLong("players.${sender.uniqueId}.last_lottery_time", currentTimeMillis)
//                    plugin.config.set("players.${sender.uniqueId}.last_lottery_time", currentTimeMillis)
//                    plugin.saveConfig()
                }
//            plugin.config.set("players.${sender.uniqueId}.last_lottery_time", currentTimeMillis)
//            plugin.config.set("example.x", loc.x)
//            plugin.saveConfig()
//            println("Players world: ${sender.world.name} : ${sender.world.key}")
//            sender.world.dropItem(sender.location, ItemStack(Material.DIAMOND, 2))


                //WORKS
//            object : BukkitRunnable() {
//                override fun run() {
//                    sender.sendMessage("Good lick")
////                    sender.world.spawnEntity(sender.location, EntityType.ZOMBIE)
//
//                }
//            }.runTaskLater(plugin, 20L) // 20L is the delay in ticks (1 second).
//            Bukkit.getWorlds().forEach {
//                println("---- ${it.name}")
//            }

            }

            return true
        } else {
            sender.sendMessage("§cLottery command was disabled in Remote Config")
            return false
        }

    }
}