package com.example.amazonecommerceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.amazonecommerceapp.adapter.CategoryAdapter
import com.example.amazonecommerceapp.databinding.ActivityShoppingCategoryBinding
import com.example.amazonecommerceapp.model.ShoppingCategory
import com.example.amazonecommerceapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShoppingCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingCategoryBinding
    private lateinit var user: User
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra("user")
        val type = object : TypeToken<User>() {}.type
        user = Gson().fromJson(jsonString, type)

        binding = ActivityShoppingCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeText.text = getString(R.string.welcome_text, user.name)

        val categoryList =
            listOf<ShoppingCategory>(
                ShoppingCategory("Home and Kitchen", R.drawable.home_kitchen),
                ShoppingCategory("Beauty and Personal Care", R.drawable.beauty_care),
                ShoppingCategory("Pet supplies", R.drawable.pet_supplies),
                ShoppingCategory("Toys and Games", R.drawable.toys_games)
            )

        categoryAdapter = CategoryAdapter(this, categoryList) { category ->
            Toast.makeText(
                this,
                getString(R.string.category_selected, category.name),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.shoplistCategory.layoutManager = GridLayoutManager(this, 2)
        binding.shoplistCategory.adapter = categoryAdapter
    }
}