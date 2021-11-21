package hu.hitgyulekezete.hitradio.view.components.textfield

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.coloredShadow
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.nowPlayingShadow
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.label.Label
import hu.hitgyulekezete.hitradio.view.layout.secondaryText

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        var isFocused by remember { mutableStateOf(false) }

        val color = if (isFocused) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onBackground
        }

        label?.let { label ->
            Label(text = label, color = color)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .nowPlayingShadow()
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colors.surface)
                .padding(vertical = 10.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                singleLine = true,
                maxLines = 1,
                cursorBrush = SolidColor(MaterialTheme.colors.secondaryText),
            )

            placeholder?.let { placeholder ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colors.secondaryText,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }

    }

}

@Preview
@Composable
fun Preview_TextField() {
    PreviewContainer {
        Column {
            var value by remember { mutableStateOf("Test") }
            TextField(
                value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                value,
                onValueChange = { value = it },
                label = "Name",
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search"
            )
        }

    }
}