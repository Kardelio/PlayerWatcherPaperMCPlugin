package bk.minecraftplugin.playerWatcherPaperMCPlugin

object UserMapper {
    fun getDiscordIdFromUsername(minecraftUsername: String): String {
        return BuildConfig.MINECRAFT_USER_TO_DISCORD_ID_MAP[minecraftUsername]
            ?: throw Exception("No user by that name")
    }
}
