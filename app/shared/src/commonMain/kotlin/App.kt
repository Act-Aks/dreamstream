import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import navigation.AppNavigation

@Composable
fun App(
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    DreamStreamTheme(darkTheme) {
        AppNavigation()
    }
}
