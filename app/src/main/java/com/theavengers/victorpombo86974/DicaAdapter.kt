package com.theavengers.victorpombo86974;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Dica(val id: Int, val titulo: String, val descricao: String)

class DicaAdapter(
    private val context: Context,
    private var dicas: MutableList<Dica>
) : RecyclerView.Adapter<DicaAdapter.DicaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DicaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dica, parent, false)
        return DicaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DicaViewHolder, position: Int) {
        val dica = dicas[position]
        holder.tvTitulo.text = dica.titulo
        holder.tvDescricao.text = dica.descricao


        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("EXTRA_TITULO", dica.titulo)
                putExtra("EXTRA_DESCRICAO", dica.descricao)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dicas.size


    fun updateData(newDicas: List<Dica>) {
        dicas.clear() // Limpa a lista existente
        dicas.addAll(newDicas) // Adiciona os novos dados
        notifyDataSetChanged()
    }


    fun getDicaAt(position: Int): Dica {
        return dicas[position]
    }


    fun removeDicaAt(position: Int) {
        dicas.removeAt(position) // Remove a dica da lista
        notifyItemRemoved(position) // Atualiza o RecyclerView
    }

    class DicaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
    }
}
