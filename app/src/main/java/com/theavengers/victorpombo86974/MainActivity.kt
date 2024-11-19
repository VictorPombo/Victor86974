package com.theavengers.victorpombo86974;

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theavengers.victorpombo86974.DicaAdapter
import com.theavengers.victorpombo86974.DicaDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DicaAdapter
    private lateinit var databaseHelper: DicaDatabaseHelper
    private lateinit var etTitulo: EditText
    private lateinit var etDescricao: EditText
    private lateinit var btnAdicionarDica: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DicaDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        etTitulo = findViewById(R.id.etTitulo)
        etDescricao = findViewById(R.id.etDescricao)
        btnAdicionarDica = findViewById(R.id.btnAdicionarDica)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DicaAdapter(this, databaseHelper.getAllDicas().toMutableList())
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredDicas = databaseHelper.getAllDicas().filter {
                    it.titulo.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateData(filteredDicas)
                return true
            }
        })


        databaseHelper.insertDica("Desligue aparelhos que não estão em uso", "Aparelhos consomem energia mesmo em espera.")
        databaseHelper.insertDica("Use lâmpadas LED", "Lâmpadas LED economizam energia e duram mais.")


        adapter.updateData(databaseHelper.getAllDicas())


        databaseHelper.removeDuplicatas()


        btnAdicionarDica.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descricao = etDescricao.text.toString().trim()

            if (titulo.isNotEmpty() && descricao.isNotEmpty()) {

                databaseHelper.insertDica(titulo, descricao)

                adapter.updateData(databaseHelper.getAllDicas().toMutableList())

                etTitulo.text.clear()
                etDescricao.text.clear()


                Toast.makeText(this, "Dica adicionada!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val dica = adapter.getDicaAt(position)

                databaseHelper.deleteDica(dica.id)
                adapter.removeDicaAt(position)

                Toast.makeText(this@MainActivity, "Dica excluída!", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
