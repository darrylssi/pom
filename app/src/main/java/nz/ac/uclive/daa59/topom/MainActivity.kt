package nz.ac.uclive.daa59.topom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    modifier = Modifier.fillMaxSize()
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
    val colorViewModel = viewModel<ColorViewModel>()
    Scaffold(
        bottomBar = { BottomNav(navController = navController, colorViewModel = colorViewModel) }
    ) { it
        NavigationGraph(navController = navController, colorViewModel = colorViewModel)
    }
}

@Composable
fun BottomNav(navController: NavController, colorViewModel: ColorViewModel) {
    val items = listOf(
        BottomNavItem.Timer,
        BottomNavItem.ToDo,
        BottomNavItem.Settings
    )
    BottomNavigation(
        backgroundColor = getColor(colorViewModel = colorViewModel),
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
fun NavigationGraph(navController: NavHostController, colorViewModel: ColorViewModel) {
    NavHost(navController, startDestination = BottomNavItem.Timer.screen_route) {
        composable(BottomNavItem.Timer.screen_route) {
            TimerScreen(25*60*1000L, colorViewModel)
        }
        composable(BottomNavItem.ToDo.screen_route) {
            ToDoScreen(colorViewModel)
        }
        composable(BottomNavItem.Settings.screen_route) {
            SettingsScreen(colorViewModel = colorViewModel)
        }
    }
}

class ColorViewModel: ViewModel() {
    val selectedOption = mutableStateOf("green")
}

@Composable
fun getColor(colorViewModel: ColorViewModel) = run {
    when (colorViewModel.selectedOption.value) {
        "gray" -> colorResource(id = R.color.gray)
        "orange" -> colorResource(id = R.color.orange)
        "pink" -> colorResource(id = R.color.pink)
        "yellow" -> colorResource(id = R.color.yellow)
        else -> { colorResource(id = R.color.green) }
    }
}