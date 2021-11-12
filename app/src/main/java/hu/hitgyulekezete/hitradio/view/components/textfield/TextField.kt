package hu.hitgyulekezete.hitradio.view.components.textfield

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.label.Label

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
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
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = color,
                    shape = RoundedCornerShape(15)
                )
                .padding(vertical = 8.dp, horizontal = 8.dp)
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
            )
        }

    }

}

@Preview
@Composable
fun Preview_TextField() {
    PreviewContainer {
        Column {
            var value by remember { mutableStateOf("") }
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
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}