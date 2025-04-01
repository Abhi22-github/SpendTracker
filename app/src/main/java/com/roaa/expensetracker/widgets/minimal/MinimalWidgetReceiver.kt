package com.roaa.expensetracker.widget.minimal

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MinimalWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = MinimalWidget()
}
