package com.eljeff.deudoresapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Configuración temporizador
        val timer = Timer()

        timer.schedule(
            timerTask {
                goToLoginActivity()
            }, 1500
        ) // SE cierra schedule
    }

    private fun goToLoginActivity() {

        // creamos el intent para ir al loginactivity
        val intent = Intent(this, LoginActivity::class.java)

        // Limpiamos la pila de actividades
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        //Se llama la actividad de login
        startActivity(intent)
    }
}