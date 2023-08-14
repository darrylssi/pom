package nz.ac.uclive.daa59.topom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import nz.ac.uclive.daa59.topom.ui.theme.ToPomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToPomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Timer(totalTime = 25L*60L*1000L)
                }
            }
        }
    }
}

@Composable
fun Timer(
    totalTime: Long
) {
    var value by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var currentSec by remember { mutableStateOf(0L) }
    var currentMin by remember { mutableStateOf(25L) }
    var isTimerOn by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerOn) {
        if(currentTime > 0 && isTimerOn) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
            currentSec = (currentTime / 1000L).mod(60L)
            currentMin = (currentTime / 1000L) / 60L
        }
    }
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (currentSec < 10) {
                "$currentMin : 0$currentSec"
            } else {
                "$currentMin : $currentSec"
            },
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Button(
            onClick = {
                if(currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerOn = true
                } else {
                    isTimerOn = !isTimerOn
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            // change button color
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isTimerOn || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                // change the text of button based on values
                text = if (isTimerOn && currentTime >= 0L) "Stop"
                else if (!isTimerOn && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }
}
