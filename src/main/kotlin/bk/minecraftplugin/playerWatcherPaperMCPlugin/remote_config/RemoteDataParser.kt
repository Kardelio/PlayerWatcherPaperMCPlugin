package bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config

class RemoteDataParser() {

    companion object {
        enum class SupportedDataTypes(val typeStr: String) {
            BOOLEAN("boolean"),
            STRING("string"),
            INT("int"),
        }
    }


    fun convertStringDataToDataType(inString: String): SupportedDataTypes {

        return SupportedDataTypes.BOOLEAN
    }
}