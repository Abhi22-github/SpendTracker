package com.roaa.expensetracker.Composables.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.roaa.expensetracker.Composables.CustomFonts.numberFont
import com.roaa.expensetracker.Composables.blueColor
import com.roaa.expensetracker.Composables.components.AddPaymentMethodBottomSheet
import com.roaa.expensetracker.Composables.components.TopBar
import com.roaa.expensetracker.Composables.greenColor
import com.roaa.expensetracker.Composables.orange
import com.roaa.expensetracker.Composables.utils.IconState
import com.roaa.expensetracker.Composables.utils.combineColors
import com.roaa.expensetracker.R
import com.roaa.expensetracker.Utilities.extractNumbers
import com.roaa.expensetracker.ViewModels.UiViewModel
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PaymentMethodScreen(
    modifier: Modifier = Modifier,
    sendUserBack: () -> Unit,
    uiViewModel: UiViewModel = hiltViewModel()
) {
    val showBottomSheet by uiViewModel.paymentMethodBottomSheetStatus.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopBar(title = "Payment Methods",
            showDelete = false,
            sendUserBackToPreviousActivity = { sendUserBack() },
            delete = {})
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                scope.launch {
                    uiViewModel.paymentMethodBottomSheetStatus.emit(true)
                }
            },
            icon = { Icon(Icons.Filled.Add, "Localized description") },
            text = { Text(text = "Add Payment Method") },
        )
    }) {
        Column(Modifier.padding(paddingValues = it)) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Primary Account",
                modifier = Modifier.padding(18.dp, 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
            )

            PaymentCard(blueColor)

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Secondary Account",
                modifier = Modifier.padding(18.dp, 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
            )

            PaymentCard(greenColor)

            // PaymentMethodCardOld()

        }

    }
    if (showBottomSheet) AddPaymentMethodBottomSheet()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                //  onSingleItemClick(item)
            },
        colors = CardDefaults.cardColors(
            containerColor = combineColors(
                MaterialTheme.colorScheme.surface,
                color,
                angle = 0.1f,
            )
        )
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2, progress) = createRefs()

            Text(text = "₹24,045",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = numberFont),
                modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Text(text = "Amount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                modifier = Modifier.constrainAs(balanceLabel) {
                    top.linkTo(balanceText.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(cardNumber) {
                    top.linkTo(progress.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                },
                horizontalArrangement = Arrangement.Center
            ) {


                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .size(36.dp)
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
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.80f)

                ) {
                    if (true) {
                        Text(
                            text = "Bank Of Maharshtra",
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                    }
                }


            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage1) {
                        top.linkTo(parent.top, margin = -30.dp)
                        start.linkTo(parent.start, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            LinearProgressIndicator(progress = { Math.random().toFloat() },
                modifier = Modifier
                    .height(10.dp)
                    .constrainAs(progress) {
                        top.linkTo(balanceLabel.bottom, 20.dp)
                        start.linkTo(parent.start, 24.dp)
                        end.linkTo(parent.end, 24.dp)
                        width = Dimension.fillToConstraints
                    },
                color = color.copy(alpha = 0.30f),
                trackColor = color.copy(alpha = 0.10f),
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {
                    drawStopIndicator(
                        drawScope = this,
                        stopSize = ProgressIndicatorDefaults.CircularStrokeWidth,
                        color = color,
                        strokeCap = StrokeCap.Round
                    )
                })
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage2) {
                        end.linkTo(parent.end, margin = -30.dp)
                        bottom.linkTo(parent.bottom, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )

            IconButton(onClick = { }, modifier = Modifier.constrainAs(moreIcon) {
                top.linkTo(parent.top, 18.dp)
                end.linkTo(parent.end, 18.dp)
            }) {
                Icon(
                    Icons.Filled.MoreVert, contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun PaymentMethodCardOld() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                //  onSingleItemClick(item)
            },
        colors = CardDefaults.cardColors(
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
                        fontFamily = numberFont, fontWeight = FontWeight.SemiBold
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePaymentCard(
    color: Color,
    sendBankAmount: (String) -> Unit,
    sendBankName: (String) -> Unit,
    uiViewModel: UiViewModel = hiltViewModel(),
) {
    val liveBankName by uiViewModel.liveBankName.collectAsState()
    val liveBankAmount by uiViewModel.liveBankAmount.collectAsState()

    var bankAmount by remember { mutableStateOf(TextFieldValue("")) }
    var bankName by remember { mutableStateOf(TextFieldValue("")) }

    val hintStyleAmount = MaterialTheme.typography.titleLarge.copy(fontFamily = numberFont)
    val hintStyle = MaterialTheme.typography.bodyMedium
    val hintColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)

    val newColor = combineColors(
        MaterialTheme.colorScheme.surface,
        color,
        angle = 0.1f,
    )
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp, 4.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = newColor
        )
    ) {

        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (balanceText, balanceLabel, cardNumber, moreIcon, backgroundImage1, backgroundImage2) = createRefs()

            Row(modifier = Modifier.constrainAs(balanceText) {
                top.linkTo(parent.top, margin = 24.dp)
                start.linkTo(parent.start, margin = 24.dp)
            }, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = numberFont,
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box() {
                    BasicTextField(value = bankAmount,
                        onValueChange = {
                            bankAmount = TextFieldValue(
                                extractNumbers(it.text).toString(),
                                selection = TextRange(extractNumbers(it.text).toString().length)
                            )
                            sendBankAmount(bankAmount.text)
                            scope.launch { uiViewModel.errorStatusInBankAccountAdd.emit(false) }
                        },
                        cursorBrush = SolidColor(color),
                        modifier = Modifier
                            .background(
                                color.copy(alpha = 0.2f), RoundedCornerShape(5.dp)
                            )
                            .padding(10.dp, 3.dp),
                        textStyle = MaterialTheme.typography.titleLarge.copy(fontFamily = numberFont),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        decorationBox = { innerTextField ->
                            if (bankAmount.text.isEmpty()) {
                                Text(
                                    text = "Amount",
                                    style = hintStyleAmount,
                                    color = hintColor,
                                )
                            }
                            innerTextField()
                        })
                }
            }
            Text(text = "Amount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                modifier = Modifier.constrainAs(balanceLabel) {
                    top.linkTo(balanceText.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(cardNumber) {
                    top.linkTo(balanceLabel.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                },
                horizontalArrangement = Arrangement.Center
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
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }


                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(0.80f)

                ) {

                    Spacer(Modifier.height(2.dp))
                    BasicTextField(
                        value = bankName,
                        onValueChange = {
                            bankName = it
                            sendBankName(bankName.text)
                            scope.launch { uiViewModel.errorStatusInBankAccountAdd.emit(false) }
                        },
                        cursorBrush = SolidColor(color),
                        modifier = Modifier
                            .background(
                                color.copy(alpha = 0.2f), RoundedCornerShape(5.dp)
                            )
                            .padding(10.dp, 3.dp),
                        textStyle = MaterialTheme.typography.titleLarge.copy(fontFamily = numberFont),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                    ) { innerTextField ->
                        if (bankName.text.isEmpty()) {
                            Text(
                                text = "Bank Name",
                                style = hintStyleAmount.copy(),
                                color = hintColor,
                            )
                        }
                        innerTextField()

                    }
//                    Text(
//                        text = liveBankName,
//                        style = typography.labelLarge.copy(fontWeight = FontWeight.Medium),
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
//                    )

                }


            }

            val image = rememberAsyncImagePainter(R.drawable.shape_soft_star_1)
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage1) {
                        top.linkTo(parent.top, margin = -30.dp)
                        start.linkTo(parent.start, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )
            Image(
                painter = image,
                contentDescription = "Test Image",
                modifier = Modifier
                    .size(144.dp)
                    .constrainAs(backgroundImage2) {
                        end.linkTo(parent.end, margin = -30.dp)
                        bottom.linkTo(parent.bottom, margin = -30.dp)
                    },
                alpha = 0.1f,
                colorFilter = ColorFilter.tint(color)
            )

            IconButton(onClick = { }, modifier = Modifier.constrainAs(moreIcon) {
                top.linkTo(parent.top, 18.dp)
                end.linkTo(parent.end, 18.dp)
            }) {
                Icon(
                    Icons.Filled.MoreVert, contentDescription = null,
                )
            }
        }
    }
}


