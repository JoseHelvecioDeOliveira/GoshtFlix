package com.example.goshtflix.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goshtflix.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Captura a ação do Enter no teclado
        binding.etName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val name = binding.etName.text.toString()
                if (name.isNotEmpty()) {
                    navigateToMainActivity(name)
                } else {
                    Toast.makeText(this, "Por favor, digite seu nome.", Toast.LENGTH_SHORT).show()
                }
                true // Impede que a tecla "Enter" faça a ação padrão
            } else {
                false
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val name = binding.etName.text.toString()

            if (name.isNotEmpty()) {
                navigateToMainActivity(name)
            } else {
                Toast.makeText(this, "Por favor, digite seu nome.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity(name: String) {
        val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
        // Passa o nome para a MainActivity
        intent.putExtra("USER_NAME", name)
        startActivity(intent)
        finish()

        Toast.makeText(this@WelcomeActivity, "Bem-vindo(a), $name!", Toast.LENGTH_SHORT).show()
    }
}