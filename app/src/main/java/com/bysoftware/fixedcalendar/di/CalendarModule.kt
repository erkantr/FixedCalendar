package com.bysoftware.fixedcalendar.di

import com.bysoftware.fixedcalendar.CalendarAdapter
import com.bysoftware.fixedcalendar.utils.IFCDateUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {

    @Provides
    @Singleton
    fun provideCalendarAdapter(): CalendarAdapter {
        val currentDate = LocalDate.now()
        val ifcDate = IFCDateUtils.convertToIFC(currentDate)
        
        return CalendarAdapter(
            currentMonth = ifcDate.month,
            currentDay = ifcDate.day,
            isLeapDay = ifcDate.isLeapDay,
            isYearDay = ifcDate.isYearDay
        )
    }
} 