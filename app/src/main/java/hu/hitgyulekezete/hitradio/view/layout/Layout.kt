package hu.hitgyulekezete.hitradio.view.pages

import android.media.AudioManager
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.hitgyulekezete.hitradio.R
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.view.layout.HitradioTheme
import hu.hitgyulekezete.hitradio.view.layout.secondaryText
import hu.hitgyulekezete.hitradio.view.layout.navBarColor
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingBar
import hu.hitgyulekezete.hitradio.view.pages.discover.DiscoverPage
import hu.hitgyulekezete.hitradio.view.pages.episode.EpisodePage
import hu.hitgyulekezete.hitradio.view.pages.episodes.EpisodesPage
import hu.hitgyulekezete.hitradio.view.pages.live.LivePage
import hu.hitgyulekezete.hitradio.view.pages.news.NewsPage
import hu.hitgyulekezete.hitradio.view.pages.newsitem.NewsItemPage
import hu.hitgyulekezete.hitradio.view.pages.people.PeoplePage
import hu.hitgyulekezete.hitradio.view.pages.person.PersonPage
import hu.hitgyulekezete.hitradio.view.pages.program.ProgramPage
import hu.hitgyulekezete.hitradio.view.pages.programs.ProgramsPage
import kotlinx.coroutines.launch

private data class BottomNavigationPages(
    val key: String,
    val label: String,
    val icon: @Composable () -> Unit
) {
    companion object {
        val Home = BottomNavigationPages("home", "Főoldal") @Composable {
            Icon(
                painterResource(id = R.drawable.ic_navbar_home),
                contentDescription = "Főoldal"
            )
        }
        val Live = BottomNavigationPages("live", "Lejátszó") @Composable {
            Icon(
                painterResource(id = R.drawable.ic_navbar_nowplaying),
                contentDescription = "Lejátszó oldal"
            )
        }
        val Discover = BottomNavigationPages("discover", "Keresés") @Composable {
            Icon(
                painterResource(id = R.drawable.ic_navbar_discover),
                contentDescription = "Keresés oldal"
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

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.navBarColor,
        contentColor = MaterialTheme.colors.secondaryText,
    ) {
        routes.forEach { route ->

            val navController = navControllers[route] ?: return@forEach

            BottomNavigationItem(
                icon = { route.icon() },
                label = { Text(route.label) },
                selected = currentRoute.key == route.key,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.secondaryText,
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
        NavHost(
            navController = navController,
            startDestination = startDestination,
            Modifier.fillMaxSize()
        ) {
            composable("home") { backStack ->
                hu.hitgyulekezete.hitradio.view.pages.home.HomePage(
                    audioController = audioController,
                    viewModel = hiltViewModel(backStack),
                    onNavigateToEpisodesPage = {
                        navController.navigate("episodes")
                    },
                    onNavigateToEpisode = { episode ->
                        navController.navigate("episode/${episode.id}")
                    },
                    onNavigateToNewsPage = {
                        navController.navigate("news")
                    },
                    onNavigateToNewsItem = { news ->
                        navController.navigate("newsitem/${news.id}")
                    },
                    onNavigateToPeoplePage = {
                        navController.navigate("people")
                    },
                    onNavigateToPerson = { person ->
                        navController.navigate("person/${person.id}")
                    }
                )
//                Column {
//                    Text("home")
//                    Button(onClick = {
//                        navController.navigate("news") {
//                            this.restoreState = true
//                            this.launchSingleTop = true
//                        }
//                    }) {
//                        Text("go to news")
//                    }
//                    Button(onClick = {
//                        navController.navigate("episodes") {
//                            this.restoreState = true
//                            this.launchSingleTop = true
//                        }
//                    }) {
//                        Text("go to episodes")
//                    }
//                    Button(onClick = {
//                        navController.navigate("program/kozeppont")
//                    }) {
//                        Text("go to kozeppont")
//                    }
//                    Button(onClick = {
//                        navController.navigate("people")
//                    }) {
//                        Text("go to people")
//                    }
//                }

            }
            composable("live") { backStack ->
                LivePage(
                    audioController = audioController,
                    viewModel = hiltViewModel(backStack)
                )
            }
            composable("discover") { backStack ->
                DiscoverPage(
                    viewModel = hiltViewModel(backStack),
                    audioController = audioController,
                    onAllProgramsClick = { search ->
                        navController.navigate("programs?search=${search}")
                    },
                    onProgramClick = { program ->
                        navController.navigate("program/${program.id}")
                    },
                    onAllPeopleClick = { search ->
                        navController.navigate("people?search=${search}")
                    },
                    onPersonClick = { person ->
                        navController.navigate("person/${person.id}")
                    },
                    onEpisodeClick = { episode ->
                        navController.navigate("episode/${episode.id}")
                    }
                )
            }
            composable("news") { backStack ->
                NewsPage(
                    viewModel = hiltViewModel(backStack),
                    onNewsItemClick = { news ->
                        navController.navigate("newsitem/${news.id}")
                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable("newsitem/{item}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("item") ?: return@composable

                NewsItemPage(
                    newsId = id,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
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
                    },
                    onBackClick = {
                        navController.navigateUp()
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

                    },
                    onBackClick = {
                        navController.navigateUp()
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
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable("people?search={search}") { backStack ->
                val search = backStack.arguments?.get("search") as String?

                PeoplePage(
                    search = search,
                    viewModel = hiltViewModel(backStack),
                    onPersonClick = { person ->
                        navController.navigate("person/${person.id}")
                    },
                    onBackClick = {
                        navController.navigateUp()
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
                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
                )

            }
            composable("programs?search={search}") { backStack ->
                val search = backStack.arguments?.get("search") as String?

                ProgramsPage(
                    search = search,
                    viewModel = hiltViewModel(backStack),
                    onProgramClick = { program ->
                        navController.navigate("program/${program.id}")
                    },
                    onBackClick = {
                        navController.navigateUp()
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


    HitradioTheme {
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
                    .fillMaxSize()
            ) {
                Crossfade(currentRoute.key) { current ->
                    when (current) {
                        BottomNavigationPages.Home.key -> {
                            Box(
                                Modifier
                                    .fillMaxSize()
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
                        }
                        BottomNavigationPages.Live.key -> {
                            Box(
                                Modifier
                                    .fillMaxSize()
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
                        }
                        BottomNavigationPages.Discover.key -> {
                            Box(
                                Modifier
                                    .fillMaxSize()
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