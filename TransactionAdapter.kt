package uz.moifinansy.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class TransactionAdapter(
    private val items: List<TransactionItem>,
    private val onItemLongClick: (TransactionItem) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val decimalFormat = DecimalFormat("#,###")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvItemCategory)
        val tvComment: TextView = view.findViewById(R.id.tvItemComment)
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
        val tvAmount: TextView = view.findViewById(R.id.tvItemAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvCategory.text = item.category
        holder.tvComment.text = item.comment
        holder.tvDate.text = item.date

        if (item.type == "income") {
            holder.tvAmount.text = "+${decimalFormat.format(item.amount)} сум"
            holder.tvAmount.setTextColor(0xFF10B981.toInt()) // Emerald
        } else {
            holder.tvAmount.text = "-${decimalFormat.format(item.amount)} сум"
            holder.tvAmount.setTextColor(0xFFEF4444.toInt()) // Red
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = items.size
}
