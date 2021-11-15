package hu.hitgyulekezete.hitradio.view.components.person.personcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.pictureOrDefault
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.primaryText

@Composable
fun PersonCard(
    person: Person,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(90.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberImagePainter(person.pictureOrDefault),
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 13.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(50))
        )

        Text(
            person.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.primaryText,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun Preview_PersonCard() {
    PreviewContainer {
        Row {
            PersonCard(
                Person(
                    id = "1",
                    name = "Teszt Ember",
                    type = PersonType.Guest,
                    picture = null,
                    description = null
                )
            )

            Spacer(Modifier.width(16.dp))

            PersonCard(
                Person(
                    id = "2",
                    name = "Teszt Műsorvezető",
                    type = PersonType.Host,
                    picture = "https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG",
                    description = null
                )
            )
        }
    }
}