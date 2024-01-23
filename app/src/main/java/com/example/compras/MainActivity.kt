package com.example.compras

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.text.set
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val title = findViewById<TextView>(R.id.text)
        val editCode = findViewById<EditText>(R.id.codigo) //colocar automaticamente ao criar
        val buttonEntrar = findViewById<Button>(R.id.entrar)
        val buttonCriar = findViewById<Button>(R.id.criar)
        val buttonCompartilhar = findViewById<ImageButton>(R.id.compartilhar)


        try {
            val pref:SharedPreferences = getSharedPreferences("keys-xml", MODE_PRIVATE)
            editCode.setText(pref.getString("lastCode",""))

        }catch (_:Exception){
        }


        title.setOnClickListener {
            //Copiar o Código ao clicar ao clicar
        }


        buttonEntrar.setOnClickListener {

            if (editCode.text.toString().length == 0) {
                val intent = Intent(this@MainActivity, SessionActivity::class.java)
                intent.putExtra("code", "LAJSIDHSUGA")

                startActivity(intent)
            }

            if (editCode.text.toString().length < 10) {
                return@setOnClickListener
            }


            val db: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(editCode.text.toString())
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val intent = Intent(this@MainActivity, SessionActivity::class.java)
                        intent.putExtra("code", editCode.text.toString())


                        val pref:SharedPreferences = getSharedPreferences("keys-xml", MODE_PRIVATE)
                        val editor:SharedPreferences.Editor = pref.edit()

                        editor.putString("lastCode",editCode.text.toString())
                        editor.apply()




                        startActivity(intent)


                    } else {
                        Log.d("TAG", "Não existe") //animação
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }


        buttonCriar.setOnClickListener {
            buttonCompartilhar.isVisible = true
            buttonCompartilhar.isClickable = true

        }

        buttonCompartilhar.setOnClickListener {

            // Sua string que você quer compartilhar
            val suaString = "XQYHNMTSRS"

            // Criar uma Intent
            val intent = Intent(Intent.ACTION_SEND)

            // Definir o tipo de conteúdo da Intent
            intent.type = "text/plain"

            // Adicionar a string à Intent
            intent.putExtra(Intent.EXTRA_TEXT, suaString)

            // Iniciar a Activity de compartilhamento
            startActivity(Intent.createChooser(intent, ""))
        }


    }
}