package hu.hitgyulekezete.hitradio.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import java.text.SimpleDateFormat
import java.util.*

val formatter = SimpleDateFormat("HH:mm")

@Composable
fun OnlineRadio(
    playbackState: AudioStateManager.PlaybackState,
    onPlayPause: () -> Unit,
    programRepository: CurrentProgramRepository,
) {
    Card(
        Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        val currentProgram = programRepository.currentProgramLiveData.observeAsState()

        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            if (currentProgram.value?.title != null) {
                Text(
                    "Éppen adásban",
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = currentProgram.value!!.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            } else {
                Text(
                    text = "Élő adás",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }

            if (currentProgram.value?.description != null) {
                Text(
                    text = currentProgram.value!!.description,
                    fontStyle = FontStyle.Italic,
                )
            }

            val nextPrograms = programRepository.nextPrograms.observeAsState()

            val length = nextPrograms.value?.size?.coerceAtMost(3) ?: 0

            if (length > 0) {
                Card(
                    Modifier.padding(top = 8.dp)
                ) {
                    Column(
                        Modifier.padding(all = 8.dp)
                    ) {
                        Text(
                            "Következik",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        for (i in 0 until length) {
                            val program = nextPrograms.value!![i]

                            Row(
                                Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = formatter.format(program.start),
                                    fontStyle = FontStyle.Italic
                                )
                                Spacer(
                                    Modifier.width(4.dp)
                                )
                                Text(text = program.title)
                            }
                        }
                    }
                }
            }

            Row(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Box(
                    Modifier
                        .width(56.dp)
                        .height(56.dp)
                ) {
                    PlayPauseButton(
                        playbackState = playbackState,
                        onClick = onPlayPause
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OnlineRadioPreview() {
    val start = Calendar.getInstance().run {
        add(Calendar.HOUR, -1)
        time
    }

    val end = Calendar.getInstance().run {
        add(Calendar.HOUR, 1)
        time
    }

    val currentProgramRepository = CurrentProgramRepository(
        listOf(
            ProgramGuideItem(
                id = "1",
                title = "Teszt műsor",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec aliquet, nisi et commodo scelerisque, turpis lorem elementum sem, at maximus nunc elit placerat leo. Phasellus et nibh sed lacus pulvinar faucibus id ut lectus.",
                replay = "",
                start = start,
                end = end
            )
        )
    )

    currentProgramRepository.start()

    var playbackState by remember { mutableStateOf(AudioStateManager.PlaybackState.STOPPED) }



    OnlineRadio(
        playbackState = playbackState,
        onPlayPause = {
            playbackState = playbackState.toggle()
        },
        programRepository = currentProgramRepository
    )
}