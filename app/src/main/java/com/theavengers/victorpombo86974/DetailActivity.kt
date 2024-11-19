package com.theavengers.victorpombo86974;

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val titulo = intent.getStringExtra("EXTRA_TITULO")
        val descricao = intent.getStringExtra("EXTRA_DESCRICAO")


        findViewById<TextView>(R.id.tvTituloDetail).text = titulo
        findViewById<TextView>(R.id.tvDescricaoDetail).text = descricao
    }


    override fun onSupportNavigateUp(): Boolean {
        finish() // Fecha a atividade atual e retorna à anterior
        return true
    }
}
