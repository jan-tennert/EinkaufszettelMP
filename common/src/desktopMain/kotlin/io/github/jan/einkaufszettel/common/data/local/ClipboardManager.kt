package io.github.jan.einkaufszettel.common.data.local

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual class ClipboardManager {

    actual fun copyToClipboard(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard;
        clipboard.setContents(StringSelection(text), null);
    }

}