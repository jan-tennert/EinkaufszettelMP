package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val LocalIcon.Home: ImageVector
get() {
    if (_home != null) {
        return _home!!
    }
    _home = materialIcon(name = "Filled.Home") {
        materialPath {
            moveTo(10.0f, 20.0f)
            verticalLineToRelative(-6.0f)
            horizontalLineToRelative(4.0f)
            verticalLineToRelative(6.0f)
            horizontalLineToRelative(5.0f)
            verticalLineToRelative(-8.0f)
            horizontalLineToRelative(3.0f)
            lineTo(12.0f, 3.0f)
            lineTo(2.0f, 12.0f)
            horizontalLineToRelative(3.0f)
            verticalLineToRelative(8.0f)
            close()
        }
    }
    return _home!!
}

private var _home: ImageVector? = null
