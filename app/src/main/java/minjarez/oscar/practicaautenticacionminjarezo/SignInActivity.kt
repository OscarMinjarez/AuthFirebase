package minjarez.oscar.practicaautenticacionminjarezo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.auth = FirebaseAuth.getInstance()
        val email: EditText = findViewById(R.id.etMail)
        val password: EditText = findViewById(R.id.etPassword)
        val confirmPassword: EditText = findViewById(R.id.etConfirmPassword)
        val error: TextView = findViewById(R.id.tvError)
        val button: Button = findViewById(R.id.btnRegister)
        error.visibility = View.INVISIBLE
        button.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty()) {
                error.text = "Not empty values"
                error.visibility = View.VISIBLE
            } else if (!password.text.toString().equals(confirmPassword.text.toString())) {
                error.text = "Password must be the same"
                error.visibility = View.VISIBLE
            } else {
                error.visibility = View.INVISIBLE
                signIn(email.text.toString(), password.text.toString())
            }
        }

    }

    fun signIn(email: String, password: String) {
        Log.d("INFO", "email: $email, password: ${password}")
        this.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.w("ERROR", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "El registro falló.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}