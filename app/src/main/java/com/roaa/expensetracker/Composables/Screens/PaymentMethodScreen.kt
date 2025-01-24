package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PaymentMethodScreen(modifier: Modifier = Modifier, sendUserBack: () -> Unit) {
    Scaffold(topBar = {
        TopBar(
            title = "Payment Methods",
            showDelete = false,
            sendUserBackToPreviousActivity = { sendUserBack() },
            delete = {}
        )
    }) {
        Column(Modifier.padding(paddingValues = it)) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Primary Account", modifier = Modifier.padding(18.dp, 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
            )
            Card(
                shape = RoundedCornerShape(12.dp), modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        //  onSingleItemClick(item)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = combineColors(
                        MaterialTheme.colorScheme.surface,
                        blueColor,
                        angle = 0.1f,
                    )
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp, 18.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .size(56.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            val image = rememberAsyncImagePainter(IconState.fromNumber(24))
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }


                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth(0.80f)

                    ) {
                        Text(
                            text = "**** **** **** 1232",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = typography.titleMedium.copy(
                                fontFamily = numberFont,
                                fontWeight = FontWeight.SemiBold
                            ),
                        )
                        if (true) {
                            Text(
                                text = "HDFC Bank",
                                style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = { }, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Filled.MoreVert, contentDescription = null,


                            )
                    }
                }

            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Secondary Account", modifier = Modifier.padding(18.dp, 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
            )
            Card(
                shape = RoundedCornerShape(12.dp), modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        //  onSingleItemClick(item)
                    }, colors = CardDefaults.cardColors(
                    containerColor = combineColors(
                        MaterialTheme.colorScheme.surface,
                        greenColor,
                        angle = 0.1f,
                    )
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp, 18.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .size(56.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            val image = rememberAsyncImagePainter(IconState.fromNumber(25))
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }


                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth(0.80f)

                    ) {
                        Text(
                            text = "Cash",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = typography.titleMedium.copy(
                                fontFamily = numberFont,
                                fontWeight = FontWeight.SemiBold
                            ),
                        )
                        if (true) {
                            Text(
                                text = "HDFC Bank",
                                style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = { }, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Filled.MoreVert, contentDescription = null,


                            )
                    }
                }

            }

            Card(
                shape = RoundedCornerShape(12.dp), modifier = Modifier
                    .padding(16.dp, 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        //  onSingleItemClick(item)
                    }, colors = CardDefaults.cardColors(
                    containerColor = combineColors(
                        MaterialTheme.colorScheme.surface,
                        orange,
                        angle = 0.1f,
                    )
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp, 18.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .size(56.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            val image = rememberAsyncImagePainter(IconState.fromNumber(24))
                            Image(
                                painter = image,
                                contentDescription = "Test Image",
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }


                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth(0.80f)

                    ) {
                        Text(
                            text = "**** **** **** 1232",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = typography.titleMedium.copy(
                                fontFamily = numberFont,
                                fontWeight = FontWeight.SemiBold
                            ),
                        )
                        if (true) {
                            Text(
                                text = "Bank of Maharashtra",
                                style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = { }, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Filled.MoreVert, contentDescription = null,


                            )
                    }
                }

            }
        }

    }
}


