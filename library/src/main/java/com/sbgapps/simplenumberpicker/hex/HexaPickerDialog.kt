/*
 * Copyright 2017 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbgapps.simplenumberpicker.hex

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.sbgapps.simplenumberpicker.R
import com.sbgapps.simplenumberpicker.utils.ThemeUtil

class HexaPickerDialog : DialogFragment() {

    lateinit private var dialog: AlertDialog
    lateinit private var numberTextView: TextView
    lateinit private var backspaceButton: ImageButton

    private var reference = DEFAULT_REFERENCE
    private var minLength = NO_MIN_LENGTH
    private var maxLength = NO_MAX_LENGTH
    private var style = R.style.SimpleNumberPickerTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null != savedInstanceState) {
            assignArguments(savedInstanceState)
        } else if (null != arguments) {
            assignArguments(arguments)
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, style)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val attributes = context.obtainStyledAttributes(style, R.styleable.SimpleNumberPicker)

        val view = activity.layoutInflater.inflate(R.layout.snp_dialog_hexadecimal_picker, null)

        // Init number
        var color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor,
                ContextCompat.getColor(context, android.R.color.secondary_text_light))
        numberTextView = view.findViewById(R.id.tv_number)
        numberTextView.setTextColor(color)
        if (savedInstanceState?.containsKey(ARG_SAVED_VALUE) == true)
            numberTextView.text = savedInstanceState.getString(ARG_SAVED_VALUE)

        // Init backspace
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpBackspaceColor,
                ContextCompat.getColor(context, android.R.color.secondary_text_light))
        backspaceButton = view.findViewById(R.id.key_backspace)
        backspaceButton.setImageDrawable(
                ThemeUtil.makeSelector(context, R.drawable.snp_ic_backspace_black_24dp, color))
        backspaceButton.setOnClickListener {
            val number = numberTextView.text.subSequence(0, numberTextView.text.length - 1)
            numberTextView.text = number
            onNumberChanged()
        }
        backspaceButton.setOnLongClickListener {
            numberTextView.text = ""
            onNumberChanged()
            true
        }

        // Create dialog
        dialog = AlertDialog.Builder(context, style)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    if (numberTextView.text.isEmpty()) numberTextView.text = "0"
                    val number = numberTextView.text.toString()

                    val activity = activity
                    val fragment = parentFragment
                    if (activity is HexaPickerHandler) {
                        val handler = activity as HexaPickerHandler
                        handler.onHexaNumberPicked(reference, number)
                    } else if (fragment is HexaPickerHandler) {
                        val handler = fragment as HexaPickerHandler
                        handler.onHexaNumberPicked(reference, number)
                    }
                    dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
                .create()

        // Init dialog
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpDialogBackground,
                ContextCompat.getColor(context, android.R.color.white))
        dialog.window?.setBackgroundDrawable(ColorDrawable(color))

        // Init keys
        val listener = { v: View ->
            if (NO_MAX_LENGTH == maxLength || maxLength != numberTextView.length()) {
                val key = v.tag as Int
                val id = numberTextView.text.toString() + Integer.toString(key, 16).toUpperCase()
                numberTextView.text = id
                onNumberChanged()
            }
        }

        color = attributes.getColor(
                R.styleable.SimpleNumberPicker_snpKeyColor,
                ThemeUtil.getThemeAccentColor(context))
        val ids = resources.obtainTypedArray(R.array.snp_key_ids)
        for (i in 0 until NB_KEYS) {
            val key = view.findViewById<TextView>(ids.getResourceId(i, -1))
            key.tag = i
            key.setOnClickListener(listener)
            key.setTextColor(color)
        }
        ids.recycle()
        attributes.recycle()

        return dialog
    }

    override fun onStart() {
        super.onStart()
        onNumberChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putInt(ARG_REFERENCE, reference)
            putInt(ARG_MIN_LENGTH, minLength)
            putInt(ARG_MAX_LENGTH, maxLength)
            putInt(ARG_STYLE, style)
            putString(ARG_SAVED_VALUE, numberTextView.text.toString())
        }
    }

    private fun onNumberChanged() {
        backspaceButton.isEnabled = 0 != numberTextView.length()
        if (numberTextView.text.isEmpty()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
        } else dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !(NO_MIN_LENGTH != minLength && numberTextView.length() < minLength)
    }

    private fun assignArguments(args: Bundle) {
        if (args.containsKey(ARG_REFERENCE)) reference = args.getInt(ARG_REFERENCE)
        if (args.containsKey(ARG_MIN_LENGTH)) minLength = args.getInt(ARG_MIN_LENGTH)
        if (args.containsKey(ARG_MAX_LENGTH)) maxLength = args.getInt(ARG_MAX_LENGTH)
        if (args.containsKey(ARG_STYLE)) style = args.getInt(ARG_STYLE)
    }

    class Builder {

        private var reference = DEFAULT_REFERENCE
        private var minLength = NO_MIN_LENGTH
        private var maxLength = NO_MAX_LENGTH
        private var theme = R.style.SimpleNumberPickerTheme

        fun setReference(reference: Int): Builder {
            this.reference = reference
            return this
        }

        fun setMinLength(minLength: Int): Builder {
            this.minLength = minLength
            return this
        }

        fun setMaxLength(maxLength: Int): Builder {
            this.maxLength = maxLength
            return this
        }

        fun setTheme(theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun create(): HexaPickerDialog {
            return newInstance(reference, minLength, maxLength, theme)
        }
    }

    companion object {

        private val ARG_REFERENCE = "ARG_REFERENCE"
        private val ARG_MIN_LENGTH = "ARG_MIN_LENGTH"
        private val ARG_MAX_LENGTH = "ARG_MAX_LENGTH"
        private val ARG_STYLE = "ARG_STYLE"
        private val ARG_SAVED_VALUE = "ARG_SAVED_VALUE"

        private val NB_KEYS = 16
        private val DEFAULT_REFERENCE = 0
        private val NO_MIN_LENGTH = -1
        private val NO_MAX_LENGTH = -1

        private fun newInstance(reference: Int,
                                minLength: Int,
                                maxLength: Int,
                                theme: Int):
                HexaPickerDialog {

            val fragment = HexaPickerDialog()
            fragment.arguments = Bundle().apply {
                putInt(ARG_REFERENCE, reference)
                putInt(ARG_MIN_LENGTH, minLength)
                putInt(ARG_MAX_LENGTH, maxLength)
                putInt(ARG_STYLE, theme)
            }
            return fragment
        }
    }
}
