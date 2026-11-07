package uz.moifinansy.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TransactionItem(
    val id: String,
    val type: String, // "income" or "expense"
    val amount: Long,
    val category: String,
    val comment: String,
    val date: String
)

class MainActivity : AppCompatActivity() {

    private val transactions = mutableListOf<TransactionItem>()
    private lateinit var tvBalance: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpense: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private val prefs by lazy { getSharedPreferences("moifinansy_prefs", MODE_PRIVATE) }
    private val gson = Gson()
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBalance = findViewById(R.id.tvBalance)
        tvIncome = findViewById(R.id.tvIncome)
        tvExpense = findViewById(R.id.tvExpense)
        rvHistory = findViewById(R.id.rvHistory)

        rvHistory.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(transactions) { item ->
            // Delete confirmation
            AlertDialog.Builder(this)
                .setTitle("Удалить операцию?")
                .setMessage("${item.category}: ${decimalFormat.format(item.amount)} сум")
                .setPositiveButton("Удалить") { _, _ ->
                    transactions.remove(item)
                    saveData()
                    updateUI()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
        rvHistory.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            showAddDialog()
        }

        loadData()
        updateUI()
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etComment = dialogView.findViewById<EditText>(R.id.etComment)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rgType)

        AlertDialog.Builder(this)
            .setTitle("Новая операция")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val amountStr = etAmount.text.toString().trim()
                val category = etCategory.text.toString().trim().ifEmpty { "Операция" }
                val comment = etComment.text.toString().trim()
                val isIncome = rgType.checkedRadioButtonId == R.id.rbIncome

                if (amountStr.isEmpty()) {
                    Toast.makeText(this, "Введите сумму!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val amount = amountStr.toLongOrNull() ?: 0L
                val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val item = TransactionItem(
                    id = System.currentTimeMillis().toString(),
                    type = if (isIncome) "income" else "expense",
                    amount = amount,
                    category = category,
                    comment = comment,
                    date = sdf.format(Date())
                )
                transactions.add(0, item)
                saveData()
                updateUI()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun updateUI() {
        var totalIncome = 0L
        var totalExpense = 0L
        for (item in transactions) {
            if (item.type == "income") totalIncome += item.amount
            else totalExpense += item.amount
        }
        val balance = totalIncome - totalExpense

        tvBalance.text = "${decimalFormat.format(balance)} сум"
        tvIncome.text = "+${decimalFormat.format(totalIncome)} сум"
        tvExpense.text = "-${decimalFormat.format(totalExpense)} сум"
        adapter.notifyDataSetChanged()
    }

    private fun saveData() {
        val json = gson.toJson(transactions)
        prefs.edit().putString("saved_transactions", json).apply()
    }

    private fun loadData() {
        val json = prefs.getString("saved_transactions", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<TransactionItem>>() {}.type
            val savedList: List<TransactionItem> = gson.fromJson(json, type)
            transactions.clear()
            transactions.addAll(savedList)
        } else {
            // Initial data in Uzbek Som
            transactions.add(TransactionItem("1", "income", 8500000, "Зарплата", "Основной доход", "01.05.2026"))
            transactions.add(TransactionItem("2", "expense", 350000, "Продукты", "Корзинка маркет", "02.05.2026"))
            transactions.add(TransactionItem("3", "expense", 25000, "Такси", "Яндекс Такси", "03.05.2026"))
        }
    }
}
