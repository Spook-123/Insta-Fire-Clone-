package com.sumeet.insta_fireclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()

        btnDone.setOnClickListener {

            val email = etSignUpUsername.text.toString()
            val pass = etSignUpPassword1.text.toString()
            val confirmPass = etSignUpPassword2.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this,"Sign Up Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this,"Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this,"Empty Fields are not Allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}