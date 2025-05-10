package com.bysoftware.fixedcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.Locale
import javax.inject.Inject


class RecyclerViewAdapter() : BaseAdapter() {

     lateinit var context: Context
     lateinit var list: List<String>
      private var monthOfYear : Int = 0
     var dayOfMonth : Int = 0

   @Inject constructor(context: Context, list: List<String>, monthOfYear: Int, dayOfMonth: Int) : this() {
         this.context = context
         this.list = list
         this.monthOfYear = monthOfYear
         this.dayOfMonth = dayOfMonth
     }

     /* RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

     lateinit var context: Context
     lateinit var list: List<String>


     constructor(context: Context, list: List<String>) : this() {
         this.context = context
         this.list = list
     }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.card_item, parent)

        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {

        var months: String = list[position]

        holder.month.text

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var month: TextView

        init {
            month = itemView.findViewById(R.id.month)
        }
    }

*/

     private var layoutInflater: LayoutInflater? = null
     lateinit var month: TextView

     override fun getCount(): Int {
         return list.size
     }

     override fun getItem(p0: Int): Any {
         return 0
     }

     override fun getItemId(p0: Int): Long {
         return 0
     }

     @SuppressLint("ViewHolder", "SuspiciousIndentation")
     override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

         var p1 = p1

         if (layoutInflater == null) {
             layoutInflater =
                 context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         }
         if (p1 == null) {
             p1 = layoutInflater!!.inflate(R.layout.card_item, null)
         }

        // val inflater = LayoutInflater.from(context).inflate(R.layout.card_item, p2)

          month = p1!!.findViewById(R.id.month)

         val views = findViewIdsOfType<TextView>(p1.findViewById(R.id.gridlayout)) // Değiştirmeniz gereken layout_root, ana layoutunuzun id'si

         // Liste içindeki view'lerin ID'lerini yazdır


         if(monthOfYear == p0){
             views.forEach {
                 if (context.resources.getResourceEntryName(it.id) == "d${dayOfMonth}txt"){
                     val text = p1.findViewById<TextView>(it.id)
                     text.setTextColor(context.getColor(R.color.white))
                 } else if (context.resources.getResourceEntryName(it.id) == "d$dayOfMonth"){
                     val view = p1.findViewById<View>(it.id)
                     view.visibility = View.VISIBLE
             }
             }
         }

         views.forEach { viewId ->
             println("TextView ID: ${context.resources.getResourceEntryName(viewId.id)}")
         }
         // Liste içindeki view'lerin ID'lerini yazdır

         month.text = list[p0]

         val ay = list[p0]


         val dateText = context.getString(R.string.date)
         val monthText = context.getString(R.string.info)


         p1.setOnClickListener {
             if (monthOfYear == p0){
                 Toast.makeText(context,"$dateText $ay",Toast.LENGTH_SHORT).show()
             } else{
                 if (Locale.getDefault().language ==  "tr"){
                     Toast.makeText(context,"${monthOfYear+1}. aya tıklayın ", Toast.LENGTH_SHORT).show()
                 } else if (monthOfYear+1 == 1 ){
                     Toast.makeText(context,"Click on the ${monthOfYear+1}st month ", Toast.LENGTH_SHORT).show()
                 } else if ( monthOfYear+1 == 2){
                     Toast.makeText(context,"Click on the ${monthOfYear+1}nd month ", Toast.LENGTH_SHORT).show()
                 } else if (monthOfYear+1 == 3){
                     Toast.makeText(context,"Click on the ${monthOfYear+1}rd month ", Toast.LENGTH_SHORT).show()
                 } else{
                     Toast.makeText(context,"Click on the ${monthOfYear+1}th month ", Toast.LENGTH_SHORT).show()
                 }
             }
         }


         return p1
     }

     inline fun <reified T : TextView> findViewIdsOfType(view: View): List<View> {
        val views = mutableListOf<View>()
        findViewIdsOfTypeRecursive(view, views)
        return views
    }

    inline fun <reified T : TextView> findViewIdsOfType2(view: View): List<View> {
        val views = mutableListOf<View>()
        findViewIdsOfTypeRecursive(view, views)
        return views
    }

    // Tüm view'leri dolaşarak belirli türdeki view'lerin ID'lerini bir listeye ekleyen rekurisif fonksiyon
     fun findViewIdsOfTypeRecursive(view: View, views: MutableList<View>) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                findViewIdsOfTypeRecursive(child, views)
            }
        }

        if (view !is FrameLayout) {
            views.add(view)
         //   views.add(context.resources.getResourceEntryName(view.id))
        }
    }

}