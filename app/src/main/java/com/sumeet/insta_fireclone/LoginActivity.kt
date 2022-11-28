package com.sumeet.insta_fireclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sumeet.insta_fireclone.daos.UserDao
import com.sumeet.insta_fireclone.models.User
import kotlinx.android.synthetic.main.activity_login.*


private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val auth = FirebaseAuth.getInstance()

        /**if(auth.currentUser != null) {
        val currentUser = auth.currentUser
        updateUI(currentUser)
        }**/

        // Implementing Sign Up Activity
        btnSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }


        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(email.isBlank() || password.isBlank()) {
                Toast.makeText(this,"Email/Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase authentication check


            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                btnLogin.isEnabled = true
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user,email)
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.e(TAG,"signInWithEmail failed",task.exception)
                    Toast.makeText(this,"Authentication failed", Toast.LENGTH_SHORT).show()
                    updateUI(null, null.toString())
                }
            }

        }
    }


    private fun updateUI(currentUser: FirebaseUser?, email:String) {
        if(currentUser == null) {
            btnLogin.visibility = View.VISIBLE
            return
        }
        val user = User(currentUser.uid,email)
        val userDao = UserDao()
        userDao.addUser(user)
        val postIntent = Intent(this,PostsActivity::class.java)
        startActivity(postIntent)
        finish()
    }
}