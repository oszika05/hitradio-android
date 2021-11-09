package hu.hitgyulekezete.hitradio.view.pages

import android.app.Activity
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingBar
import hu.hitgyulekezete.hitradio.view.pages.episode.EpisodePage
import hu.hitgyulekezete.hitradio.view.pages.episodes.EpisodesPage
import hu.hitgyulekezete.hitradio.view.pages.news.NewsPage
import hu.hitgyulekezete.hitradio.view.pages.news.NewsPageViewModel
import hu.hitgyulekezete.hitradio.view.pages.newsitem.NewsItemPage
import hu.hitgyulekezete.hitradio.view.pages.people.PeoplePage
import hu.hitgyulekezete.hitradio.view.pages.person.PersonPage
import hu.hitgyulekezete.hitradio.view.pages.program.ProgramPage
import kotlinx.coroutines.launch

private data class BottomNavigationPages(
    val key: String,
    val label: String,
    val icon: @Composable () -> Unit
) {
    companion object {
        val Home = BottomNavigationPages("home", "Home") @Composable {
            Icon(
                Icons.Default.Home,
                contentDescription = "home"
            )
        }
        val Live = BottomNavigationPages("live", "Live") @Composable {
            Icon(
                Icons.Default.Podcasts,
                contentDescription = "live"
            )
        }
        val Discover = BottomNavigationPages("discover", "Discover") @Composable {
            Icon(
                Icons.Default.Search,
                contentDescription = "discover"
            )
        }
    }
}

@Composable
private fun BottomBar(
    routes: List<BottomNavigationPages>,
    currentRoute: BottomNavigationPages,
    setCurrentRoute: (BottomNavigationPages) -> Unit,
    navControllers: Map<BottomNavigationPages, NavHostController>,
) {

    BottomNavigation {
        routes.forEach { route ->

            val navController = navControllers[route] ?: return@forEach

            BottomNavigationItem(
                icon = { route.icon() },
                label = { Text(route.label) },
                selected = currentRoute.key == route.key,
                onClick = {
                    if (currentRoute.key == route.key) {
                        navController.navigate(route.key) {
                            launchSingleTop = true
                            popUpTo(route.key) {
                                inclusive = true
                                saveState = false
                            }
                        }
                    } else {
                        setCurrentRoute(route)
                    }
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun InnerLayout(
    startDestination: String,
    navController: NavHostController,
    audioController: AudioController,
    audioManager: AudioManager,
    volumeObserver: VolumeObserver,
    nowPlayingBarSwipeableState: SwipeableState<Int>
) {
    NowPlayingBar(
        audioController = audioController,
        audioManager = audioManager,
        volumeObserver = volumeObserver,
        swipeableState = nowPlayingBarSwipeableState,
    ) {
        NavHost(navController = navController, startDestination = startDestination, Modifier.fillMaxSize()) {
            composable("home") {
                Column {
                    Text("home")
                    Button(onClick = {
                        navController.navigate("news") {
                            this.restoreState = true
                            this.launchSingleTop = true
                        }
                    }) {
                        Text("go to news")
                    }
                    Button(onClick = {
                        navController.navigate("episodes") {
                            this.restoreState = true
                            this.launchSingleTop = true
                        }
                    }) {
                        Text("go to episodes")
                    }
                    Button(onClick = {
                        navController.navigate("program/kozeppont")
                    }) {
                        Text("go to kozeppont")
                    }
                    Button(onClick = {
                        navController.navigate("people")
                    }) {
                        Text("go to people")
                    }
                }

            }
            composable("live") {
                Text("live")
            }
            composable("discover") {
                Text("discover")
            }
            composable("news") { backStack ->
                NewsPage(
                    viewModel = hiltViewModel(backStack),
                    onNewsItemClick = { news ->
                        val newsJson = Gson().toJson(news)
                        Log.d("ALMA", "news json: $newsJson")
                        val encodedNewsJson = Uri.encode(newsJson)
                        Log.d("ALMA", "news encoded json: $encodedNewsJson")
                        navController.navigate("newsitem/${encodedNewsJson}")
                    }
                )
            }
            composable("newsitem/{item}") { backStackEntry ->
                val newsJson = backStackEntry.arguments?.getString("item")
                Log.d("ALMA", "newsitem raw: $newsJson")
                val decodedNewsJson = Uri.encode(newsJson)
                Log.d("ALMA", "newsitem docoded: $decodedNewsJson")
                val news = Gson().fromJson(newsJson, News::class.java)

                NewsItemPage(news = news)
            }
            composable("episodes") { backStack ->

                val programId = backStack.arguments?.get("programId") as String?
                val initialSearch = backStack.arguments?.get("search") as String?

                EpisodesPage(
                    viewModel = hiltViewModel(backStack),
                    audioController = audioController,
                    initialSearch = initialSearch ?: "",
                    programId = programId,
                    onEpisodeClick = { episode ->
                        navController.navigate("episode/${episode?.id}")
                    }
                )
            }
            composable("episode/{id}") { backStack ->

                val episodeId = backStack.arguments?.get("id") as String? ?: return@composable

                EpisodePage(
                    episodeId = episodeId,
                    audioController = audioController,
                    viewModel = hiltViewModel(backStack),
                    onEpisodeClick = { episode ->
                        navController.navigate("episode/${episode.id}")
                    },
                    onPersonClick = {

                    },
                    onTagClick = {

                    }
                )
            }
            composable("program/{id}") { backStack ->

                val programId = backStack.arguments?.get("id") as String? ?: return@composable

                ProgramPage(
                    programId = programId,
                    audioController = audioController,
                    onEpisodeClick = { episode ->
                        navController.navigate("episode/${episode.id}")
                    },
                )
            }
            composable("people") { backStack ->
                PeoplePage(
                    viewModel = hiltViewModel(backStack),
                    onPersonClick = { person ->
                        navController.navigate("person/${person.id}")
                    }
                )
            }
            composable("person/{id}") { backStack ->
                val personId = backStack.arguments?.get("id") as String? ?: return@composable

                PersonPage(
                    personId = personId,
                    viewModel = hiltViewModel(backStack),
                    onEpisodeClick = { episode ->
                        navController.navigate("episode/${episode.id}")
                    }
                )

            }
        }
    }

}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun Layout(
    audioController: AudioController,
    audioManager: AudioManager,
    volumeObserver: VolumeObserver,
) {

    val homeNavController = rememberNavController()
    val liveNavController = rememberNavController()
    val discoverNavController = rememberNavController()

    val navControllers = mapOf(
        BottomNavigationPages.Home to homeNavController,
        BottomNavigationPages.Live to liveNavController,
        BottomNavigationPages.Discover to discoverNavController,
    )

    val bottomNavigationItems = listOf(
        BottomNavigationPages.Home,
        BottomNavigationPages.Live,
        BottomNavigationPages.Discover,
    )

    var currentRoute by remember { mutableStateOf(BottomNavigationPages.Home) }

    var nowPlayingBarSwipeableState = rememberSwipeableState(0)
    val scope = rememberCoroutineScope()


    Scaffold(
        bottomBar = {
            BottomBar(
                routes = bottomNavigationItems,
                currentRoute = currentRoute,
                setCurrentRoute = {
                    scope.launch {
                        nowPlayingBarSwipeableState.animateTo(0)
                    }
                    currentRoute = it
                },
                navControllers = navControllers,
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .alpha(if (currentRoute.key == BottomNavigationPages.Home.key) 1f else 0f)
            ) {
                InnerLayout(
                    startDestination = BottomNavigationPages.Home.key,
                    navController = homeNavController,
                    audioController = audioController,
                    audioManager = audioManager,
                    volumeObserver = volumeObserver,
                    nowPlayingBarSwipeableState = nowPlayingBarSwipeableState,
                )
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .alpha(if (currentRoute.key == BottomNavigationPages.Live.key) 1f else 0f)
            ) {
                InnerLayout(
                    startDestination = BottomNavigationPages.Live.key,
                    navController = liveNavController,
                    audioController = audioController,
                    audioManager = audioManager,
                    volumeObserver = volumeObserver,
                    nowPlayingBarSwipeableState = nowPlayingBarSwipeableState,
                )
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .alpha(if (currentRoute.key == BottomNavigationPages.Discover.key) 1f else 0f)
            ) {
                InnerLayout(
                    startDestination = BottomNavigationPages.Discover.key,
                    navController = discoverNavController,
                    audioController = audioController,
                    audioManager = audioManager,
                    volumeObserver = volumeObserver,
                    nowPlayingBarSwipeableState = nowPlayingBarSwipeableState,
                )
            }
        }
    }


}

//@ExperimentalMaterialApi
//@Preview(showBackground = true)
//@Composable
//fun Preview_Layout() {
//    Layout(
//        audioController = AudioController(null)
//    )
//}