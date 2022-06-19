package better.text.protext.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun Context.getFromClipboard(): String {
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return manager.primaryClip?.getItemAt(0)?.text.toString()
}

fun Context.copyToClipboard(text: String) {
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("Protext", text)
    manager.setPrimaryClip(clipData)
}
