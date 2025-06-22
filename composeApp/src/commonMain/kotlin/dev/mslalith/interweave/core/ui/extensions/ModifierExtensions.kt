package dev.mslalith.interweave.core.ui.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.modifyIf(
    predicate: () -> Boolean,
    block: Modifier.() -> Modifier
): Modifier = if (predicate()) this.then(other = block()) else this

fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    this then Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick
    )
}
