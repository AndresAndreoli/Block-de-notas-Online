package com.example.blocknotasonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var eMail: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var name: EditText
    private lateinit var lastName: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        eMail = findViewById(R.id.etRegisterEmail)
        password = findViewById(R.id.etRegisterPassword)
        confirmPassword = findViewById(R.id.etRegisterConfirmPassword)
        name = findViewById(R.id.etRegisterName)
        lastName = findViewById(R.id.etRegisterLastName)

        eMail.doOnTextChanged { text, start, before, count -> manageButtonRegister() }
    }

    private fun manageButtonRegister(){
        btnRegister = findViewById(R.id.btnRegisterSignUp)

        if (password.text.isEmpty() || confirmPassword.text.isEmpty() || name.text.isEmpty() || lastName.text.isEmpty() || !ValidateEmail.isEmail(eMail.text.toString())){
            btnRegister.isEnabled = false
        } else {
            btnRegister.isEnabled = true
        }
    }

    fun callCancel (view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun callSignUp(view: View){
        if (eMail.text.isNotEmpty() && password.text.isNotEmpty() && confirmPassword.text.isNotEmpty() && name.text.isNotEmpty() && lastName.text.isNotEmpty()){
            if (password.text.toString()==confirmPassword.text.toString()){
                signUp()
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp(){
        auth.createUserWithEmailAndPassword(eMail.text.toString(), password.text.toString())
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()

                    // Cargar datos BD
                    var dateToday = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    var db = FirebaseFirestore.getInstance()

                    db.collection("users").document(eMail.text.toString()).set(
                        hashMapOf(
                        "name" to name.text.toString(),
                        "lastName" to lastName.text.toString(),
                        "date" to dateToday.toString()
                        )
                    )

                    finish()
                } else {
                    Toast.makeText(this, "Algo salio mal. Intenta nuevamente", Toast.LENGTH_SHORT).show()
                }
            }
    }
}