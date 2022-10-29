package io.github.jan.einkaufszettel.common.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.jan.einkaufszettel.common.ui.icons.Key
import io.github.jan.einkaufszettel.common.ui.icons.LocalIcon
import io.github.jan.einkaufszettel.common.ui.icons.Visibility
import io.github.jan.einkaufszettel.common.ui.icons.VisibilityOff

@Composable
fun PasswordField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    label: String = "Passwort",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        password,
        onValueChange = onPasswordChanged,
        leadingIcon = { Icon(LocalIcon.Key, "Password") },
        label = { Text(label) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = keyboardActions,
        singleLine = true,
        trailingIcon = {
            IconButton({
                visible = !visible
            }) {
                Icon(if(visible) LocalIcon.Visibility else LocalIcon.VisibilityOff, "", tint = MaterialTheme.colorScheme.onBackground)
            }
        },
        modifier = modifier
    )
}