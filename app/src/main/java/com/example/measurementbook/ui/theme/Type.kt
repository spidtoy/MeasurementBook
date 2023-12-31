package com.example.measurementbook.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.measurementbook.R


val Anuphan = FontFamily(
    Font(R.font.anuphan_regular),
    Font(R.font.anuphan_semibold, FontWeight.SemiBold),
    Font(R.font.anuphan_bold, FontWeight.Bold)
)
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Anuphan,
        fontWeight = FontWeight.Normal
    ),
    h6 = TextStyle(
        fontFamily = Anuphan,
        fontWeight = FontWeight.SemiBold
    ),
    h5 = TextStyle(
        fontFamily = Anuphan,
        fontWeight = FontWeight.Bold
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)