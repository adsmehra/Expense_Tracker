package com.example.expensetracker

import java.util.Date

class Transaction{
    var title:String=""
    var expense:Double=0.0
    var date:String=""

    constructor()

    constructor(title: String, expense: Double, date: String) {
        this.title = title
        this.expense = expense
        this.date = date
    }
}

