package com.example.amazonecommerceapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.amazonecommerceapp.databinding.ActivityCreateAccountBinding
import com.example.amazonecommerceapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var usersList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra("usersList")
        val type = object : TypeToken<MutableList<User>>() {}.type
        usersList = Gson().fromJson(jsonString, type)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun continueButtonOnClick(view: View) {
        val name = binding.editNameText.text.toString()
        if (name.isBlank()) {
            displayMessage(getString(R.string.field_required_error, getString(R.string.your_name)))
            return
        }

        val emailOrNumber = binding.editMobileEmailText.text.toString()
        if (emailOrNumber.isBlank()) {
            displayMessage(
                getString(
                    R.string.field_required_error,
                    getString(R.string.mobile_number_or_email)
                )
            )
            return
        }

        val password = binding.editPasswordText.text.toString()
        if (password.isBlank()) {
            displayMessage(getString(R.string.field_required_error, getString(R.string.password)))
            return
        }

        val reenterPassword = binding.editReEnterPasswordText.text.toString()
        if (password != reenterPassword) {
            displayMessage(getString(R.string.password_mismatch_error))
            return
        }

        val userExists = usersList.any { it.name.equals(name, true) }
        if (userExists) {
            displayMessage(getString(R.string.user_exists_error, name))
            return
        }

        val emailOrNumberExists = usersList.any { it.emailOrNumber.equals(emailOrNumber) }
        if (emailOrNumberExists) {
            displayMessage(getString(R.string.email_number_exists_error, emailOrNumber))
            return
        }

        // Go back to login page with User data
        val jsonString = Gson().toJson(User(name, emailOrNumber, password, null))
        val responseIntent = Intent().apply {
            putExtra("user", jsonString)
        }
        setResult(Activity.RESULT_OK, responseIntent)
        finish()
    }

    private fun displayMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        binding.editNameText.text.clear()
        binding.editMobileEmailText.text.clear()
        binding.editPasswordText.text.clear()
        binding.editReEnterPasswordText.text.clear()
    }
}