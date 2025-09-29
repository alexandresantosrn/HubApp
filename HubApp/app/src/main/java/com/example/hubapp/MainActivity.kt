package com.example.hubapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardCalculator = findViewById<MaterialCardView>(R.id.cardCalculator)
        val cardBasketball = findViewById<MaterialCardView>(R.id.cardBasketball)
        val cardConverter = findViewById<MaterialCardView>(R.id.cardConverter)

        cardCalculator.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        cardBasketball.setOnClickListener {
            startActivity(Intent(this, BasketballActivity::class.java))
        }

        cardConverter.setOnClickListener {
            startActivity(Intent(this, ConverterActivity::class.java))
        }
    }
}