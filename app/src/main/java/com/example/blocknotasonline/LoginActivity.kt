package com.example.blocknotasonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    companion object{
        lateinit var email: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        val currentUser = auth.currentUser

        if (currentUser != null){
            email = currentUser.email.toString()
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Para que el usuario no pueda volver hacia atras
        }
    }

    fun callSignIn(view: View){
        if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                signIn()
        } else {
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn(){
        auth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    email = etEmail.text.toString()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Para que el usuario no pueda volver hacia atras
                } else {
                    Toast.makeText(this, "Email no rregistrado", Toast.LENGTH_SHORT).show()
                }
            }
    }

   fun callRegister (view: View){
       register()
   }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun callForgotPassword(view: View){
        ForgotPassword()
    }

    private fun ForgotPassword(){
        if (etEmail.text.isNotEmpty()){
            auth.sendPasswordResetEmail(etEmail.text.toString())
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this, "Mail de recuperacion de password enviado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Email no registrado", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Ingresa un Email", Toast.LENGTH_SHORT).show()
        }
    }
}