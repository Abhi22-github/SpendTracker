package com.example.expensetracker.Composables.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.R


val BUTTON_GAP = 6.dp

@Composable
fun KeyBoard(modifier: Modifier) {
    Surface(modifier = modifier) {
        Row(Modifier.height(450.dp).padding(0.dp)) {
            Column(Modifier.weight(0.75f)) {
                Row(Modifier.weight(.25f)) {
                    for (i in 7..9)
                        KeyboardButton(
                            modifier = Modifier
                                .weight(1F)
                                .padding(BUTTON_GAP),
                            type = KeyboardButtonType.DEFAULT,
                            text = i.toString(),
                            onClick = {
                            },
                        )
                }
                Row(Modifier.weight(.25f)) {
                    for (i in 4..6)
                        KeyboardButton(
                            modifier = Modifier
                                .weight(1F)
                                .padding(BUTTON_GAP),
                            type = KeyboardButtonType.DEFAULT,
                            text = i.toString(),
                            onClick = {
                            },
                        )
                }
                Row(Modifier.weight(.25f)) {
                    for (i in 1..3)
                        KeyboardButton(
                            modifier = Modifier
                                .weight(1F)
                                .padding(BUTTON_GAP),
                            type = KeyboardButtonType.DEFAULT,
                            text = i.toString(),
                            onClick = {
                            },
                        )
                }
                Row(Modifier.weight(.25f)) {

                    KeyboardButton(
                        modifier = Modifier
                            .weight(2F)
                            .padding(BUTTON_GAP),
                        type = KeyboardButtonType.DEFAULT,
                        text = "0",
                        onClick = {
                        },
                    )
                    KeyboardButton(
                        modifier = Modifier
                            .weight(1F)
                            .padding(BUTTON_GAP),
                        type = KeyboardButtonType.DEFAULT,
                        text = ".",
                        onClick = {
                        },
                    )

                }
            }
            Column(Modifier.weight(0.25f)) {
                Column {
                    KeyboardButton(
                        modifier = Modifier
                            .weight(0.25F)
                            .padding(BUTTON_GAP),
                        type = KeyboardButtonType.SECONDARY,
                        icon = painterResource(R.drawable.ic_backspace),
                        onClick = {
                        },
                    )
                    KeyboardButton(
                        modifier = Modifier
                            .weight(0.75F)
                            .padding(BUTTON_GAP),
                        type = KeyboardButtonType.PRIMARY,
                        icon = painterResource(R.drawable.home_checked),
                        onClick = {
                        },
                    )
                }
            }
        }

    }
}

@Composable
@Preview
fun KeyBoardPreview() {
    ExpenseTrackerTheme {
        KeyBoard(Modifier)
    }
}

