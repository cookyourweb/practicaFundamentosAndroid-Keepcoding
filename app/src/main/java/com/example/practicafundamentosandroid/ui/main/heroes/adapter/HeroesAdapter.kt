package  com.example.practicafundamentosandroid.ui.main.heroes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import  com.example.practicafundamentosandroid.R
import  com.example.practicafundamentosandroid.databinding.ItemHeroBinding
import  com.example.practicafundamentosandroid.model.data.Hero

interface MainAdapterCallback {
    fun onHeroClicked(hero: Hero)
}

class HeroesAdapter(private val callback: MainAdapterCallback): RecyclerView.Adapter<HeroesAdapter.MainViewHolder>() {

    private var items = listOf<Hero>()

    inner class MainViewHolder(private val binding: ItemHeroBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Hero) {
            with(binding) {
                tvHeroName.text = item.name
                pbHealth.max = item.maxHealth
                pbHealth.progress = item.currentHealth
                Glide
                    .with(root)
                    .load(item.photo)
                    .centerCrop()
                    .placeholder(R.drawable.background_element)
                    .into(ivPhoto)
                val bgColorId = if (item.isAlive()) {
                    R.color.dark_blue_semitransparent
                } else {
                    R.color.grey_semitransparent
                }
                root.setBackgroundColor(ContextCompat.getColor(root.context, bgColorId))
                root.setOnClickListener {
                    if (item.isAlive()) {
                        callback.onHeroClicked(item)
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            String.format(root.context.getString(R.string.hero_dead), item.name),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemHeroBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateList(list: List<Hero>) {
        items = list
        notifyDataSetChanged()
    }
}