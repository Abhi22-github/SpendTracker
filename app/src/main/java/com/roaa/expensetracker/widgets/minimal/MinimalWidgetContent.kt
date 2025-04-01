package com.roaa.expensetracker.widget.minimal

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.roaa.expensetracker.R
import com.roaa.expensetracker.activity.ComposeMainActivity

@Composable
@GlanceComposable
fun MinimalWidgetContent() {
    val size = LocalSize.current
    val context = LocalContext.current

    val prefs = currentState<Preferences>()

    val intent = Intent(context, ComposeMainActivity::class.java).apply {
        putExtra("SHOW_ADD_TRANSACTION", true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    Box(
        modifier = GlanceModifier
            .cornerRadius(0.dp)
            .fillMaxSize()
            .padding(10.dp).background(ImageProvider(R.drawable.add_widget_preview_background)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            GlanceModifier
                .cornerRadius(50.dp).background(GlanceTheme.colors.primaryContainer).padding(10.dp)
        ) {
            val drawable = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.icon_round_add,
                null,
            )!!

            Image(
                modifier = GlanceModifier.size(
                    when (size) {
                        MinimalWidget.largeMode -> 32.dp
                        MinimalWidget.smallMode -> 24.dp
                        else -> 28.dp
                    }
                ),
                provider = ImageProvider(drawable.toBitmap()),
                colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                contentDescription = null,
            )
//            Text(
//                "Add Transaction", style = TextStyle(
//                    color = GlanceTheme.colors.onSurface,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = when (size) {
//                        MinimalWidget.largeMode -> 22.sp
//                        MinimalWidget.smallMode -> 14.sp
//                        else -> 18.sp
//                    },
//                )
//            )
        }
//        Button(text = "Hello", onClick = {}, colors = ButtonDefaults.buttonColors(contentColor = GlanceTheme.colors.surface))
//        Button(
//            text = "",
//            modifier = GlanceModifier
//                .padding(16.dp),
//            onClick = {},
//            colors = IconButtonDefaults.iconButtonColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            )
//        )

//        Column(
//            modifier = GlanceModifier.padding(8.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            if (
//                stateBudget !== WidgetReceiver.StateBudget.NOT_SET &&
//                stateBudget !== WidgetReceiver.StateBudget.END_PERIOD
//            ) {
//                Row(
//                    modifier = GlanceModifier.padding(12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    CanvasText(
//                        modifier = GlanceModifier.padding(
//                            0.dp,
//                            0.dp,
//                            when (size) {
//                                MinimalWidget.largeMode -> 8.dp
//                                MinimalWidget.smallMode -> 4.dp
//                                else -> 6.dp
//                            },
//                            0.dp,
//                        ),
//                        text = context.resources.getString(
//                            when (size) {
//                                MinimalWidget.smallMode -> R.string.add_spent_short
//                                else -> R.string.add_spent
//                            }
//
//                        ),
//                        style = TextStyle(
//                            color = GlanceTheme.colors.onSurface,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = when (size) {
//                                MinimalWidget.largeMode -> 22.sp
//                                MinimalWidget.smallMode -> 14.sp
//                                else -> 18.sp
//                            },
//                        )
//                    )
//
//                    val drawable = ResourcesCompat.getDrawable(
//                        context.resources,
//                        R.drawable.ic_add,
//                        null,
//                    )!!
//
//                    Image(
//                        modifier = GlanceModifier.size(
//                            when (size) {
//                                MinimalWidget.largeMode -> 32.dp
//                                MinimalWidget.smallMode -> 24.dp
//                                else -> 28.dp
//                            }
//                        ),
//                        provider = ImageProvider(drawable.toBitmap()),
//                        colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
//                        contentDescription = null,
//                    )
//                }
//            }
//
//        }
    }

//    if (BuildConfig.DEBUG) {
//        Box(
//            modifier = GlanceModifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center,
//        ) {
////            CanvasText(
////                modifier = GlanceModifier.padding(top = 8.dp),
////                text = "${size.width}x${size.height}", style = TextStyle(
////                    color = GlanceTheme.colors.onSurfaceVariant,
////                    fontWeight = FontWeight.Bold,
////                    fontSize = 10.sp,
////                )
////            )
//        }
//    }

    Box(
        modifier = GlanceModifier
            .cornerRadius(48.dp)
            .fillMaxSize()
            .clickable(actionStartActivity(intent))
    ) {}
}