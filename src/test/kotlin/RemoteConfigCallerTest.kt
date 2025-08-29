import bk.minecraftplugin.playerWatcherPaperMCPlugin.local_config.LocalConfig
import bk.minecraftplugin.playerWatcherPaperMCPlugin.remote_config.RemoteConfigCallerImpl
import bk.minecraftplugin.playerWatcherPaperMCPlugin.utils.CurrentSystemTime
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import kotlin.test.Test

class RemoteConfigCallerTest {

    lateinit var caller: RemoteConfigCallerImpl

    val localConfigMock = mockk<LocalConfig>()
    val currentSystemTimeMock = mockk<CurrentSystemTime>()

    @Before
    fun setup() {
        every { localConfigMock.getAsLong(any()) } returns 0L
        every { currentSystemTimeMock.getCurrentSystemTimeMillis() } returns 0L
        caller = RemoteConfigCallerImpl(localConfigMock, currentSystemTimeMock)
    }

    @Test
    fun t() {
        assert("a" == "a")
        caller.getStoredConfigOnly()
    }
}