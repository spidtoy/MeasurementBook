package com.example.measurementbook.ui.sharedui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.measurementbook.R
import com.example.measurementbook.generalutilities.uiutilities.Size

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = PaddingValues(start = 30.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        RoundedCornerShape(50.dp),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.primary,
        unfocusedBorderColor = MaterialTheme.colors.primary,
        cursorColor = MaterialTheme.colors.primary
    )
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    @OptIn(ExperimentalMaterialApi::class)
    (BasicTextField(
        value = value,
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(
                false, isError, interactionSource, colors, focusedIndicatorLineThickness = 0.dp,
                unfocusedIndicatorLineThickness = 0.dp
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPadding,
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape,
                        2.dp,
                        2.dp
                    )
                }
            )
        }
    ))
}
@Composable
fun PasswordTextField(password: String,
                        onValueChange: (String) -> Unit,
                        togglePassword: () -> Unit,
                        placeholder: String,
                        showPassword: Boolean
   ) {
    val focusManager = LocalFocusManager.current
    val size = Size()
    val screenWidth = size.width()
    MyTextField(value = password,
        onValueChange = { onValueChange(it) } ,
        modifier = Modifier.height(45.dp).width((screenWidth - 30).dp),
        textStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.primary),
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        placeholder = { Row { Spacer(modifier = Modifier.width(20.dp))
            Text(text = placeholder,
            fontSize = 14.sp, color = MaterialTheme.colors.secondaryVariant,
        ) }},
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }),
        trailingIcon = {
            val icon = if (showPassword) { painterResource(id = R.drawable.baseline_visibility_24) }
            else { painterResource(id = R.drawable.baseline_visibility_off_24) }

            IconButton(onClick = togglePassword) {
                Image(painter = icon,
                    contentDescription = stringResource(R.string.visibility),
                    alignment = Alignment.Center,
                )
            }
        }
    )

}

@Composable
fun OrdinaryTextField(text: String,
                      onValueChange: (String) -> Unit,
                      placeholder: String,
                      modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    MyTextField(value = text,
        onValueChange = { onValueChange(it) } ,
        modifier = modifier,
        textStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.primary),
        singleLine = true,
        placeholder = { Row { Spacer(modifier = Modifier.width(20.dp))
            Text(text = placeholder,
                fontSize = 14.sp, color = MaterialTheme.colors.secondaryVariant,
            ) }},
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }),
        trailingIcon = {
            IconButton(onClick = {
                onValueChange("")
            },
            modifier = Modifier.size(16.dp)) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                )
            }
        }
    )

}