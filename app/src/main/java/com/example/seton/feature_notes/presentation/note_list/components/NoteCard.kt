@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.seton.feature_notes.presentation.note_list.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seton.common.presentation.state.ContainerColor
import com.example.seton.feature_notes.data.NoteSharedElementKey
import com.example.seton.feature_notes.data.NoteTextType

private const val TAG = "note_card"

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    content: String,
    onLongClick: () -> Unit,
    isCardChecked: Boolean,
    color: ContainerColor,
    animatedContentScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = if(isSystemInDarkTheme()) {
                color.darkVariant
            } else color.lightVariant
        ),
        border = if (isCardChecked) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
        } else {
            BorderStroke(0.dp, Color.Transparent)
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            with(sharedTransitionScope) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(12.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(
                                    key = NoteSharedElementKey(id, title, NoteTextType.Title)
                                ),
                                animatedVisibilityScope = animatedContentScope
                            )
                    )
                    Text(
                        text = content,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        maxLines = 5,
                        modifier = Modifier
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(
                                    key = NoteSharedElementKey(id, content, NoteTextType.Content)
                                ),
                                animatedVisibilityScope = animatedContentScope
                            )
                    )
                }
            }
        }
    }
}