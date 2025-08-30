package bk.minecraftplugin.playerWatcherPaperMCPlugin.utils

interface CurrentSystemTime {
    fun getCurrentSystemTimeMillis(): Long
}

class CurrentSystemTimeImpl() : CurrentSystemTime {
    override fun getCurrentSystemTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
