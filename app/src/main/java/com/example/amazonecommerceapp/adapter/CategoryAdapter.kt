package com.example.amazonecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amazonecommerceapp.databinding.ItemCategoryBinding
import com.example.amazonecommerceapp.model.ShoppingCategory

class CategoryAdapter(
    val context: Context,
    private val categoryList: List<ShoppingCategory>,
    private val onCategoryClick: (ShoppingCategory) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = categoryList[position]
                    onCategoryClick(item)
                }
            }
        }

        fun bind(category: ShoppingCategory) {
            binding.categoryImage.setImageResource(category.imgSrc)
            binding.categoryName.text = category.name
        }

    }
}
