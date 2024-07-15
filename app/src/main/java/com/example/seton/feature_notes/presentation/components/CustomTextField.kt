package com.example.seton.feature_notes.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = false,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    text: String,
    placeholderText: String,
    imeAction: ImeAction = ImeAction.None,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = text,
        singleLine = isSingleLine,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = fontSize
            )
        },
        textStyle = TextStyle(
            fontWeight = fontWeight,
            fontSize = fontSize
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        modifier = modifier
    )
}