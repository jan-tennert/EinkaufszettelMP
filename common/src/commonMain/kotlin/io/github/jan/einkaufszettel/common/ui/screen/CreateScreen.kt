package io.github.jan.einkaufszettel.common.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import einkaufszettel.db.LocalUserDto
import io.github.jan.einkaufszettel.common.EinkaufszettelViewModel
import io.github.jan.einkaufszettel.common.data.remote.FileInfo
import io.github.jan.einkaufszettel.common.toComposeImage
import io.github.jan.einkaufszettel.common.ui.components.ImageChooser
import io.github.jan.einkaufszettel.common.ui.components.UserCard
import io.github.jan.einkaufszettel.common.ui.dialog.UserAddDialog
import io.github.jan.einkaufszettel.common.ui.events.UIEvent
import io.github.jan.einkaufszettel.common.ui.icons.ArrowBack
import io.github.jan.einkaufszettel.common.ui.icons.Done
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.QuestionMark

@Composable
fun CreateScreen(
    viewModel: EinkaufszettelViewModel,
    oldName: String = "",
    oldAuthorizedUserIds: List<String> = listOf(),
    oldImage: @Composable (() -> Unit)? = null,
    modifyImageEnabled: Boolean = true,
    create: (name: String, fileInfo: FileInfo?, authorizedUsers: List<String>) -> Unit,
    back: () -> Unit
) {
    val localUsers by viewModel.localUserFlow.collectAsState(listOf())
    val oldAuthorizedUsers = remember(oldAuthorizedUserIds, localUsers) {
        if(oldAuthorizedUserIds.isNotEmpty()) {
            localUsers.filter { oldAuthorizedUserIds.contains(it.id) }
        } else listOf()
    }
    var showUserAddDialog by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    var name by remember { mutableStateOf(oldName) }
    var fileInfo by remember { mutableStateOf<FileInfo?>(null) }
    val imageBitmap by remember(fileInfo) {
        if(fileInfo != null) {
            mutableStateOf(fileInfo!!.bytes.toComposeImage())
        } else {
            mutableStateOf(null)
        }
    }
    var showImageChooser by remember { mutableStateOf(false) }
    val authorizedUsers = remember(oldAuthorizedUsers) { mutableStateListOf<LocalUserDto>(*oldAuthorizedUsers.toTypedArray()) }
    Column(
 //       verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            label = { Text("Name") }
        )
        Box(modifier = Modifier.padding(10.dp).let { if(modifyImageEnabled) it.clickable { showImageChooser = true } else it }) {
            if(oldImage != null) {
              oldImage()
            } else if(imageBitmap == null) {
                Icon(
                    LocalIcon.QuestionMark, "", modifier = Modifier
                        .size(100.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onBackground
                        ),
                        tint = MaterialTheme.colorScheme.onBackground)
            } else {
                Image(
                    imageBitmap!!, "", modifier = Modifier
                        .size(100.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onBackground
                        ))
            }
        }
        if(modifyImageEnabled) {
            ImageChooser(showImageChooser) {
                fileInfo = it
                showImageChooser = false
            }
        }

        Text("Autorisierte Benutzer", modifier = Modifier.padding(10.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(0.8f),
            state = lazyListState,
        ) {
            items(localUsers, { it.id }) { user ->
                UserCard(user, authorizedUsers.contains(user), { viewModel.removeProfile(user.id) }) {
                    if(user in authorizedUsers) {
                        authorizedUsers.remove(user)
                    } else {
                        authorizedUsers.add(user)
                    }
                }
            }

            item {
                Button(onClick = { showUserAddDialog = true }, modifier = Modifier.padding(10.dp)) {
                    Text("Benutzer hinzufügen")
                }
            }
        }

        if(showUserAddDialog) {
            UserAddDialog(viewModel) {
                showUserAddDialog = false;
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton({
            back()
        }, modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100)) {
            Icon(LocalIcon.ArrowBack, "Zurück")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                if(name.isNotBlank() && (fileInfo != null || oldImage != null)) {
                    create(name, fileInfo, authorizedUsers.map { it.id })
                    back()
                } else {
                    viewModel.events.add(UIEvent.Alert("Bitte gib einen Namen an und wähle ein Icon aus"))
                }
            },
            modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(100),
        ) {
            Icon(LocalIcon.Done, "Fertig")
        }
    }
}