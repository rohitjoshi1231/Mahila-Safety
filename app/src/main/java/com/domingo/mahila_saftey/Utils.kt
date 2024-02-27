package com.domingo.mahila_saftey

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

// Utility class
object Utils {
    fun customColor(context: Context, colorResourceId: Int): Int {
        return ContextCompat.getColor(context, colorResourceId)
    }

    interface RenameFileCallback {
        fun onFileRenamed(fileName: String)
    }

    fun renameFile(context: Context, callback: RenameFileCallback) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
        builder.setTitle("Rename file")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER_VERTICAL

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.edit)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 0, 16, 0)
        params.height = 40
        params.width = 40
        imageView.layoutParams = params

        val editText = EditText(context)
        editText.setHintTextColor(customColor(context, R.color.subtext))
        editText.setTextColor(Color.WHITE)
        editText.isAllCaps = false
        editText.background = null
        editText.hint = "Untitled"

        layout.addView(imageView)
        layout.addView(editText)

        builder.setView(layout)

        builder.setPositiveButton("Rename") { _, _ ->
            val fileName = editText.text.toString()
            if (fileName.isNotBlank()) {
                callback.onFileRenamed(fileName)
            } else {
                editText.error = "Please enter a folder name"
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
