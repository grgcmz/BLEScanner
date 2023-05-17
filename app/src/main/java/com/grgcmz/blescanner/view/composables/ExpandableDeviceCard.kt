package com.grgcmz.blescanner.view.composables

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grgcmz.blescanner.R
import com.grgcmz.blescanner.model.DeviceModel
import com.grgcmz.blescanner.model.ScanResultAdapter

const val COLLAPSE_ANIMATION_DURATION = 50
const val FADE_OUT_ANIMATION_DURATION = 50
const val FADE_IN_ANIMATION_DURATION = 50
const val EXPAND_ANIMATION_DURATION = 100

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpandableDeviceCard(
    deviceModel: DeviceModel,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    ){
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }

    val transition = updateTransition(transitionState, label = "transition")

    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
        if (expanded) 48.dp else 24.dp
    }

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            CardArrow(
                degrees = arrowRotationDegree,
                onClick = onCardArrowClick
            )
            // Left Icon column
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(0.15f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                    contentDescription = "Bluetooth Icon"
                )
            }

            Column() {

                Column() {
                    Text(
                        text = deviceModel.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Center Column
                FlowColumn(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachColumn = 3,

                    ) {
                    Text(
                        text = deviceModel.address,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "$deviceModel.rssi dBm",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = ScanResultAdapter.getBondState(deviceModel.bondState),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            // Right connection Button
            // TODO()
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = "Card Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(100.dp))
            Text(
                text = "Expandable content here",
                textAlign = TextAlign.Center
            )
        }
    }
}