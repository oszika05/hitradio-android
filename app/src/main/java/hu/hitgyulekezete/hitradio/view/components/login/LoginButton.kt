package hu.hitgyulekezete.hitradio.view.components

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
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
fun LoginButton(modifier: Modifier = Modifier, onSuccess: () -> Unit = {}) {
    val signInLauncher = rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) {
        val response = it.idpResponse
        onSuccess()
    }

    hu.hitgyulekezete.hitradio.view.components.button.Button(
        onClick = {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()

            signInLauncher.launch(signInIntent)
        },
        text = "Bejelentkezés",
        variant = ButtonVariant.Secondary,
        modifier = modifier,
    )
}