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

package com.sbgapps.simplenumberpicker.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.sbgapps.simplenumberpicker.decimal.DecimalPickerDialog
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler
import com.sbgapps.simplenumberpicker.hex.HexaPickerDialog
import com.sbgapps.simplenumberpicker.hex.HexaPickerHandler

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), HexaPickerHandler, DecimalPickerHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        decimalAction.setOnClickListener { showDecimalPicker() }
        hexaAction.setOnClickListener { showHexDialog() }
    }

    @SuppressLint("SetTextI18n")
    override fun onDecimalNumberPicked(reference: Int, number: Float) {
        decimalTextView.text = "$number"
    }

    private fun showDecimalPicker() {
        DecimalPickerDialog.Builder()
                .setReference(REF_DEC_DIALOG)
                .setNatural(naturalCheckBox.isChecked)
                .setRelative(relativeCheckBox.isChecked)
                .setTheme(R.style.DecimalPickerTheme)
                .create()
                .show(supportFragmentManager, TAG_DEC_DIALOG)
    }

    override fun onHexaNumberPicked(reference: Int, hexNumber: String) {
        hexaTextView.text = hexNumber
    }

    private fun showHexDialog() {
        HexaPickerDialog.Builder()
                .setReference(REF_HEX_DIALOG)
                .setMinLength(rangeView.start + 1)
                .setMaxLength(rangeView.end + 1)
                .setTheme(R.style.HexaPickerTheme)
                .create()
                .show(supportFragmentManager, TAG_HEX_DIALOG)
    }

    companion object {
        private const val REF_DEC_DIALOG = 1
        private const val REF_HEX_DIALOG = 2
        private const val TAG_DEC_DIALOG = "TAG_DEC_DIALOG"
        private const val TAG_HEX_DIALOG = "TAG_HEX_DIALOG"
    }
}
