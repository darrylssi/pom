package nz.ac.uclive.daa59.topom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
    totalTime: Long
) {
    var value by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var currentSec by remember { mutableStateOf(0L) }
    var currentMin by remember { mutableStateOf((totalTime / 1000L) / 60L) }
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
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
                    currentTime = totalTime
                    currentMin = 25
                    currentSec = 0
                    isTimerOn = !isTimerOn
                }
            },
            modifier = Modifier.wrapContentSize(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isTimerOn || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                text = if (currentTime <= 0L) stringResource(id = R.string.start)
                else if (isTimerOn) stringResource(id = R.string.stop)
                else stringResource(id = R.string.start)
            )
        }
    }
}