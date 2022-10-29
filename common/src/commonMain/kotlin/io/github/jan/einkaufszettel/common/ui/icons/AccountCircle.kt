package io.github.jan.einkaufszettel.common.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val LocalIcon.AccountCircle: ImageVector
    get() {
        if (_accountCircle != null) {
            return _accountCircle!!
        }
        _accountCircle = materialIcon(name = "Filled.AccountCircle") {
            materialPath {
                moveTo(12.0f, 2.0f)
                curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
                reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
                reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
                reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
                close()
                moveTo(12.0f, 5.0f)
                curveToRelative(1.66f, 0.0f, 3.0f, 1.34f, 3.0f, 3.0f)
                reflectiveCurveToRelative(-1.34f, 3.0f, -3.0f, 3.0f)
                reflectiveCurveToRelative(-3.0f, -1.34f, -3.0f, -3.0f)
                reflectiveCurveToRelative(1.34f, -3.0f, 3.0f, -3.0f)
                close()
                moveTo(12.0f, 19.2f)
                curveToRelative(-2.5f, 0.0f, -4.71f, -1.28f, -6.0f, -3.22f)
                curveToRelative(0.03f, -1.99f, 4.0f, -3.08f, 6.0f, -3.08f)
                curveToRelative(1.99f, 0.0f, 5.97f, 1.09f, 6.0f, 3.08f)
                curveToRelative(-1.29f, 1.94f, -3.5f, 3.22f, -6.0f, 3.22f)
                close()
            }
        }
        return _accountCircle!!
    }

private var _accountCircle: ImageVector? = null