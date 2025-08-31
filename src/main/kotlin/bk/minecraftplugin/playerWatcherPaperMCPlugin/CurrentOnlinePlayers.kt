package bk.minecraftplugin.playerWatcherPaperMCPlugin

class CurrentOnlinePlayers {

    /**
     * Specifically in memory storage used here.
     * As if the server is down there can be no players online
     * and the moment the server dies all players will become offline.
     */
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
