package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.airbnb.lottie.compose.*


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1) Cargamos la composición de Lottie desde assets/logo.json
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("logo.json"))
            // 2) Controlamos si ya se reprodujo para no reiniciarla
            var isPlayed by remember { mutableStateOf(false) }
            // 3) Animamos la composición una sola vez
            val progress by animateLottieCompositionAsState(
                composition,
                isPlaying = !isPlayed,
                iterations = 1,
                speed = 1.0f,
                restartOnPlay = false
            )

            // 4) Cuando el progreso llega a 1f (fin de animación), abrimos MainScreenActivity
            LaunchedEffect(progress) {
                if (progress == 1f && !isPlayed) {
                    isPlayed = true
                    startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                    finish()
                }
            }

            // 5) Dibujamos la animación centrada sobre fondo negro
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.colorsplash)),
                contentAlignment = Alignment.Center
            ) {
                if (composition != null) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress }
                    )
                }
            }
        }
    }
}