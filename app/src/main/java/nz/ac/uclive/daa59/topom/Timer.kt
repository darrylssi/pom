package nz.ac.uclive.daa59.topom

import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
    totalTime: Long,
    colorViewModel: ColorViewModel
) {
    var currentTime by rememberSaveable { mutableStateOf(totalTime) }
    var currentSec by rememberSaveable { mutableStateOf((totalTime / 1000L).mod(60L)) }
    var currentMin by rememberSaveable { mutableStateOf((totalTime / 1000L) / 60L) }
    var isTimerOn by rememberSaveable { mutableStateOf(false) }
    var pomPass by rememberSaveable { mutableStateOf(0) }

    val formattedMin = currentMin.toString().padStart(2, '0')
    val formattedSec = currentSec.toString().padStart(2, '0')
    
    var size by rememberSaveable { mutableStateOf(1f) }
    val animateScale by animateFloatAsState(targetValue = size)

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val context = LocalContext.current
    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

    val mediaPlayer = MediaPlayer.create(context, R.raw.tada)

    LaunchedEffect(key1 = currentTime, key2 = isTimerOn) {
        if(currentTime > 0 && isTimerOn) {
            delay(1000L)
            currentTime -= 1000L
            currentSec = (currentTime / 1000L).mod(60L)
            currentMin = (currentTime / 1000L) / 60L
        } else if (isTimerOn) {
            currentTime = totalTime
            currentMin = (totalTime / 1000L) / 60L
            currentSec = (totalTime / 1000L).mod(60L)
            size = 1f
            mediaPlayer.start()
            isTimerOn = false
            pomPass ++
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        if (isPortrait) {
            Text(
                text = "$formattedMin : $formattedSec",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.scale(scale = animateScale)
            )
            Button(
                onClick = {
                    size = if (isTimerOn) 1f else 1.5f
                    if(currentTime <= 0L) {
                        currentTime = totalTime
                        isTimerOn = true
                    } else {
                        currentTime = totalTime
                        currentMin = (totalTime / 1000L) / 60L
                        currentSec = (totalTime / 1000L).mod(60L)
                        isTimerOn = !isTimerOn
                    }
                    vibrator?.let { v ->
                        val amplitude = 100 // Adjust the vibration strength
                        val duration = 50 // Adjust the vibration duration

                        if (v.hasVibrator()) {
                            val vibrationEffect = VibrationEffect.createOneShot(duration.toLong(), amplitude)
                            v.vibrate(vibrationEffect)
                        }
                    }
                },
                modifier = Modifier.wrapContentSize(Alignment.Center),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!isTimerOn || currentTime <= 0L) {
                        getColor(colorViewModel = colorViewModel)
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
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.poms, pomPass)
            )
            Button(
                onClick = {
                    val uri = Uri.parse("smsto:")
                    val message = context.resources.getQuantityString(R.plurals.text_message, pomPass, pomPass)
                    val intent = Intent(Intent.ACTION_SENDTO, uri).putExtra(Intent.EXTRA_TEXT, message)
                    startActivity(context, intent, null)
                },
                enabled = pomPass > 0,
                modifier = Modifier.wrapContentSize(Alignment.Center),
                colors = ButtonDefaults.buttonColors( getColor(colorViewModel = colorViewModel) )
            ) {
                Text(
                    text = stringResource(id = R.string.share)
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column() {
                    Text(
                        text = "$formattedMin : $formattedSec",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Button(
                        onClick = {
                            size = if (isTimerOn) 1f else 1.5f
                            if(currentTime <= 0L) {
                                currentTime = totalTime
                                isTimerOn = true
                            } else {
                                currentTime = totalTime
                                currentMin = (totalTime / 1000L) / 60L
                                currentSec = (totalTime / 1000L).mod(60L)
                                isTimerOn = !isTimerOn
                            }
                            vibrator?.let { v ->
                                val amplitude = 100 // Adjust the vibration strength
                                val duration = 50 // Adjust the vibration duration

                                if (v.hasVibrator()) {
                                    val vibrationEffect = VibrationEffect.createOneShot(duration.toLong(), amplitude)
                                    v.vibrate(vibrationEffect)
                                }
                            }
                        },
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (!isTimerOn || currentTime <= 0L) {
                                getColor(colorViewModel = colorViewModel)
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
                Column() {
                    Text(
                        text = stringResource(id = R.string.poms, pomPass)
                    )
                    Button(
                        onClick = {
                            val uri = Uri.parse("smsto:")
                            val message = context.resources.getQuantityString(R.plurals.text_message, pomPass, pomPass)
                            val intent = Intent(Intent.ACTION_SENDTO, uri).putExtra(Intent.EXTRA_TEXT, message)
                            startActivity(context, intent, null)
                        },
                        enabled = pomPass > 0,
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        colors = ButtonDefaults.buttonColors( getColor(colorViewModel = colorViewModel) )
                    ) {
                        Text(
                            text = stringResource(id = R.string.share)
                        )
                    }
                }
            }

        }
    }
}