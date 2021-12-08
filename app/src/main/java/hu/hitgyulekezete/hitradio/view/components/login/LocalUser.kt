package hu.hitgyulekezete.hitradio.view.components.login

import androidx.compose.runtime.compositionLocalOf
import com.google.firebase.auth.FirebaseAuth

val LocalUser = compositionLocalOf { FirebaseAuth.getInstance().currentUser }