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

package fr.baiget.simplenumberpicker.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.baiget.simplenumberpicker.decimal.DecimalPickerDialog
import fr.baiget.simplenumberpicker.decimal.DecimalPickerHandler
import fr.baiget.simplenumberpicker.hex.HexaPickerDialog
import fr.baiget.simplenumberpicker.hex.HexaPickerHandler
import me.bendik.simplerangeview.SimpleRangeView
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity(), HexaPickerHandler,
    DecimalPickerHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        find<Button>(R.id.decimalAction).setOnClickListener { showDecimalPicker() }
        find<Button>(R.id.hexaAction).setOnClickListener { showHexDialog() }
    }

    @SuppressLint("SetTextI18n")
    override fun onDecimalNumberPicked(reference: Int, number: Float) {
        find<TextView>(R.id.decimalTextView).text = "$number"
    }

    private fun showDecimalPicker() = DecimalPickerDialog.Builder()
        .setReference(REF_DEC_DIALOG)
        .setNatural(find<CheckBox>(R.id.naturalCheckBox).isChecked)
        .setRelative(find<CheckBox>(R.id.relativeCheckBox).isChecked)
        .setTheme(R.style.DecimalPickerTheme)
        .create()
        .show(supportFragmentManager,
            TAG_DEC_DIALOG
        )

    override fun onHexaNumberPicked(reference: Int, hexNumber: String) {
        find<TextView>(R.id.hexaTextView).text = hexNumber
    }

    private fun showHexDialog() = HexaPickerDialog.Builder()
        .setReference(REF_HEX_DIALOG)
        .setMinLength(find<SimpleRangeView>(R.id.rangeView).start + 1)
        .setMaxLength(find<SimpleRangeView>(R.id.rangeView).end + 1)
        .setTheme(R.style.HexaPickerTheme)
        .create()
        .show(supportFragmentManager,
            TAG_HEX_DIALOG
        )

    companion object {
        private const val REF_DEC_DIALOG = 1
        private const val REF_HEX_DIALOG = 2
        private const val TAG_DEC_DIALOG = "TAG_DEC_DIALOG"
        private const val TAG_HEX_DIALOG = "TAG_HEX_DIALOG"
    }
}
