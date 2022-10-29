package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val LocalIcon.QuestionMark: ImageVector
    get() {
        if (_questionMark != null) {
            return _questionMark!!
        }
        _questionMark = materialIcon(name = "Filled.QuestionMark") {
            materialPath {
                moveTo(11.07f, 12.85f)
                curveToRelative(0.77f, -1.39f, 2.25f, -2.21f, 3.11f, -3.44f)
                curveToRelative(0.91f, -1.29f, 0.4f, -3.7f, -2.18f, -3.7f)
                curveToRelative(-1.69f, 0.0f, -2.52f, 1.28f, -2.87f, 2.34f)
                lineTo(6.54f, 6.96f)
                curveTo(7.25f, 4.83f, 9.18f, 3.0f, 11.99f, 3.0f)
                curveToRelative(2.35f, 0.0f, 3.96f, 1.07f, 4.78f, 2.41f)
                curveToRelative(0.7f, 1.15f, 1.11f, 3.3f, 0.03f, 4.9f)
                curveToRelative(-1.2f, 1.77f, -2.35f, 2.31f, -2.97f, 3.45f)
                curveToRelative(-0.25f, 0.46f, -0.35f, 0.76f, -0.35f, 2.24f)
                horizontalLineToRelative(-2.89f)
                curveTo(10.58f, 15.22f, 10.46f, 13.95f, 11.07f, 12.85f)
                close()
                moveTo(14.0f, 20.0f)
                curveToRelative(0.0f, 1.1f, -0.9f, 2.0f, -2.0f, 2.0f)
                reflectiveCurveToRelative(-2.0f, -0.9f, -2.0f, -2.0f)
                curveToRelative(0.0f, -1.1f, 0.9f, -2.0f, 2.0f, -2.0f)
                reflectiveCurveTo(14.0f, 18.9f, 14.0f, 20.0f)
                close()
            }
        }
        return _questionMark!!
    }

private var _questionMark: ImageVector? = null