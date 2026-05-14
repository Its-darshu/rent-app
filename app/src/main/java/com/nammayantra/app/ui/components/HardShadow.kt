package com.nammayantra.app.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nammayantra.app.ui.theme.InkBlack

fun Modifier.hardShadow(
    color: Color = InkBlack,
    offsetX: Dp = 4.dp,
    offsetY: Dp = 4.dp
): Modifier = this.drawBehind {
    drawRect(
        color = color,
        topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
        size = size
    )
}
