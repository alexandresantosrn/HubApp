package com.example.hubapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConverterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.appStart("Conversor")
        setContentView(R.layout.activity_converter)

        val inputValue = findViewById<EditText>(R.id.etInputValue)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val btnConvert = findViewById<Button>(R.id.btnConvert)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnHome = findViewById<Button>(R.id.btnHome)

        LogHelper.logVerbose("ConverterActivity", "Componentes de interface inicializados com sucesso")

        btnHome.setOnClickListener {
            LogHelper.logInfo("ConverterActivity", "Usuário clicou em 'Home', retornando à tela principal")
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        val types = listOf(
            "Celsius → Fahrenheit",
            "Fahrenheit → Celsius",
            "Km → Milhas",
            "Milhas → Km"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        LogHelper.logVerbose("ConverterActivity", "Spinner de tipos de conversão configurado: ${types.joinToString()}")

        btnConvert.setOnClickListener {
            val valueText = inputValue.text.toString()
            val value = valueText.toDoubleOrNull()

            if (value == null) {
                LogHelper.logWarning("ConverterActivity", "Valor inválido informado pelo usuário: '$valueText'")
                Toast.makeText(this, "Digite um valor válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedType = spinnerType.selectedItem.toString()

            try {
                val result = when (selectedType) {
                    "Celsius → Fahrenheit" -> value * 9 / 5 + 32
                    "Fahrenheit → Celsius" -> (value - 32) * 5 / 9
                    "Km → Milhas" -> value * 0.621371
                    "Milhas → Km" -> value / 0.621371
                    else -> 0.0
                }

                val formatted = if (result % 1.0 == 0.0)
                    result.toInt().toString()
                else
                    String.format("%.2f", result)

                LogHelper.logDebug("ConverterActivity", "Conversão realizada: $selectedType | Valor: $value → Resultado: $formatted")
                tvResult.text = "Resultado: $formatted"

            } catch (e: Exception) {
                LogHelper.logError("ConverterActivity", "Erro inesperado durante a conversão: ${e.message}", e)
                Toast.makeText(this, "Ocorreu um erro na conversão.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.logWarning("ConverterActivity", "onDestroy chamado — encerrando a Activity.")
        LogHelper.appStop("Conversor")
    }
}
