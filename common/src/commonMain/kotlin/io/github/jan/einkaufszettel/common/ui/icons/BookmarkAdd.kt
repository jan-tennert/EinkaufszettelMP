package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val LocalIcon.BookmarkAdd: ImageVector
    get() {
        if (_bookmarkAdd != null) {
            return _bookmarkAdd!!
        }
        _bookmarkAdd = materialIcon(name = "Filled.BookmarkAdd") {
            materialPath {
                moveTo(21.0f, 7.0f)
                horizontalLineToRelative(-2.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(-2.0f)
                verticalLineTo(7.0f)
                horizontalLineToRelative(-2.0f)
                verticalLineTo(5.0f)
                horizontalLineToRelative(2.0f)
                verticalLineTo(3.0f)
                horizontalLineToRelative(2.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(2.0f)
                verticalLineTo(7.0f)
                close()
                moveTo(19.0f, 21.0f)
                lineToRelative(-7.0f, -3.0f)
                lineToRelative(-7.0f, 3.0f)
                verticalLineTo(5.0f)
                curveToRelative(0.0f, -1.1f, 0.9f, -2.0f, 2.0f, -2.0f)
                lineToRelative(7.0f, 0.0f)
                curveToRelative(-0.63f, 0.84f, -1.0f, 1.87f, -1.0f, 3.0f)
                curveToRelative(0.0f, 2.76f, 2.24f, 5.0f, 5.0f, 5.0f)
                curveToRelative(0.34f, 0.0f, 0.68f, -0.03f, 1.0f, -0.1f)
                verticalLineTo(21.0f)
                close()
            }
        }
        return _bookmarkAdd!!
    }

private var _bookmarkAdd: ImageVector? = null