package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemDark: Boolean = isSystemInDarkTheme()
            var isDarkTheme by rememberSaveable { mutableStateOf(isSystemDark) }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StrContent(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = !isDarkTheme },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun StrContent(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    var count by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.screen_padding))
    ) {
        Text(
            text = stringResource(R.string.title),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.subtitle),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_size))
                )
                .padding(dimensionResource(R.dimen.padding))
        ) {
            Text(
                text = pluralStringResource(R.plurals.description, count, count),
                modifier = Modifier
                    .clickable { count++ }
                    .background(MaterialTheme.colorScheme.primary),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Row {
                Checkbox(
                    checked = count > 0,
                    onCheckedChange = { count = 0 },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.secondary,
                        checkedColor = MaterialTheme.colorScheme.secondary,
                    )
                )

                Surface(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_size)),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Text(
                        stringResource(R.string.card),
                        Modifier.padding(dimensionResource(R.dimen.padding)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer)))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = stringResource(R.string.error),
                    tint = MaterialTheme.colorScheme.error
                )

                Text(
                    text = stringResource(R.string.error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.theme_night),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer)))

            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.background,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            )
        }

        var isEnabled by remember { mutableStateOf(true) }
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val iconRes = when {
            !isEnabled -> R.drawable.apple
            isPressed -> R.drawable.facebook
            else -> R.drawable.google
        }

        Row {
            Column {
                IconButton(
                    modifier = Modifier.width(100.dp).height(100.dp),
                    onClick = { isEnabled = !isEnabled },
                    enabled = true,
                    interactionSource = interactionSource
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = "selector",
                        tint = Color.Unspecified,
                    )
                }

                Box {
                    AndroidView(
                        factory = { context ->
                            FrameLayout(context).apply {
                                setBackgroundResource(R.drawable.shape_card)
                            }
                        },
                        modifier = Modifier.matchParentSize()
                    )

                    Text(
                        text = stringResource(R.string.card),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.width(20.dp))

            CascadeLayout(step = 10.dp) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Blue)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Green)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Yellow)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Cyan)
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Magenta)
                )
            }
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.spacer)))


        AdaptiveGridLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding)),
        ) {
            repeat(6) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${stringResource(R.string.card)} ${index + 1}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CascadeLayout(
    modifier: Modifier = Modifier,
    step: Dp = 24.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val stepPx = step.roundToPx()

        val maxChildWidth = placeables.maxOfOrNull { it.width } ?: 0
        val maxChildHeight = placeables.maxOfOrNull { it.height } ?: 0

        val totalWidth = if (placeables.isNotEmpty()) maxChildWidth + (placeables.size - 1) * stepPx else 0
        val totalHeight = if (placeables.isNotEmpty()) maxChildHeight + (placeables.size - 1) * stepPx else 0

        val layoutWidth = constraints.constrainWidth(totalWidth)
        val layoutHeight = constraints.constrainHeight(totalHeight)

        layout(layoutWidth, layoutHeight) {
            var currentX = 0
            var currentY = 0

            placeables.forEach { placeable ->
                val x = when (horizontalAlignment) {
                    Alignment.CenterHorizontally -> currentX + (layoutWidth - totalWidth) / 2
                    Alignment.End -> currentX + (layoutWidth - totalWidth)
                    else -> currentX
                }

                val y = when (verticalAlignment) {
                    Alignment.CenterVertically -> currentY + (layoutHeight - totalHeight) / 2
                    Alignment.Bottom -> currentY + (layoutHeight - totalHeight)
                    else -> currentY
                }

                placeable.placeRelative(x = x, y = y)

                currentX += stepPx
                currentY += stepPx
            }
        }
    }
}

@Composable
fun AdaptiveGridLayout(
    modifier: Modifier = Modifier,
    minItemWidth: Dp = 120.dp,
    spacing: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(0, 0) {}
        }

        val minItemWidthPx = minItemWidth.roundToPx()
        val spacingPx = spacing.roundToPx()
        val maxWidth = constraints.maxWidth

        val columns = max(1, (maxWidth + spacingPx) / (minItemWidthPx + spacingPx))

        val itemWidth = (maxWidth - (columns - 1) * spacingPx) / columns

        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth,
            minHeight = 0,
            maxHeight = constraints.maxHeight
        )

        val placeables = measurables.map { it.measure(itemConstraints) }

        val rows = (placeables.size + columns - 1) / columns
        val rowHeights = IntArray(rows) { 0 }

        placeables.forEachIndexed { index, placeable ->
            val row = index / columns
            rowHeights[row] = max(rowHeights[row], placeable.height)
        }

        val totalHeight = rowHeights.sum() + (rows - 1) * spacingPx
        val layoutHeight = constraints.constrainHeight(totalHeight)

        layout(maxWidth, layoutHeight) {
            var currentX = 0
            var currentY = 0

            placeables.forEachIndexed { index, placeable ->
                val row = index / columns
                val col = index % columns

                currentX = col * (itemWidth + spacingPx)

                currentY = 0
                for (r in 0 until row) {
                    currentY += rowHeights[r] + spacingPx
                }

                placeable.placeRelative(x = currentX, y = currentY)
            }
        }
    }

}

@Preview(showSystemUi = true, locale = "ru")
@Composable
fun TextContentPreview() {
    val isSystemDark: Boolean = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable { mutableStateOf(isSystemDark) }

    MyApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            StrContent(
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDarkTheme = !isDarkTheme },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

