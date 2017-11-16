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

package com.sbgapps.simplenumberpicker.decimal

import android.annotation.SuppressLint
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
import java.text.DecimalFormatSymbols

class DecimalPickerDialog : DialogFragment() {

    lateinit private var dialog: AlertDialog
    lateinit private var numberTextView: TextView
    lateinit private var backspaceButton: ImageButton
    lateinit private var decimalSeparator: String

    private var reference = DEFAULT_REFERENCE
    private var relative = true
    private var natural = false
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

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val attributes = context.obtainStyledAttributes(style, R.styleable.SimpleNumberPicker)

        val view = activity.layoutInflater.inflate(R.layout.snp_dialog_decimal_picker, null)

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
            var number = numberTextView.text.subSequence(0, numberTextView.text.length - 1)
            if (1 == number.length && '-' == number[0]) number = ""
            numberTextView.text = number
            onNumberChanged()
        }
        backspaceButton.setOnLongClickListener {
            numberTextView.text = ""
            onNumberChanged()
            true
        }

        // Create dialog
        dialog = AlertDialog.Builder(context, theme)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    var result = numberTextView.text.toString()
                    if (result.isEmpty()) result = "0"
                    result = result.replace(',', '.')
                    if (result == ".") result = "0"
                    val number = result.toFloat()

                    val activity = activity
                    val fragment = parentFragment
                    if (activity is DecimalPickerHandler) {
                        val handler = activity as DecimalPickerHandler
                        handler.onDecimalNumberPicked(reference, number)
                    } else if (fragment is DecimalPickerHandler) {
                        val handler = fragment as DecimalPickerHandler
                        handler.onDecimalNumberPicked(reference, number)
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
            val key = v.tag as Int
            val id = numberTextView.text.toString() + Integer.toString(key)
            numberTextView.text = id
            onNumberChanged()
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

        // Init sign
        val sign = view.findViewById<TextView>(R.id.key_sign)
        if (relative) {
            sign.setTextColor(color)
            sign.setOnClickListener { v ->
                val number = numberTextView.text.toString()
                if (number.startsWith("-")) numberTextView.text = number.substring(1)
                else numberTextView.text = "-" + number
                onNumberChanged()
            }
        } else {
            sign.visibility = View.INVISIBLE
        }

        // Init decimal separator
        initDecimalSeparator()
        val separator = view.findViewById<TextView>(R.id.key_point)
        if (natural) {
            separator.visibility = View.INVISIBLE
        } else {
            separator.text = decimalSeparator
            separator.setTextColor(color)
            separator.setOnClickListener { v ->
                if (numberTextView.text.toString().contains(decimalSeparator)) return@setOnClickListener
                val number = numberTextView.text.toString()
                numberTextView.text = number + decimalSeparator
                onNumberChanged()
            }
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
            putBoolean(ARG_RELATIVE, relative)
            putBoolean(ARG_NATURAL, natural)
            putInt(ARG_STYLE, theme)
            putString(ARG_SAVED_VALUE, numberTextView.text.toString())
        }
    }

    private fun onNumberChanged() {
        backspaceButton.isEnabled = 0 != numberTextView.length()
        if (numberTextView.text.isEmpty()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled =
                    !(1 == numberTextView.text.length && '-' == numberTextView.text[0])
        }
    }

    private fun initDecimalSeparator() {
        val formatSymbols = DecimalFormatSymbols()
        decimalSeparator = "" + formatSymbols.decimalSeparator
    }

    private fun assignArguments(args: Bundle) {
        if (args.containsKey(ARG_REFERENCE)) reference = args.getInt(ARG_REFERENCE)
        if (args.containsKey(ARG_RELATIVE)) relative = args.getBoolean(ARG_RELATIVE)
        if (args.containsKey(ARG_NATURAL)) natural = args.getBoolean(ARG_NATURAL)
        if (args.containsKey(ARG_STYLE)) style = args.getInt(ARG_STYLE)
    }

    class Builder {

        private var reference = DEFAULT_REFERENCE
        private var relative = true
        private var natural = false
        private var theme = R.style.SimpleNumberPickerTheme

        fun setReference(reference: Int): Builder {
            this.reference = reference
            return this
        }

        fun setRelative(relative: Boolean): Builder {
            this.relative = relative
            return this
        }

        fun setNatural(natural: Boolean): Builder {
            this.natural = natural
            return this
        }

        fun setTheme(theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun create(): DecimalPickerDialog {
            return newInstance(reference, relative, natural, theme)
        }
    }

    companion object {

        private val ARG_REFERENCE = "ARG_REFERENCE"
        private val ARG_RELATIVE = "ARG_RELATIVE"
        private val ARG_NATURAL = "ARG_NATURAL"
        private val ARG_STYLE = "ARG_STYLE"
        private val ARG_SAVED_VALUE = "ARG_SAVED_VALUE"

        private val NB_KEYS = 10
        private val DEFAULT_REFERENCE = 0

        private fun newInstance(reference: Int,
                                relative: Boolean,
                                natural: Boolean,
                                theme: Int):
                DecimalPickerDialog {

            val fragment = DecimalPickerDialog()
            fragment.arguments = Bundle().apply {
                putInt(ARG_REFERENCE, reference)
                putBoolean(ARG_RELATIVE, relative)
                putBoolean(ARG_NATURAL, natural)
                putInt(ARG_STYLE, theme)
            }
            return fragment
        }
    }
}
