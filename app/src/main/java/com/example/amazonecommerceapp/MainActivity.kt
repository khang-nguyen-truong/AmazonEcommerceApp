package com.example.amazonecommerceapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.amazonecommerceapp.databinding.ActivityMainBinding
import com.example.amazonecommerceapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersList: MutableList<User>

    private val resultContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val responseData: Intent? = result.data
                val jsonString = responseData?.getStringExtra("user")
                val type = object : TypeToken<User>() {}.type
                val user = Gson().fromJson(jsonString, type) as User
                usersList.add(user)
                displayMessage(getString(R.string.added_user_successfully, user.name))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usersList = mutableListOf<User>(
            User("Tom", "tom@mail.com", "tom", null),
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun createAccountOnClick(view: View) {
        val intent = Intent(this, CreateAccountActivity::class.java).apply {
            val jsonString = Gson().toJson(usersList)
            putExtra("usersList", jsonString)
        }

        resultContracts.launch(intent)
    }

    fun signInButtonOnClick(view: View) {
        val username = binding.editUsername.text.toString()
        if (username.isBlank()) {
            displayMessage(
                getString(
                    R.string.field_required_error,
                    getString(R.string.edit_username_text)
                )
            )
            return
        }

        val password = binding.editPassword.text.toString()
        if (password.isBlank()) {
            displayMessage(
                getString(
                    R.string.field_required_error,
                    getString(R.string.password_text)
                )
            )
            return
        }

        val user = usersList.find { it.emailOrNumber == username && it.password == password }
        if (user == null) {
            displayMessage(getString(R.string.invalid_user_password))
            return
        }

        val intent = Intent(this, ShoppingCategoryActivity::class.java).apply {
            val jsonString = Gson().toJson(user)
            putExtra("user", jsonString)
        }
        displayMessage(getString(R.string.user_signin_successfully, user.name))
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        binding.editUsername.text.clear()
        binding.editPassword.text.clear()
        binding.showPassword.isChecked = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val jsonString = Gson().toJson(usersList)
        outState.putString("usersList", jsonString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null) {
            val jsonString = savedInstanceState.getString("usersList")
            val type = object : TypeToken<MutableList<User>>() {}.type
            usersList = Gson().fromJson(jsonString, type)
        }
    }

    private fun displayMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}