package cl.giancarlo.especialista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.giancarlo.especialista.navigation.NavGraph
import cl.giancarlo.especialista.ui.theme.EspecialistasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EspecialistasTheme() {
                NavGraph()
            }
        }
    }
}