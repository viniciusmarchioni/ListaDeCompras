package com.viniciusmarchioni.compras.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.viniciusmarchioni.compras.Produtos
class AdapterProduto(private val context: Context, private val produtos: MutableList<Produtos>) :
    RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val item =
            LayoutInflater.from(context).inflate(R.layout.holder_recycle_layout, parent, false)
        return ProdutoViewHolder(item)
    }

    override fun getItemCount(): Int = produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.nome.text = produtos[position].nome
        holder.qnt.text = produtos[position].qnt
        holder.marca.text = produtos[position].marca
        holder.notation.text = produtos[position].notation
    }


    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome = itemView.findViewById<TextView>(R.id.textnome)
        val qnt = itemView.findViewById<TextView>(R.id.textqnt)
        val marca = itemView.findViewById<TextView>(R.id.textmarca)
        val notation = itemView.findViewById<TextView>(R.id.notation)
    }
}