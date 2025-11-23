package com.giotech.embeddedrfidlogin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.giotech.embeddedrfidlogin.databinding.RfidItemBinding
import java.text.SimpleDateFormat
import java.util.*

class RfidAdapter(
    private val onClick: (RfidEntry) -> Unit = {},
    private val onNewItem: (() -> Unit) // Callback to scroll RecyclerView to top
) : RecyclerView.Adapter<RfidAdapter.VH>() {

    companion object {
        private val dateFormat by lazy {
            SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        }
    }

    private val items = mutableListOf<RfidEntry>()
    private val animatedIds = mutableSetOf<String>() // Track animated items

    inner class VH(private val b: RfidItemBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: RfidEntry) {
            b.tvName.text = item.name
            b.tvUid.text = "UID: ${item.uid}"
            b.tvDate.text = dateFormat.format(Date(item.timestamp()))
            b.dot.alpha = 1f
            b.root.setOnClickListener { onClick(item) }
        }

        fun animateIfNew() {
            if (items.any { it.uid !in animatedIds }) {
                // Scroll to top callback
                onNewItem.invoke()

                // Blinking blue animation
                b.root.setCardBackgroundColor(ContextCompat.getColor(b.root.context, R.color.mono_500)) // blue
                val blinkCount = 3
                val blinkDuration = 200L
                for (i in 0 until blinkCount) {
                    b.root.animate()
                        .alpha(0.3f)
                        .setStartDelay(i * blinkDuration * 2)
                        .setDuration(blinkDuration)
                        .withEndAction {
                            b.root.alpha = 1f
                        }
                        .start()
                }
                b.root.postDelayed({
                    b.root.setCardBackgroundColor(ContextCompat.getColor(b.root.context, R.color.white)) // transparent
                }, blinkCount * blinkDuration * 2)

                animatedIds.clear()
                animatedIds.addAll(items.map { it.uid })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RfidItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.animateIfNew()
    }

    override fun getItemCount(): Int = items.size

    // Update list and trigger animations for new items
    fun submitList(list: List<RfidEntry?>?) {
        val sorted = list?.filterNotNull()?.sortedByDescending { it.timestamp() } ?: emptyList()
        val newItems = sorted.filter { it.uid !in items.map { old -> old.uid } }

        items.clear()
        items.addAll(sorted)

        notifyDataSetChanged()

        // Animate newly added items
        newItems.forEach { newItem ->
            val position = items.indexOf(newItem)
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
    }
}
