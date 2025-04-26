package minjarez.oscar.practicaautenticacionminjarezo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.auth = FirebaseAuth.getInstance()
        val email: EditText = findViewById(R.id.etMail)
        val password: EditText = findViewById(R.id.etPassword)
        val error: TextView = findViewById(R.id.tvError)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        error.visibility = View.INVISIBLE
        btnLogin.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty()) {
                error.text = "Not empty values"
                error.visibility = View.VISIBLE
            } else {
                error.visibility = View.INVISIBLE
                login(email.text.toString(), password.text.toString())
            }
        }
        btnRegister.setOnClickListener {
            this.goToRegister()
        }
    }

    private fun goToRegister() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun goToMain(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showError(text: String = "", visible: Boolean) {
        val error: TextView = findViewById(R.id.tvError)
        error.text = text
        error.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = this.auth.currentUser
        if (currentUser != null) {
            this.goToMain(currentUser)
        }
    }

    private fun login(email: String, password: String) {
        this.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = this.auth.currentUser
                    this.showError(visible = false)
                    this.goToMain(user!!)
                } else {
                    this.showError("Invalid user or password", true)
                }
            }
    }
}