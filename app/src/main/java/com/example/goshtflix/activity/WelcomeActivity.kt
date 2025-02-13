package com.example.goshtflix.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.goshtflix.dao.AppDatabase
import com.example.goshtflix.databinding.ActivityWelcomeBinding
import com.example.goshtflix.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            val name = binding.etName.text.toString()

            if (name.isNotEmpty()) {
                // Inicia a Coroutine para salvar o usuário no banco
//
//                val db = Room.databaseBuilder(
//                    applicationContext,
//                    AppDatabase::class.java, "goshtflix"
//                ).build()


                lifecycleScope.launch {
                    // Acesso ao banco de dados fora da thread principal (usando Dispatchers.IO)
                   // val db = withContext(Dispatchers.IO) {
                  //      AppDatabase.getDatabase(this@WelcomeActivity)
//                    }
//
//                    val userDao = db.userDao()
//                    userDao.insertAll(User(uid = 0, firstName = name))

                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@WelcomeActivity, "Bem-vindo(a), $name!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Caso o nome esteja vazio, avisa ao usuário
                Toast.makeText(this, "Por favor, digite seu nome.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
