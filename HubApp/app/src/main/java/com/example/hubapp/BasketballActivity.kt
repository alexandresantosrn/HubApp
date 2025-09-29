package com.example.hubapp

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
        setContentView(R.layout.activity_basketball)

        pTimeA = findViewById(R.id.placarTimeA)
        pTimeB = findViewById(R.id.placarTimeB)

        val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
        val bDoisPontosTimeA: Button = findViewById(R.id.doisPontosA)
        val bTLivreTimeA: Button = findViewById(R.id.tiroLivreA)
        val btSubtrairTimeA : Button = findViewById(R.id.negativoA)

        val bTresPontosTimeB: Button = findViewById(R.id.tresPontosB)
        val bDoisPontosTimeB: Button = findViewById(R.id.doisPontosB)
        val bTLivreTimeB: Button = findViewById(R.id.tiroLivreB)
        val btSubtrairTimeB : Button = findViewById(R.id.negativoB)

        val bReiniciar: Button = findViewById(R.id.reiniciarPartida)

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

    fun adicionarPontos(pontos: Int, time: String) {
        if(time == "A"){
            if (pontos + pontuacaoTimeA >= 0) {
                pontuacaoTimeA += pontos
            }

        }else {
            if (pontos + pontuacaoTimeB >= 0) {
                pontuacaoTimeB += pontos
            }
        }

        atualizaPlacar(time)
    }

    fun atualizaPlacar(time: String){
        if(time == "A"){
            pTimeA.setText(pontuacaoTimeA.toString())
        }else {
            pTimeB.setText(pontuacaoTimeB.toString())
        }

        atualizaCoresPlacar()
    }

    fun reiniciarPartida() {
        pontuacaoTimeA = 0
        pTimeA.setText(pontuacaoTimeA.toString())
        pontuacaoTimeB = 0
        pTimeB.setText(pontuacaoTimeB.toString())
        Toast.makeText(this,"Placar reiniciado",
            Toast.LENGTH_SHORT).show()

        atualizaCoresPlacar()
    }

    fun atualizaCoresPlacar() {
        when {
            pontuacaoTimeA > pontuacaoTimeB -> {
                pTimeA.setBackgroundColor(getColor(R.color.colorSecondary))
                pTimeA.setTextColor(getColor(R.color.colorPrimary))
                pTimeB.setBackgroundColor(getColor(android.R.color.darker_gray))
                pTimeB.setTextColor(getColor(R.color.colorSecondary))
            }
            pontuacaoTimeB > pontuacaoTimeA -> {
                pTimeB.setBackgroundColor(getColor(R.color.colorSecondary))
                pTimeB.setTextColor(getColor(R.color.colorPrimary))
                pTimeA.setBackgroundColor(getColor(android.R.color.darker_gray))
                pTimeA.setTextColor(getColor(R.color.colorSecondary))
            }
            else -> {
                pTimeA.setBackgroundColor(getColor(android.R.color.darker_gray))
                pTimeB.setBackgroundColor(getColor(android.R.color.darker_gray))
                pTimeA.setTextColor(getColor(R.color.colorSecondary))
                pTimeB.setTextColor(getColor(R.color.colorSecondary))
            }
        }
    }
}