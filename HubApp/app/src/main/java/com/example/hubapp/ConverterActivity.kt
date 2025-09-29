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
        setContentView(R.layout.activity_converter)

        val inputValue = findViewById<EditText>(R.id.etInputValue)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val btnConvert = findViewById<Button>(R.id.btnConvert)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnHome = findViewById<Button>(R.id.btnHome)

        btnHome.setOnClickListener {
            // volta para a MainActivity limpando a pilha
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish() // encerra a tela atual
        }

        // Tipos de conversão
        val types =
            listOf("Celsius → Fahrenheit", "Fahrenheit → Celsius", "Km → Milhas", "Milhas → Km")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        btnConvert.setOnClickListener {
            val value = inputValue.text.toString().toDoubleOrNull()

            if (value == null) {
                Toast.makeText(this, "Digite um valor válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = when (spinnerType.selectedItem.toString()) {
                "Celsius → Fahrenheit" -> value * 9 / 5 + 32
                "Fahrenheit → Celsius" -> (value - 32) * 5 / 9
                "Km → Milhas" -> value * 0.621371
                "Milhas → Km" -> value / 0.621371
                else -> 0.0
            }

            // Formata resultado removendo ".0" quando inteiro
            val formatted = if (result % 1.0 == 0.0) result.toInt().toString() else String.format(
                "%.2f",
                result
            )

            tvResult.text = "Resultado: $formatted"
        }
    }
}