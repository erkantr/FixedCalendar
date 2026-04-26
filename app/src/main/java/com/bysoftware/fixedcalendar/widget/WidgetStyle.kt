package com.bysoftware.fixedcalendar.widget

enum class WidgetStyle(val key: Int) {
    TEXT_HERO_COMPACT(0),
    TEXT_STACKED_MINIMAL(1),
    TEXT_PILL_CHIP(2),
    GRID_CLASSIC(3);

    companion object {
        fun fromKey(value: Int): WidgetStyle = entries.firstOrNull { it.key == value } ?: GRID_CLASSIC
    }
}
