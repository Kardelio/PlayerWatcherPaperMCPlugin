package bk.minecraftplugin.playerWatcherPaperMCPlugin.utils

interface Random {
    fun genDouble(): Double
}

class RandomImpl : Random {
    override fun genDouble(): Double {
        return kotlin.random.Random.nextDouble()
    }
}
