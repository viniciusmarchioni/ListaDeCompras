package com.example.compras

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlin.random.Random
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = findViewById<TextView>(R.id.text)
        val editCode = findViewById<EditText>(R.id.codigo) //colocar automaticamente ao criar
        val buttonEntrar = findViewById<Button>(R.id.entrar)
        val buttonCriar = findViewById<Button>(R.id.criar)
        val buttonCompartilhar = findViewById<ImageButton>(R.id.compartilhar)
        title.isClickable = false


        try {

            val pref: SharedPreferences = getSharedPreferences("keys-xml", MODE_PRIVATE)
            editCode.setText(pref.getString("lastCode", ""))

        } catch (_: Exception) {
        }


        title.setOnClickListener {
            it.isClickable = false
            val clipboardManager: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Label", title.text)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(this, getString(R.string.copy), Toast.LENGTH_SHORT).show()
        }


        buttonEntrar.setOnClickListener {
            it.isClickable = false

            if (editCode.text.isEmpty()){
                YoYo.with(Techniques.Shake).duration(700).playOn(editCode)
                it.isClickable = true
                return@setOnClickListener
            }

            // Chame a função para verificar a existência da chave
            val chaveRef = databaseReference.child(editCode.text.toString())

            val valueEventListener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val intent = Intent(this@MainActivity, SessionActivity::class.java)
                        intent.putExtra("code", editCode.text.toString())

                        val pref: SharedPreferences = getSharedPreferences("keys-xml", MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = pref.edit()

                        editor.putString("lastCode", editCode.text.toString())
                        editor.apply()

                        chaveRef.removeEventListener(this)

                        startActivity(intent)
                    } else{
                        YoYo.with(Techniques.Shake).duration(700).playOn(editCode)
                        it.isClickable = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(editCode)
                    it.isClickable = true
                }

            }
            chaveRef.addListenerForSingleValueEvent(valueEventListener)
            chaveRef.removeEventListener(valueEventListener)



        }


        buttonCriar.setOnClickListener {
            val code = generateCode()
            databaseReference.child(code).setValue("")
            title.text = code
            title.isClickable = true
            buttonCompartilhar.isVisible = true
            buttonCompartilhar.isClickable = true
            editCode.setText(code)
            it.isClickable = false
            it.isVisible = false
        }

        buttonCompartilhar.setOnClickListener {

            // Sua string que você quer compartilhar
            val suaString = title.text.toString()

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

    private fun generateCode(): String {
        var code = ""

        for (i in 0..9) {
            val random = Random.nextInt(65, 90)
            code += random.toChar()
        }
        return code
    }
}

