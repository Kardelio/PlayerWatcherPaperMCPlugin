package bk.minecraftplugin.playerWatcherPaperMCPlugin

class CurrentOnlinePlayers {

    // storage in mem
    val currentOnlineMinecraftUserNames = mutableListOf<String>()

    fun getCurrentList(): List<String> {
        return currentOnlineMinecraftUserNames
    }

    fun addToList(username: String): Boolean {
        if (currentOnlineMinecraftUserNames.firstOrNull { it == username } == null) {
            // user NOT already in list skip
            currentOnlineMinecraftUserNames.add(username)
            return true
        } else {
            return false
        }
    }

    fun removeFromList(username: String): Boolean {
        if (currentOnlineMinecraftUserNames.firstOrNull { it == username } != null) {
            // user NOT already in list skip
            currentOnlineMinecraftUserNames.remove(username)
            return true
        } else {
            return false
        }
    }
}
