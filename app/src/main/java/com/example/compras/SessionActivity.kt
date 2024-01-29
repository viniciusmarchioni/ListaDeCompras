package com.example.compras

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.compras.Adapter.AdapterProduto
import com.example.compras.databinding.ActivitySessionBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tsuryo.swipeablerv.SwipeLeftRightCallback

class SessionActivity : AppCompatActivity() {

    private val listaProdutos: MutableList<Produtos> = mutableListOf()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var binding: ActivitySessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mp = MediaPlayer.create(this, R.raw.click)
        val suj = resources.getStringArray(R.array.sujestoes)
        binding.sessioncode.text = intent.getStringExtra("code")
        val pref: SharedPreferences = getSharedPreferences("config-xml", MODE_PRIVATE)

        // Configurando adapter do Auto Complete
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suj)
        binding.editnome.setAdapter(arrayAdapter)

        // Configurando adapter do recyclerView
        val adapterProduto = AdapterProduto(this, listaProdutos)
        binding.list.adapter = adapterProduto

        // Configurando recyclerView NOVA
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                binding.layoutfora.isVisible = true
                binding.layoutfora.isClickable = true
                binding.editnome.setText(listaProdutos[position].nome)
                binding.editmarca.setText(listaProdutos[position].marca)
                binding.editqnt.setText(listaProdutos[position].qnt)
                listaProdutos.removeAt(position)
                adapterProduto.notifyItemRemoved(position)
            }

            override fun onSwipedRight(position: Int) {
                //remover o valor que for filho do código da sessão com o nome do produto escolhido
                db.child("${binding.sessioncode.text}/${listaProdutos[position].nome}")
                    .removeValue()
                listaProdutos.removeAt(position)
                adapterProduto.notifyItemRemoved(position)
            }
        })
        refreshList(binding.sessioncode.text.toString(), adapterProduto)

        // Configurando adapter spinner
        binding.spinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                arrayOf("unid", "kg", "g", "L", "ml")
            )

        binding.backbutton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.refresh.setOnRefreshListener {
            refreshList(binding.sessioncode.text.toString(), adapterProduto)

            binding.refresh.isRefreshing = false
        }

        binding.button.setOnClickListener {
            binding.layoutfora.isVisible = true
            binding.layoutfora.isClickable = true
        }

        binding.addbutton.setOnClickListener {

            it.isClickable = false


            if (binding.editnome.text.isEmpty() || binding.editqnt.text.isEmpty()) {
                it.isClickable = true
                return@setOnClickListener
            }

            val produto = Produtos(
                binding.editnome.text.toString(),
                binding.editqnt.text.toString(),
                binding.spinner.selectedItem.toString(),
                binding.editmarca.text.toString()
            )

            listaProdutos.add(produto)
            adapterProduto.notifyItemInserted(listaProdutos.size)
            if (pref.getBoolean("sound", true)) //verificando o arq se estiver faz som
                mp.start()
            saveProduct(produto, binding.sessioncode.text.toString())//try pq pode dar timeout
            binding.layoutfora.isVisible = false
            binding.layoutfora.isClickable = false
            binding.editnome.text.clear()
            binding.editqnt.text.clear()
            binding.editmarca.text.clear()
            it.isClickable = true
        }

        binding.layoutfora.setOnClickListener {
            it.isVisible = false
            it.isClickable = false
            binding.button.isClickable = true
        }


    }

    private fun refreshList(path: String, adapter: AdapterProduto) {
        listaProdutos.clear()

        val valueEventListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    for (filho in snapshot.children) {
                        if (filho.hasChildren()) {
                            val obj = Produtos()
                            for (dado in filho.children) {
                                val valorDoDado =
                                    dado.getValue(String::class.java)
                                when (dado.key) {
                                    "nome" -> {
                                        obj.nome = valorDoDado
                                    }

                                    "qnt" -> {
                                        obj.qnt = valorDoDado
                                    }

                                    "notation" -> {
                                        obj.notation = valorDoDado
                                    }

                                    "marca" -> {
                                        obj.marca = valorDoDado
                                    }
                                }
                            }
                            listaProdutos.add(obj)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        db.child(path).addListenerForSingleValueEvent(valueEventListener)
        db.removeEventListener(valueEventListener)
    }

    private fun saveProduct(produtos: Produtos, path: String) {
        db.child(path).child(produtos.nome.toString()).setValue(produtos)
            .addOnCompleteListener {
                Toast.makeText(this, getString(R.string.add_notification), Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {

            }
    }
}
