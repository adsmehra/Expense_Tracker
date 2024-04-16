package com.example.expensetracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate

class DbHelper(context: Context): SQLiteOpenHelper(context,"ExpenseData",null,1) {

    private lateinit var context:Context
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table ExpenseList (Title TEXT ,Amount NUMBER,Date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists ExpenseList")
    }

    fun saveData(title: String, expense: Double, date: String): Boolean {
        val db=this.writableDatabase
        val cv=ContentValues()
        cv.put("Title",title)
        cv.put("Amount",expense)
        cv.put("Date",date)
        val result=db.insert("ExpenseList",null,cv)
        if (result==-1 .toLong()){
            return false
        }else{
            return true
        }
    }

    fun loadData(): Cursor? {
        val db=this.writableDatabase
        val cursor=db.rawQuery("Select * from ExpenseList",null)
        return cursor
    }

    fun deleteData(model: Transaction): Int {
        val db=this.writableDatabase
        val result=db.delete("ExpenseList","Title=?", arrayOf(model.title))
        return result
    }

    fun sumCal(): Double {
        val db=this.writableDatabase
        val cursor=db.rawQuery("Select Amount from ExpenseList",null)
        var sum=0.0
        while (cursor.moveToNext()){
            val exp=cursor.getDouble(cursor.getColumnIndexOrThrow("Amount"))
            sum=sum+exp
        }
        return sum
    }

    fun dateExpense(date: LocalDate): Double {
        val db=this.writableDatabase
        val day=date.toString()
        var result=""
        if (day[5]=='0'){
            result=day.substring(0,5)+day.substring(6)
        }
        val cursor=db.rawQuery("Select Amount from ExpenseList where Date=?", arrayOf(result))
        var sum=0.0
        while (cursor!!.moveToNext()){
            val exp=cursor.getDouble(cursor.getColumnIndexOrThrow("Amount"))
            sum=sum+exp
        }
        return sum
    }



}