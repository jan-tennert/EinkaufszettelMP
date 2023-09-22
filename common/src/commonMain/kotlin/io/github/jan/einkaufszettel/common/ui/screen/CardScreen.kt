package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import einkaufszettel.db.GetAllCards
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.ui.components.ActionButton
import io.github.jan.einkaufszettel.common.ui.components.CardCard
import io.github.jan.einkaufszettel.common.ui.components.CardImage
import io.github.jan.einkaufszettel.common.ui.dialog.DeleteDialog
import io.github.jan.einkaufszettel.common.ui.icons.Add
import io.github.jan.einkaufszettel.common.ui.icons.ArrowBack
import io.github.jan.einkaufszettel.common.ui.icons.Delete
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon

sealed interface CardScreenState {
    object None : CardScreenState
    object Create : CardScreenState
    @JvmInline
    value class Show(val card: GetAllCards) : CardScreenState
    @JvmInline
    value class Edit(val card: GetAllCards) : CardScreenState
}

@Composable
fun CardScreen(viewModel: EinkaufszettelViewModel) {
    var cardState by remember { mutableStateOf<CardScreenState>(CardScreenState.None) }
    val cards by viewModel.cardFlow.collectAsState(listOf())

    AnimatedVisibility(
        visible = cardState is CardScreenState.None
    ) {
        LazyVerticalGrid(
            GridCells.Adaptive(150.dp),
        ) {
            items(cards, { it.id }) {
                Box(Modifier.padding(8.dp)) {
                    CardCard(
                        card = it,
                        viewModel = viewModel,
                        onClick = {
                            cardState = CardScreenState.Show(it)
                        },
                        onLongClick = {
                            cardState = CardScreenState.Edit(it)
                        }
                    )
                }
            }
        }

        ActionButton(Alignment.BottomEnd, { cardState = CardScreenState.Create }) {
            Icon(LocalIcon.Add, "Erstellen")
        }
    }

    AnimatedVisibility(
        visible = cardState is CardScreenState.Show
    ) {
        BackHandle {
            cardState = CardScreenState.None
        }
        val card = (cardState as? CardScreenState.Show)?.card ?: return@AnimatedVisibility
        val scale = remember { mutableStateOf(1f) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale.value *= zoom
                }
            }
        ) {
            CardImage(
                card = card,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        // adding some zoom limits (min 50%, max 200%)
                        scaleX = maxOf(.5f, minOf(3f, scale.value)),
                        scaleY = maxOf(.5f, minOf(3f, scale.value)),
                    ),
                original = true
            )
        }

        ActionButton(Alignment.BottomStart, { cardState = CardScreenState.None }) {
            Icon(LocalIcon.ArrowBack, "Zurück")
        }
    }

    AnimatedVisibility(
        visible = cardState is CardScreenState.Create
    ) {
        BackHandle {
            cardState = CardScreenState.None
        }

        CreateScreen(
            viewModel = viewModel,
            create = { name, fileInfo, authorizedUsers ->
                viewModel.createCard(name, fileInfo!!, authorizedUsers)
                cardState = CardScreenState.None
            },
            back = {
                cardState = CardScreenState.None
            }
        )
    }

    AnimatedVisibility(
        visible = cardState is CardScreenState.Edit
    ) {
        var showDeleteDialog by remember { mutableStateOf(false) }
        BackHandle {
            cardState = CardScreenState.None
        }

        val card = (cardState as? CardScreenState.Edit)?.card ?: return@AnimatedVisibility
        CreateScreen(
            viewModel = viewModel,
            oldName = card.description,
            oldAuthorizedUserIds = card.authorizedUsers,
            oldImage = { CardImage(card, viewModel, Modifier.size(100.dp)) },
            modifyImageEnabled = false,
            create = { name, _, authorizedUsers ->
                viewModel.editCard(card.id, name, authorizedUsers)
                cardState = CardScreenState.None
            },
            back = {
                cardState = CardScreenState.None
            }
        )

        ActionButton(Alignment.BottomCenter, { showDeleteDialog = true }, {
            Icon(LocalIcon.Delete, "Löschen")
        })

        if(showDeleteDialog) {
            DeleteDialog(
                onDismiss = { showDeleteDialog = false },
                onDelete = {
                    viewModel.deleteCard(card.id, card.imagePath)
                    cardState = CardScreenState.None
                }
            )
        }
    }
}