package com.example.expensetracker

import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.PanZoom
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYSeries
import com.example.expensetracker.databinding.ActivityGraphBinding
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.time.LocalDate

class GraphActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraphBinding

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper= DbHelper(this)

        binding.backBlack.setOnClickListener{
            onBackPressed()
        }


        val today: LocalDate = LocalDate.now()
        val dates = arrayOf<LocalDate>(LocalDate.now().minusDays(6),
            LocalDate.now().minusDays(5),
            LocalDate.now().minusDays(4),
            LocalDate.now().minusDays(3),
            LocalDate.now().minusDays(2),
            LocalDate.now().minusDays(1),
            LocalDate.now())
        val expenses= arrayOf<Int>(
            dbHelper.dateExpense(today.minusDays(6)).toInt(),
            dbHelper.dateExpense(today.minusDays(5)).toInt(),
            dbHelper.dateExpense(today.minusDays(4)).toInt(),
            dbHelper.dateExpense(today.minusDays(3)).toInt(),
            dbHelper.dateExpense(today.minusDays(2)).toInt(),
            dbHelper.dateExpense(today.minusDays(1)).toInt(),
            dbHelper.dateExpense(today).toInt(),
        )



        val graph: XYSeries=SimpleXYSeries(listOf(*expenses),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Days")

        val graphFormat=LineAndPointFormatter(Color.BLUE,Color.BLACK,null,null)
        graphFormat.setInterpolationParams(CatmullRomInterpolator.Params(10,CatmullRomInterpolator.Type.Centripetal))

        binding.plot.addSeries(graph,graphFormat)

        binding.plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format=object : Format(){
            override fun format(
                obj: Any?,
                toAppendTo: StringBuffer?,
                pos: FieldPosition?
            ): StringBuffer {
                val i=Math.round((obj as Number).toFloat())
                return toAppendTo!!.append(dates[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }

        }
        PanZoom.attach(binding.plot)


    }
}