package com.example.xpectrum

import com.journeyapps.barcodescanner.CaptureActivity
import android.content.pm.ActivityInfo
import android.os.Bundle

import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.FrameLayout
import android.graphics.Color
import android.os.Build

class PortraitCaptureActivity : CaptureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Cambiar color de la barra de estado y navegación
        window.statusBarColor = Color.parseColor("#2A3C4F")
        window.navigationBarColor = Color.parseColor("#2A3C4F")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        // Fondo degradado en la ventana
        window.setBackgroundDrawableResource(android.R.color.transparent)
        val gradientView = View(this)
        gradientView.background = android.graphics.drawable.GradientDrawable(
            android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#2A3C4F"), Color.parseColor("#495053"), Color.parseColor("#8B8D86"))
        )
        val decorView = window.decorView as? FrameLayout
        decorView?.addView(gradientView, 0, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        // Título con icono
        if (decorView != null) {
            val container = FrameLayout(this)
            val icon = android.widget.ImageView(this)
            icon.setImageResource(android.R.drawable.ic_menu_camera)
            icon.setColorFilter(Color.WHITE)
            val iconParams = FrameLayout.LayoutParams(100, 100)
            iconParams.topMargin = 80
            iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL
            container.addView(icon, iconParams)

            val titleView = TextView(this)
            titleView.text = "Escanea tu boleto"
            titleView.setTextColor(Color.WHITE)
            titleView.textSize = 24f
            titleView.setTypeface(null, android.graphics.Typeface.BOLD)
            titleView.setPadding(0, 200, 0, 16)
            titleView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            val titleParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            titleParams.topMargin = 0
            container.addView(titleView, titleParams)

            // Borde redondeado semitransparente para el área de escaneo
            val borderView = View(this)
            val borderDrawable = android.graphics.drawable.GradientDrawable()
            borderDrawable.setColor(Color.parseColor("#33000000"))
            borderDrawable.cornerRadius = 32f
            borderDrawable.setStroke(6, Color.parseColor("#2A3C4F"))
            borderView.background = borderDrawable
            val borderParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                (resources.displayMetrics.widthPixels * 0.8).toInt()
            )
            borderParams.gravity = android.view.Gravity.CENTER
            borderParams.topMargin = 120
            container.addView(borderView, borderParams)

            decorView.addView(container)
        }
    }
}

