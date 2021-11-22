package hu.hitgyulekezete.hitradio.view.components.program.programcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.card.Card
import hu.hitgyulekezete.hitradio.view.components.news.newscard.NewsCard

@Composable
fun ProgramCard(
    program: Program,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        image = rememberImagePainter(program.pictureOrDefault),
        title = program.name,
        fullWidth = false,
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview
@Composable
fun Preview_ProgramCard() {
    PreviewContainer {
        ProgramCard(
            program = Program(
                id = "1",
                name = "Test",
                picture = null,
                description = "alma ".repeat(15)
            )
        )
    }
}