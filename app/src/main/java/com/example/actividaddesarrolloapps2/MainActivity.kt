package com.example.actividaddesarrolloapps2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.actividaddesarrolloapps2.screens.LoginScreen
import com.example.actividaddesarrolloapps2.screens.RegisterScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Guardamos el nombre de la pantalla actual
            var currentScreen by remember { mutableStateOf("login") }

            MaterialTheme {
                when (currentScreen) {
                    "login" -> LoginScreen(
                        onLoginClick = { user, pass ->
                            // Lógica cuando presiona ingresar
                        },
                        onRegisterClick = {
                            currentScreen = "registro" // Cambia a la pantalla de registro
                        }
                    )
                    "registro" -> RegisterScreen(
                        onRegisterSuccess = { name, email, pass ->
                            currentScreen = "login" // Regresa al login al completar el registro
                        },
                        onBackToLoginClick = {
                            currentScreen = "login" // Regresa al login si presiona el botón de atrás
                        }
                    )
                }
            }
        }
    }
}

