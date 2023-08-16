package nz.ac.uclive.daa59.topom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.daa59.topom.ui.theme.ToPomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToPomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreenView()
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
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = when (item.title) {
                    "todo" -> stringResource(id = R.string.to_do)
                    "settings" -> stringResource(id = R.string.settings)
                    "timer" -> stringResource(id = R.string.timer)
                    else -> {""}},
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

    object ToDo : BottomNavItem("todo", R.drawable.icons8_list,"to_do")
    object Timer: BottomNavItem("timer",R.drawable.icons8_timer,"timer")
    object Settings: BottomNavItem("settings",R.drawable.icons8_settings,"settings")
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
