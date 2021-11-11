package hu.hitgyulekezete.hitradio.view.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
    )
}

@Preview
@Composable
fun Preview_TextField() {
    PreviewContainer {
        var value by remember { mutableStateOf("") }
        TextField(value, onValueChange = { value = it }, Modifier.fillMaxWidth())
    }
}