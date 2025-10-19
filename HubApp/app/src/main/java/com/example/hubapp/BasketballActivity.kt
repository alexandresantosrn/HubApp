package com.example.hubapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class BasketballActivity : ComponentActivity() {

    private var pontuacaoTimeA: Int = 0
    private var pontuacaoTimeB: Int = 0
    private lateinit var pTimeA: TextView
    private lateinit var pTimeB: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.appStart("Placar de Basquete")
        setContentView(R.layout.activity_basketball)

        LogHelper.logVerbose("BasketballActivity", "onCreate iniciado.")

        try {
            // Configuração dos elementos da interface
            pTimeA = findViewById(R.id.placarTimeA)
            pTimeB = findViewById(R.id.placarTimeB)

            // Configuração dos botões
            val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
            val bDoisPontosTimeA: Button = findViewById(R.id.doisPontosA)
            val bTLivreTimeA: Button = findViewById(R.id.tiroLivreA)
            val btSubtrairTimeA: Button = findViewById(R.id.negativoA)

            val bTresPontosTimeB: Button = findViewById(R.id.tresPontosB)
            val bDoisPontosTimeB: Button = findViewById(R.id.doisPontosB)
            val bTLivreTimeB: Button = findViewById(R.id.tiroLivreB)
            val btSubtrairTimeB: Button = findViewById(R.id.negativoB)

            val bReiniciar: Button = findViewById(R.id.reiniciarPartida)
            val btnHome = findViewById<Button>(R.id.btnHome)

            LogHelper.logInfo("BasketballActivity", "Interface e botões inicializados com sucesso.")

            // Configuração do botão Home
            btnHome.setOnClickListener {
                LogHelper.logInfo("BasketballActivity", "Usuário clicou em Home — retornando à tela principal.")
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }

            // Configuração dos listeners dos botões
            bTresPontosTimeA.setOnClickListener { adicionarPontos(3, "A") }
            bDoisPontosTimeA.setOnClickListener { adicionarPontos(2, "A") }
            bTLivreTimeA.setOnClickListener { adicionarPontos(1, "A") }
            btSubtrairTimeA.setOnClickListener { adicionarPontos(-1, "A") }

            bTresPontosTimeB.setOnClickListener { adicionarPontos(3, "B") }
            bDoisPontosTimeB.setOnClickListener { adicionarPontos(2, "B") }
            bTLivreTimeB.setOnClickListener { adicionarPontos(1, "B") }
            btSubtrairTimeB.setOnClickListener { adicionarPontos(-1, "B") }

            bReiniciar.setOnClickListener { reiniciarPartida() }

        } catch (e: Exception) {
            LogHelper.logError("BasketballActivity", "Erro ao inicializar a interface: ${e.message}", e)
        }
    }

    /** Função para adicionar pontos ao placar */
    private fun adicionarPontos(pontos: Int, time: String) {
        LogHelper.logVerbose("BasketballActivity", "Adicionando $pontos pontos ao time $time")

        try {
            if (time == "A") {
                if (pontuacaoTimeA + pontos >= 0) {
                    pontuacaoTimeA += pontos
                    LogHelper.logInfo("BasketballActivity", "Nova pontuação do time A: $pontuacaoTimeA")
                } else {
                    LogHelper.logWarning("BasketballActivity", "Tentativa de pontuação negativa para o time A ignorada.")
                }
            } else {
                if (pontuacaoTimeB + pontos >= 0) {
                    pontuacaoTimeB += pontos
                    LogHelper.logInfo("BasketballActivity", "Nova pontuação do time B: $pontuacaoTimeB")
                } else {
                    LogHelper.logWarning("BasketballActivity", "Tentativa de pontuação negativa para o time B ignorada.")
                }
            }

            atualizaPlacar(time)

        } catch (e: Exception) {
            LogHelper.logError("BasketballActivity", "Erro ao adicionar pontos: ${e.message}", e)
        }
    }

    /** Atualiza o placar na interface */
    private fun atualizaPlacar(time: String) {
        LogHelper.logVerbose("BasketballActivity", "Atualizando placar para o time $time.")
        if (time == "A") {
            pTimeA.text = pontuacaoTimeA.toString()
        } else {
            pTimeB.text = pontuacaoTimeB.toString()
        }
        atualizaCoresPlacar()
    }

    /** Reinicia a partida e zera os placares */
    private fun reiniciarPartida() {
        LogHelper.logWarning("BasketballActivity", "Reiniciando a partida.")
        pontuacaoTimeA = 0
        pontuacaoTimeB = 0
        pTimeA.text = pontuacaoTimeA.toString()
        pTimeB.text = pontuacaoTimeB.toString()
        Toast.makeText(this, "Placar reiniciado", Toast.LENGTH_SHORT).show()
        atualizaCoresPlacar()
    }

    /** Atualiza as cores dos placares conforme o time vencedor */
    private fun atualizaCoresPlacar() {
        try {
            when {
                pontuacaoTimeA > pontuacaoTimeB -> {
                    pTimeA.setBackgroundColor(getColor(R.color.colorOnSurface))
                    pTimeA.setTextColor(getColor(R.color.colorOnPrimary))
                    pTimeB.setBackgroundColor(getColor(R.color.colorBackground))
                    pTimeB.setTextColor(getColor(R.color.colorPrimary))
                }

                pontuacaoTimeB > pontuacaoTimeA -> {
                    pTimeB.setBackgroundColor(getColor(R.color.colorOnSurface))
                    pTimeB.setTextColor(getColor(R.color.colorOnPrimary))
                    pTimeA.setBackgroundColor(getColor(R.color.colorBackground))
                    pTimeA.setTextColor(getColor(R.color.colorPrimary))
                }

                else -> {
                    pTimeA.setBackgroundColor(getColor(R.color.colorBackground))
                    pTimeB.setBackgroundColor(getColor(R.color.colorBackground))
                    pTimeA.setTextColor(getColor(R.color.colorPrimary))
                    pTimeB.setTextColor(getColor(R.color.colorPrimary))
                }
            }
        } catch (e: Exception) {
            LogHelper.logError("BasketballActivity", "Erro ao atualizar cores do placar: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.logWarning("BasketballActivity", "onDestroy chamado — encerrando a Activity.")
        LogHelper.appStop("Placar de Basquete")
    }
}
