package com.example.compras

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.Adapter.AdapterProduto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SessionActivity : AppCompatActivity() {

    val listaProdutos: MutableList<Produtos> = mutableListOf()
    val db: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)


        val code = findViewById<TextView>(R.id.sessioncode)
        val button = findViewById<FloatingActionButton>(R.id.button)
        val backbutton = findViewById<ImageButton>(R.id.backbutton)
        val list = findViewById<RecyclerView>(R.id.list)
        val spinner = findViewById<Spinner>(R.id.spinner)


        code.text = intent.getStringExtra("code")


        //config adapter
        val adapterProduto = AdapterProduto(this, listaProdutos)
        list.adapter = adapterProduto


        //  ATUALIZAR RECYCLEVIEW COM OS DADOS DO FIREBASE
        val caminhoNoBancoDeDados = intent.getStringExtra("code")

        // Adicione um ouvinte para ler os dados
        db.child(caminhoNoBancoDeDados.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {

                    //entra no caminho e percorre os arrays de filhos(produtos)
                    for (filho in snapshot.children) {

                        //verifica se o filho (produto) tem mais filhos(parâmetros)
                        if (filho.hasChildren()) {
                            val obj = Produtos() //cria um obj para armazenar os resultados

                            //é retornado uma tabela rash, por isso é preciso percorrer ela
                            for (dado in filho.children) {
                                val valorDoDado =
                                    dado.getValue(String::class.java) // Obtém o valor do dado

                                //verifica o nome da chave para adicionar no lugar certo no obj
                                if (dado.key == "nome") {
                                    obj.nome = valorDoDado
                                } else if (dado.key == "qnt") {
                                    obj.qnt = valorDoDado
                                } else if (dado.key == "notation") {
                                    obj.notation = valorDoDado
                                } else if (dado.key == "marca") {
                                    obj.marca = valorDoDado
                                }
                                //vai para o próximo filho(produto) se existir...
                            }
                            //add na recycle view os produtos
                            listaProdutos.add(obj)
                            adapterProduto.notifyItemInserted(listaProdutos.size)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Erro ao ler dados do banco de dados: ${error.message}")
            }

        })


        //Swipe
        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val nomeremovido = listaProdutos[viewHolder.adapterPosition].nome

                db.child("$caminhoNoBancoDeDados/$nomeremovido").removeValue()

                listaProdutos.removeAt(viewHolder.adapterPosition)
                adapterProduto.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(list)


        //Spinner Config
        spinner.adapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayOf("UNID", "kg", "g", "L", "ml")//adicionando opções ao spinner
            )


        //pode ser que não seja preciso...
        /*spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                return
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }

        }*/



        backbutton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        button.setOnClickListener {
            val layoutFora = findViewById<RelativeLayout>(R.id.layoutfora)
            val layoutDentro = findViewById<RelativeLayout>(R.id.layoutdentro)
            val buttonAdd = findViewById<Button>(R.id.addbutton)
            val nome = findViewById<AutoCompleteTextView>(R.id.editnome)
            val qnt = findViewById<EditText>(R.id.editqnt)
            val marca = findViewById<EditText>(R.id.editmarca)

            layoutFora.isVisible = true
            layoutFora.isClickable = true

            val suj = resources.getStringArray(R.array.sujestoes)//pegando o array do arq strings
            //configurnado adapter do Auto Complete
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suj)
            nome.setAdapter(arrayAdapter)

            layoutFora.setOnClickListener {
                it.isVisible = false
                it.isClickable = false

            }
            layoutDentro.setOnClickListener {

            }
            buttonAdd.setOnClickListener {

                val produto = Produtos(
                    nome.text.toString(),
                    qnt.text.toString(),
                    spinner.selectedItem.toString(),
                    marca.text.toString()
                )

                saveProduct(produto, caminhoNoBancoDeDados.toString())
                listaProdutos.add(produto)

                adapterProduto.notifyItemInserted(listaProdutos.size)

                layoutFora.isVisible = false
                layoutFora.isClickable = false

                nome.text.clear()
                qnt.text.clear()
                marca.text.clear()

            }

        }


    }

    private fun saveProduct(produtos: Produtos, path: String) {

        db.child(path).child(produtos.nome.toString()).setValue(produtos)
            .addOnCompleteListener {
                Toast.makeText(this, "Adicionado!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {

            }

    }


}