package com.example.todoapp

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import com.example.todoapp.presentation.pages.HomePage
import com.example.todoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setStatusBarColor(window, Color.WHITE)
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    TodoAppTheme(darkTheme = false) {
        HomePage()
    }
}

private fun setStatusBarColor(window: Window?, @ColorInt color: Int) {
    window?.statusBarColor = color
}