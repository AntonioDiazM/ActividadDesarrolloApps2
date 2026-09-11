package com.example.actividaddesarrolloapps2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.actividaddesarrolloapps2.data.local.database.AppDatabase
import com.example.actividaddesarrolloapps2.data.local.entity.Usuario
import com.example.actividaddesarrolloapps2.screens.HomeScreen
import com.example.actividaddesarrolloapps2.screens.LoginScreen
import com.example.actividaddesarrolloapps2.screens.RegisterScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()

        setContent {
            var currentScreen by remember { mutableStateOf("login") }
            var loggedUserName by remember { mutableStateOf("") }
            val coroutineScope = rememberCoroutineScope()

            MaterialTheme {
                when (currentScreen) {
                    "login" -> LoginScreen(
                        onLoginClick = { userOrEmail, pass ->
                            coroutineScope.launch {
                                val usuario = usuarioDao.obtenerPorEmail(userOrEmail)

                                when {
                                    usuario == null -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "El correo no está registrado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    usuario.password == pass -> {
                                        loggedUserName = usuario.nombre
                                        Toast.makeText(
                                            this@MainActivity,
                                            "¡Bienvenido, ${usuario.nombre}!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Redirigir al Menú Principal
                                        currentScreen = "home"
                                    }
                                    else -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Contraseña incorrecta",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        onRegisterClick = {
                            currentScreen = "registro"
                        }
                    )

                    "registro" -> RegisterScreen(
                        onRegisterSuccess = { name, phone, document, email, pass ->
                            coroutineScope.launch {
                                val nuevoUsuario = Usuario(
                                    nombre = name,
                                    telefono = phone,
                                    dni = document,
                                    email = email,
                                    password = pass
                                )
                                usuarioDao.insertar(nuevoUsuario)

                                Toast.makeText(
                                    this@MainActivity,
                                    "Registro exitoso. Inicia sesión con tus datos.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                currentScreen = "login"
                            }
                        },
                        onBackToLoginClick = {
                            currentScreen = "login"
                        }
                    )

                    "home" -> HomeScreen(
                        nombreUsuario = loggedUserName,
                        onLogoutClick = {
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }
}