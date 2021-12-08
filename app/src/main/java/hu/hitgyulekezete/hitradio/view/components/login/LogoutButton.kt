package hu.hitgyulekezete.hitradio.view.components

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import hu.hitgyulekezete.hitradio.view.components.button.ButtonVariant

@Composable
fun LogoutButton(modifier: Modifier = Modifier, onSuccess: () -> Unit = {}) {
    val context = LocalContext.current

    hu.hitgyulekezete.hitradio.view.components.button.Button(
        onClick = {
            AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener {
                    onSuccess()
                }
        },
        text = "Kijelentkezés",
        variant = ButtonVariant.Secondary,
        modifier = modifier,
    )
}