package seifemadhamdy.vekoz.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun IllustrationBox(
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int,
    contentColor: Color = LocalContentColor.current,
    title: String,
    body: String,
    content: @Composable (() -> Unit)? = null,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = imageResId,
                modifier = Modifier.fillMaxWidth(fraction = 0.381966012f).alpha(alpha = 0.6f),
                contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
            )

            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth().alpha(alpha = 0.87f),
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = body,
                    modifier = Modifier.fillMaxWidth().alpha(alpha = 0.6f),
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            content?.invoke()
        }
    }
}
