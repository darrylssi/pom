package nz.ac.uclive.daa59.topom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                    color = MaterialTheme.colors.background
                ) {
                    MainScreenView()
//                    Timer(totalTime = 25L*60L*1000L)
                }
            }
        }
    }
}

@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNav(navController = navController) }
    ) { it
        NavigationGraph(navController = navController)
    }
}

@Composable
fun BottomNav(navController: NavController) {
    val items = listOf(
        BottomNavItem.Timer,
        BottomNavItem.ToDo,
        BottomNavItem.Settings
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object ToDo : BottomNavItem("To Do", R.drawable.ic_launcher_background,"to_do")
    object Timer: BottomNavItem("Timer",R.drawable.ic_launcher_background,"timer")
    object Settings: BottomNavItem("Settings",R.drawable.ic_launcher_background,"settingd")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Timer.screen_route) {
        composable(BottomNavItem.Timer.screen_route) {
            TimerScreen(25L*60L*1000L)
        }
        composable(BottomNavItem.ToDo.screen_route) {
            ToDoScreen()
        }
        composable(BottomNavItem.Settings.screen_route) {
            SettingsScreen()
        }
    }
}

@Composable
fun TimerScreen(
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
                backgroundColor = if (!isTimerOn || currentTime <= 0L) {
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

@Composable
fun ToDoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

