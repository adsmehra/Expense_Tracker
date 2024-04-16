package com.example.expensetracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ActivityMainBinding
import com.example.expensetracker.databinding.ExpenseAdderBinding
import com.example.expensetracker.databinding.TransactionRowBinding
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DbHelper

    private lateinit var arrayList: ArrayList<Transaction>

    private lateinit var adapterTransaction: TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        dbHelper= DbHelper(this)

        loadData()

        loadSum()

        setSwipetoDelete()



        binding.graphView.setOnClickListener{
            startActivity(Intent(this@MainActivity, GraphActivity::class.java))
        }

        binding.addTransaction.setOnClickListener {
            addExpenseDialog()
        }

    }

    private fun loadSum() {
        val sumExpense=dbHelper.sumCal()
        binding.sumExpense.text= sumExpense.toString().trim()
    }

    private fun setSwipetoDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val model=arrayList[position]

                val chk=dbHelper.deleteData(model)
                if(chk.equals(-1)){
                    Toast.makeText(this@MainActivity,"Record not deleted from database",Toast.LENGTH_SHORT).show()
                }else {
                    arrayList.removeAt(position)
                    adapterTransaction.notifyItemRemoved(position)
                    loadSum()
                    Toast.makeText(this@MainActivity, "Expense Deleted", Toast.LENGTH_SHORT).show()
                }
            }

        }).attachToRecyclerView(binding.transactList)
    }

    private fun addExpenseDialog() {
        val expenseAddBinding = ExpenseAdderBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(expenseAddBinding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        expenseAddBinding.backBlack.setOnClickListener {
            alertDialog.dismiss()
        }

        expenseAddBinding.dateEt.setOnClickListener{
            showDatePicker(expenseAddBinding.dateEt)
        }

        expenseAddBinding.submitBtn.setOnClickListener {
            saveData(alertDialog,
                expenseAddBinding.titleEt.text.toString().trim(),
                expenseAddBinding.expenseEt.text.toString().trim(),
                expenseAddBinding.dateEt.text.toString().trim())
        }
    }

    private fun saveData(alertDialog: AlertDialog?, title: String, expense: String, date: String) {
        if (TextUtils.isEmpty(title)){
            Toast.makeText(this,"Enter a title",Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(expense)){
            Toast.makeText(this,"Enter an expense",Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(date)){
            Toast.makeText(this,"Enter a date",Toast.LENGTH_SHORT).show()
        } else{
            val amount=expense.toDouble()
            val saveData=dbHelper.saveData(title,amount,date)
            if (saveData==true){
                Toast.makeText(this,"Expense data saved",Toast.LENGTH_SHORT).show()
                alertDialog!!.dismiss()
                loadData()
                loadSum()
            }else{
                Toast.makeText(this,"Unable to save expense data",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        arrayList= ArrayList()
        val get = dbHelper.loadData()
        get.use { // Ensures the cursor is closed after use
            while (get!!.moveToNext()) {
                val title = get.getString(get.getColumnIndexOrThrow("Title"))
                val amount = get.getDouble(get.getColumnIndexOrThrow("Amount"))
                val date = get.getString(get.getColumnIndexOrThrow("Date"))

                val myData = Transaction(title, amount, date)
                arrayList.add(myData)
            }
        }
        adapterTransaction= TransactionAdapter(this,arrayList)
        binding.transactList.adapter=adapterTransaction
    }



    private fun showDatePicker(dateEt: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Set the selected date on the EditText
                dateEt.setText("$year-${monthOfYear + 1}-$dayOfMonth")
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}