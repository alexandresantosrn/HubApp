package com.example.hubapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton

class CalculatorActivity : AppCompatActivity() {
    private lateinit var tvDisplay: TextView

    private var currentInput: String = ""

    private var operand: Double? = null

    private var pendingOp: String? = null

    private lateinit var btnToggleTheme: MaterialButton

    private lateinit var prefs: SharedPreferences

    private val historyList = mutableListOf<String>()

    private val maxHistory = 4

    private var expression: String = ""

    // Pilhas para “congelar” o contexto ao abrir parênteses
    private val opStack = ArrayDeque<String?>()
    private val valStack = ArrayDeque<Double?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        // Inicializa SharedPreferences
        prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)

        // Verifica preferência salva e aplica antes de inflar a UI
        val isDarkMode = prefs.getBoolean("isDarkMode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // TextView de display
        tvDisplay = findViewById(R.id.txtResultado)

        // Botões de dígitos
        val digits = listOf(
            "0" to R.id.btn0,
            "1" to R.id.btn1,
            "2" to R.id.btn2,
            "3" to R.id.btn3,
            "4" to R.id.btn4,
            "5" to R.id.btn5,
            "6" to R.id.btn6,
            "7" to R.id.btn7,
            "8" to R.id.btn8,
            "9" to R.id.btn9,
            "." to R.id.btnPonto
        )
        digits.forEach { (digit, id) ->
            findViewById<Button>(id).setOnClickListener { appendDigit(digit) }
        }

        // Botões de operações
        val ops = listOf(
            "+" to R.id.btnSomar,
            "-" to R.id.btnSubtrair,
            "×" to R.id.btnMultiplicar,
            "÷" to R.id.btnDividir
        )
        ops.forEach { (op, id) ->
            findViewById<Button>(id).setOnClickListener { onOperator(op) }
        }

        // Botão igual
        findViewById<Button>(R.id.btnIgual).setOnClickListener { onEquals() }

        // Botão limpar tudo
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }

        // Botão backspace
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { backspace() }

        // Botão de porcentagem
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercent() }

        // Botão de quadrado
        findViewById<Button>(R.id.btnSquare).setOnClickListener { onSquare() }

        // Botão de raiz quadrada
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { onSqrt() }

        // Botão de exponenciação
        findViewById<Button>(R.id.btnPower).setOnClickListener { onOperator("^") }

        // Botão fatorial
        findViewById<Button>(R.id.btnFatorial).setOnClickListener { onFactorial() }

        // Botão log
        findViewById<Button>(R.id.btnLog).setOnClickListener { onLog() }



        // Mapeia o botão
        btnToggleTheme = findViewById(R.id.btnToggleTheme)

        val btnHistory = findViewById<Button>(R.id.btnHistory)

        // Ajusta ícone inicial de acordo com o tema atual
        updateButtonIcon()

        // Clique do botão
        btnToggleTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                // Muda para claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.edit().putBoolean("isDarkMode", false).apply()
            } else {
                // Muda para escuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.edit().putBoolean("isDarkMode", true).apply()
            }
            // Atualiza ícone
            updateButtonIcon()
        }

        btnHistory.setOnClickListener {
            showHistoryPopup()
        }

        updateDisplay()
    }

    private fun appendDigit(d: String) {
        if (d == "." && currentInput.contains(".")) return
        currentInput = if (currentInput == "0") d else currentInput + d
        updateDisplay()
    }

    private fun onOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull()
            if (value != null) {
                if (operand == null) operand = value
                else operand = performOperation(operand!!, value, pendingOp)
            }
            currentInput = ""
        }
        pendingOp = op
        updateDisplay()
    }

    private fun onEquals() {
        if (operand != null && currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull() ?: return
            val result = performOperation(operand!!, value, pendingOp)

            // Chama a invocação do histórico
            prepareHistory(operand, value, pendingOp, result)

            operand = null
            pendingOp = null
            expression = ""
            currentInput = formatNumber(result)
            updateDisplay()
        }
    }

    private fun performOperation(a: Double, b: Double, op: String?): Double {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b == 0.0) {
                Toast.makeText(this, "Divisão por zero", Toast.LENGTH_SHORT).show()
                a
            } else a / b
            "^" -> Math.pow(a, b)
            "%" -> (a * b) / 100.0
            else -> b
        }
    }

    private fun clearAll() {
        currentInput = ""
        operand = null
        pendingOp = null
        expression = ""
        updateDisplay()
    }

    private fun backspace() {
        when {
            currentInput.isNotEmpty() -> {
                // Apaga último dígito do número atual
                currentInput = currentInput.dropLast(1)
            }
            pendingOp != null -> {
                // Remove a operação se não houver número sendo digitado
                pendingOp = null
            }
            operand != null -> {
                // Remove o operando se não houver operador
                operand = null
            }
            else -> {
                // Já está vazio, não faz nada
            }
        }
        updateDisplay()
    }

    private fun updateDisplay() {
        expression = when {
            operand != null && pendingOp != null -> {
                if (currentInput.isNotEmpty()) {
                    "${formatNumber(operand)} $pendingOp $currentInput"
                } else {
                    "${formatNumber(operand)} $pendingOp"
                }
            }
            currentInput.isNotEmpty() -> {
                currentInput
            }
            operand != null -> {
                formatNumber(operand)
            }
            else -> {
                "0"
            }
        }

        tvDisplay.text = expression
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentInput", currentInput)
        outState.putDouble("operand", operand ?: Double.NaN)
        outState.putString("pendingOp", pendingOp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentInput = savedInstanceState.getString("currentInput", "")
        val opnd = savedInstanceState.getDouble("operand", Double.NaN)
        operand = if (opnd.isNaN()) null else opnd
        pendingOp = savedInstanceState.getString("pendingOp")
        updateDisplay()
    }

    private fun updateButtonIcon() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            btnToggleTheme.text = "🌙"
        } else {
            btnToggleTheme.text = "☀️"
        }
    }

    private fun showHistoryPopup() {
        if (historyList.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Histórico de Operações")
                .setMessage("Nenhum histórico disponível")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        // Inverte a lista e adiciona numeração
        val numberedHistory = historyList
            .asReversed()
            .mapIndexed { index, item -> "${index + 1}. $item" }

        // Cria adaptador para a lista já numerada
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numberedHistory)

        // Cria o ListView dinamicamente
        val listView = ListView(this).apply {
            this.adapter = adapter
        }

        // Mostra o diálogo com o ListView
        AlertDialog.Builder(this)
            .setTitle("Histórico de Operações")
            .setView(listView)
            .setPositiveButton("Fechar", null)
            .show()
    }

    private fun prepareHistory(operand: Double?, value: Double, pendingOp: String?, result: Double) {
        if (operand == null || pendingOp == null) return

        val historyEntry = "${formatNumber(operand)} $pendingOp ${formatNumber(value)} = ${formatNumber(result)}"
        addToHistory(historyEntry)
    }

    private fun formatNumber(n: Double?): String {
        if (n == null) return "0"

        return if (n % 1.0 == 0.0) {
            n.toInt().toString() // mostra como inteiro
        } else {
            n.toString()
        }
    }
    private fun addToHistory(operation: String) {
        if (historyList.size >= maxHistory) {
            historyList.removeAt(0) // remove o mais antigo
        }
        historyList.add(operation)
    }

    private fun onPercent() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull()
            if (value != null) {
                if (operand == null) operand = value
                else operand = performOperation(operand!!, value, pendingOp)
            }
            currentInput = ""
        }
        pendingOp = "%"
        updateDisplay()
    }

    private fun onSquare() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull() ?: return
            val squared = value * value

            addUnaryHistory("x²", value, squared) // <-- histórico

            currentInput = formatNumber(squared)
            updateDisplay()
        } else if (operand != null && pendingOp == null) {
            val squared = operand!! * operand!!
            addUnaryHistory("x²", operand!!, squared) // <-- histórico

            operand = squared
            currentInput = formatNumber(squared)
            updateDisplay()
        }
    }

    private fun onSqrt() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull() ?: return
            if (value < 0) {
                Toast.makeText(this, "Raiz inválida", Toast.LENGTH_SHORT).show()
                return
            }
            val result = kotlin.math.sqrt(value)

            addUnaryHistory("√", value, result)

            currentInput = formatNumber(result)
            updateDisplay()
        } else if (operand != null && pendingOp == null) {
            if (operand!! < 0) {
                Toast.makeText(this, "Raiz inválida", Toast.LENGTH_SHORT).show()
                return
            }
            val result = kotlin.math.sqrt(operand!!)

            addUnaryHistory("√", operand!!, result)

            operand = result
            currentInput = formatNumber(result)
            updateDisplay()
        }
    }

    private fun onFactorial() {
        val value = currentInput.toDoubleOrNull()
        if (value == null || value < 0 || value % 1 != 0.0) {
            Toast.makeText(this, "Digite um número inteiro não negativo", Toast.LENGTH_SHORT).show()
            return
        }

        val n = value.toInt()
        var result = 1L
        for (i in 1..n) {
            result *= i
        }

        addUnaryHistory("!", value, result.toDouble())

        currentInput = result.toString()
        operand = null
        pendingOp = null
        updateDisplay()
    }

    // Logaritmo base 10
    private fun onLog() {
        val value = currentInput.toDoubleOrNull()
        if (value == null || value <= 0) {
            Toast.makeText(this, "Digite um número positivo", Toast.LENGTH_SHORT).show()
            return
        }

        val result = kotlin.math.log10(value)

        addUnaryHistory("log", value, result)

        currentInput = result.toString()
        operand = null
        pendingOp = null
        updateDisplay()
    }

    private fun addUnaryHistory(op: String, value: Double, result: Double) {
        val historyEntry = when (op) {
            "!" -> "${formatNumber(value)}! = ${formatNumber(result)}"
            "log" -> "log(${formatNumber(value)}) = ${formatNumber(result)}"
            "√" -> "√(${formatNumber(value)}) = ${formatNumber(result)}"
            "x²" -> "(${formatNumber(value)})² = ${formatNumber(result)}"
            "1/x" -> "1/(${formatNumber(value)}) = ${formatNumber(result)}"
            else -> "$op(${formatNumber(value)}) = ${formatNumber(result)}"
        }
        addToHistory(historyEntry)
    }
}
