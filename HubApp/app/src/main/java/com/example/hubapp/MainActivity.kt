package com.example.hubapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.appStart("Hub")
        setContentView(R.layout.activity_main)

        LogHelper.logVerbose("MainActivity", "onCreate iniciado.")

        try {
            // Criando os cards para as telas.
            val cardCalculator = findViewById<MaterialCardView>(R.id.cardCalculator)
            val cardBasketball = findViewById<MaterialCardView>(R.id.cardBasketball)
            val cardConverter = findViewById<MaterialCardView>(R.id.cardConverter)

            LogHelper.logInfo("MainActivity", "Cards inicializados com sucesso.")

            // Chamando as telas.
            cardCalculator.setOnClickListener {
                LogHelper.logInfo("MainActivity", "Clicou no card Calculator.")
                startActivity(Intent(this, CalculatorActivity::class.java))
            }

            cardBasketball.setOnClickListener {
                LogHelper.logInfo("MainActivity", "Clicou no card Basketball.")
                startActivity(Intent(this, BasketballActivity::class.java))
            }

            cardConverter.setOnClickListener {
                LogHelper.logInfo("MainActivity", "Clicou no card Converter.")
                startActivity(Intent(this, ConverterActivity::class.java))
            }

        } catch (e: Exception) {
            LogHelper.logError("MainActivity", "Erro ao inicializar os cards: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.logWarning("MainActivity", "onDestroy chamado — Activity sendo destruída.")
    }
}
