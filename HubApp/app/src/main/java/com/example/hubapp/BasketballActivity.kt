package com.example.hubapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class BasketballActivity : ComponentActivity(){
    private var pontuacaoTimeA: Int = 0
    private var pontuacaoTimeB: Int = 0
    private lateinit var pTimeA: TextView
    private lateinit var pTimeB: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.appStart("Placar de Basquete")
        setContentView(R.layout.activity_basketball)

        // Configuração dos elementos da interface
        pTimeA = findViewById(R.id.placarTimeA)
        pTimeB = findViewById(R.id.placarTimeB)

        // Configuração dos botões
        val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
        val bDoisPontosTimeA: Button = findViewById(R.id.doisPontosA)
        val bTLivreTimeA: Button = findViewById(R.id.tiroLivreA)
        val btSubtrairTimeA : Button = findViewById(R.id.negativoA)

        val bTresPontosTimeB: Button = findViewById(R.id.tresPontosB)
        val bDoisPontosTimeB: Button = findViewById(R.id.doisPontosB)
        val bTLivreTimeB: Button = findViewById(R.id.tiroLivreB)
        val btSubtrairTimeB : Button = findViewById(R.id.negativoB)

        val bReiniciar: Button = findViewById(R.id.reiniciarPartida)

        val btnHome = findViewById<Button>(R.id.btnHome)

        // Configuração do botão Home
        btnHome.setOnClickListener {
            // volta para a MainActivity
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
    }

    // Função para adicionar pontos ao placar
    fun adicionarPontos(pontos: Int, time: String) {
        LogHelper.logDebug("Placar", "Adicionando $pontos pontos ao time $time")

        if(time == "A"){
            if (pontos + pontuacaoTimeA >= 0) {
                pontuacaoTimeA += pontos
            }

        }else {
            if (pontos + pontuacaoTimeB >= 0) {
                pontuacaoTimeB += pontos
            }
        }

        LogHelper.logWarning("Placar", "Pontuação atual do time $time: $pontuacaoTimeA")
        LogHelper.logWarning("Placar", "Pontuação atual do time $time: $pontuacaoTimeB")

        atualizaPlacar(time)
    }

    // Função para atualizar o placar na interface
    fun atualizaPlacar(time: String){
        if(time == "A"){
            pTimeA.setText(pontuacaoTimeA.toString())
        }else {
            pTimeB.setText(pontuacaoTimeB.toString())
        }

        atualizaCoresPlacar()
    }

    // Função para reiniciar a partida
    fun reiniciarPartida() {
        LogHelper.logDebug("Placar", "Reiniciando a partida")
        LogHelper.logWarning("Placar", "Pontuação atual do time A: $pontuacaoTimeA")
        LogHelper.logWarning("Placar", "Pontuação atual do time B: $pontuacaoTimeB")

        pontuacaoTimeA = 0
        pTimeA.setText(pontuacaoTimeA.toString())
        pontuacaoTimeB = 0
        pTimeB.setText(pontuacaoTimeB.toString())
        Toast.makeText(this,"Placar reiniciado",
            Toast.LENGTH_SHORT).show()

        atualizaCoresPlacar()
    }

    // Função para atualizar as cores do placar de acordo com o time vencedor
    fun atualizaCoresPlacar() {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.appStop("Placar de Basquete")
    }
}