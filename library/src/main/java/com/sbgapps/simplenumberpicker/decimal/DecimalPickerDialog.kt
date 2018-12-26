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
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.fragment.app.DialogFragment
import com.sbgapps.simplenumberpicker.R
import com.sbgapps.simplenumberpicker.utils.color
import com.sbgapps.simplenumberpicker.utils.makeSelector
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.find
import java.text.DecimalFormatSymbols

class DecimalPickerDialog : DialogFragment() {

    private lateinit var dialog: AlertDialog
    private lateinit var numberTextView: TextView
    private lateinit var backspaceButton: ImageButton
    private lateinit var decimalSeparator: String

    private var reference = DEFAULT_REFERENCE
    private var relative = true
    private var natural = false
    private var style = R.style.SimpleNumberPickerTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assignArguments(savedInstanceState ?: arguments)
        setStyle(DialogFragment.STYLE_NO_TITLE, style)
        isCancelable = false
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val attributes =
            requireContext().obtainStyledAttributes(style, R.styleable.SimpleNumberPicker)

        val view = requireActivity().layoutInflater
            .inflate(R.layout.snp_dialog_decimal_picker, null)

        // Init number
        var color = attributes.getColor(
            R.styleable.SimpleNumberPicker_snpKeyColor,
            requireContext().colorAttr(R.attr.colorPrimary)
        )
        numberTextView = view.find(R.id.tv_number)
        numberTextView.setTextColor(color)
        if (savedInstanceState?.containsKey(ARG_SAVED_VALUE) == true)
            numberTextView.text = savedInstanceState.getString(ARG_SAVED_VALUE)

        // Init backspace
        color = attributes.getColor(
            R.styleable.SimpleNumberPicker_snpBackspaceColor,
            requireContext().colorAttr(R.attr.colorPrimary)
        )
        backspaceButton = view.find(R.id.key_backspace)
        backspaceButton.setImageDrawable(
            makeSelector(requireContext(), R.drawable.snp_ic_backspace_black_24dp, color)
        )
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
        dialog = AlertDialog.Builder(requireContext(), theme)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                var result = numberTextView.text.toString()
                if (result.isEmpty()) result = "0"
                result = result.replace(',', '.')
                if (result == ".") result = "0"
                val number = result.toFloat()

                val fragmentActivity = activity
                val fragment = parentFragment
                if (fragmentActivity is DecimalPickerHandler) {
                    fragmentActivity.onDecimalNumberPicked(reference, number)
                } else if (fragment is DecimalPickerHandler) {
                    fragment.onDecimalNumberPicked(reference, number)
                }
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()

        // Init dialog
        color = attributes.getColor(
            R.styleable.SimpleNumberPicker_snpDialogBackground,
            requireContext().color(android.R.color.white)
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(color))

        // Init keys
        val listener = { v: View ->
            val key = v.tag as Int
            val id = "${numberTextView.text}$key"
            numberTextView.text = id
            onNumberChanged()
        }

        color = attributes.getColor(
            R.styleable.SimpleNumberPicker_snpKeyColor,
            requireContext().colorAttr(R.attr.colorAccent)
        )
        val ids = resources.obtainTypedArray(R.array.snp_key_ids)
        for (i in 0 until NB_KEYS) {
            val key = view.find<TextView>(ids.getResourceId(i, -1))
            key.tag = i
            key.setOnClickListener(listener)
            key.setTextColor(color)
        }

        // Init sign
        val sign = view.find<TextView>(R.id.key_sign)
        if (relative) {
            sign.setTextColor(color)
            sign.setOnClickListener {
                val number = numberTextView.text.toString()
                if (number.startsWith("-")) numberTextView.text = number.drop(1)
                else numberTextView.text = "-$number"
                onNumberChanged()
            }
        } else {
            sign.isInvisible = true
        }

        // Init decimal separator
        decimalSeparator = DecimalFormatSymbols().decimalSeparator.toString()
        val separator = view.find<TextView>(R.id.key_point)
        if (natural) {
            separator.isInvisible = true
        } else {
            separator.text = decimalSeparator
            separator.setTextColor(color)
            separator.setOnClickListener {
                if (!numberTextView.text.toString().contains(decimalSeparator)) {
                    numberTextView.text = "${numberTextView.text}$decimalSeparator"
                    onNumberChanged()
                }
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

    private fun assignArguments(args: Bundle?) {
        if (args?.containsKey(ARG_REFERENCE) == true) reference = args.getInt(ARG_REFERENCE)
        if (args?.containsKey(ARG_RELATIVE) == true) relative = args.getBoolean(ARG_RELATIVE)
        if (args?.containsKey(ARG_NATURAL) == true) natural = args.getBoolean(ARG_NATURAL)
        if (args?.containsKey(ARG_STYLE) == true) style = args.getInt(ARG_STYLE)
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

        fun create(): DecimalPickerDialog = newInstance(reference, relative, natural, theme)
    }

    companion object {

        private const val ARG_REFERENCE = "ARG_REFERENCE"
        private const val ARG_RELATIVE = "ARG_RELATIVE"
        private const val ARG_NATURAL = "ARG_NATURAL"
        private const val ARG_STYLE = "ARG_STYLE"
        private const val ARG_SAVED_VALUE = "ARG_SAVED_VALUE"

        private const val NB_KEYS = 10
        private const val DEFAULT_REFERENCE = 0

        private fun newInstance(
            reference: Int,
            relative: Boolean,
            natural: Boolean,
            theme: Int
        ) = DecimalPickerDialog().apply {
            arguments = bundleOf(
                ARG_REFERENCE to reference,
                ARG_RELATIVE to relative,
                ARG_NATURAL to natural,
                ARG_STYLE to theme
            )
        }
    }
}
