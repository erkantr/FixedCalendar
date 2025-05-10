package com.bysoftware.fixedcalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bysoftware.fixedcalendar.databinding.ItemCalendarMonthBinding
import com.bysoftware.fixedcalendar.utils.IFCDateUtils
import com.google.android.material.chip.Chip
import javax.inject.Inject

class CalendarAdapter @Inject constructor(
    private val currentMonth: Int,
    private val currentDay: Int,
    private val isLeapDay: Boolean = false,
    private val isYearDay: Boolean = false
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val months = (1..13).toList()

    inner class CalendarViewHolder(private val binding: ItemCalendarMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(month: Int) {
            binding.monthTitle.text = IFCDateUtils.getMonthName(month)
            
            // Özel günler için kontrol
            if ((month == 6 && isLeapDay) || (month == 13 && isYearDay)) {
                val specialDay = Chip(binding.root.context).apply {
                    text = IFCDateUtils.getSpecialDayName(isLeapDay, isYearDay)
                    isCheckable = true
                    isChecked = true
                }
                binding.daysChipGroup.addView(specialDay)
            } else {
                // Normal günler için chip'ler oluştur
                for (day in 1..28) {
                    val chip = Chip(binding.root.context).apply {
                        text = day.toString()
                        isCheckable = true
                        isChecked = month == currentMonth && day == currentDay
                    }
                    binding.daysChipGroup.addView(chip)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarMonthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(months[position])
    }

    override fun getItemCount() = months.size
} 