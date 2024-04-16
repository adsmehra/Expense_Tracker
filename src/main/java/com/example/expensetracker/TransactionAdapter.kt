package com.example.expensetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.TransactionRowBinding


class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private lateinit var context: Context

    private lateinit var binding: TransactionRowBinding

    private lateinit var arrayList:ArrayList<Transaction>

    constructor(
        context: Context,
        arrayList: ArrayList<Transaction>
    ) : super() {
        this.context = context
        this.arrayList = arrayList
    }


    inner class TransactionHolder(view: View): RecyclerView.ViewHolder(view){
        var title : TextView=binding.title
        var date : TextView=binding.date
        var amount : TextView=binding.transaction
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        binding= TransactionRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return TransactionHolder(binding.root)
    }

    private var length=0

    override fun getItemCount(): Int {
        length=arrayList.size
        return arrayList.size
    }

    private var sum=0.0

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        var model=arrayList[position]
        var title=model.title
        var expense=model.expense
        var date=model.date

        holder.title.text=title
        holder.date.text= date.toString()
        holder.amount.text= expense.toString()

    }

}