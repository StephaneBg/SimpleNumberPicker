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

package com.sbgapps.simplenumberpicker.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sbgapps.simplenumberpicker.decimal.DecimalPickerDialog;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;
import com.sbgapps.simplenumberpicker.hex.HexaPickerDialog;
import com.sbgapps.simplenumberpicker.hex.HexaPickerHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.bendik.simplerangeview.SimpleRangeView;

public class MainActivity extends AppCompatActivity
        implements HexaPickerHandler, DecimalPickerHandler {

    private static final int REF_DEC_DIALOG = 1;
    private static final int REF_HEX_DIALOG = 2;

    private static final String TAG_DEC_DIALOG = "TAG_DEC_DIALOG";
    private static final String TAG_HEX_DIALOG = "TAG_HEX_DIALOG";

    @BindView(R.id.tv_dec_result)
    TextView decimalTextView;
    @BindView(R.id.tv_hex_result)
    TextView hexaTextView;
    @BindView(R.id.cb_natural)
    CheckBox naturalCheckBox;
    @BindView(R.id.cb_relative)
    CheckBox relativeCheckBox;
    @BindView(R.id.range_view)
    SimpleRangeView rangeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDecimalNumberPicked(int reference, float number) {
        decimalTextView.setText(Float.toString(number));
    }

    @OnClick(R.id.btn_dec_dialog)
    public void showDecimalPicker() {
        new DecimalPickerDialog.Builder()
                .setReference(REF_DEC_DIALOG)
                .setNatural(naturalCheckBox.isChecked())
                .setRelative(relativeCheckBox.isChecked())
                .setTheme(R.style.DecimalPickerTheme)
                .create()
                .show(getSupportFragmentManager(), TAG_DEC_DIALOG);
    }

    @Override
    public void onHexaNumberPicked(int reference, String hexNumber) {
        hexaTextView.setText(hexNumber);
    }

    @OnClick(R.id.btn_hex_dialog)
    public void showHexDialog() {
        new HexaPickerDialog.Builder()
                .setReference(REF_HEX_DIALOG)
                .setMinLength(rangeView.getStart() + 1)
                .setMaxLength(rangeView.getEnd() + 1)
                .setTheme(R.style.HexaPickerTheme)
                .create()
                .show(getSupportFragmentManager(), TAG_HEX_DIALOG);
    }
}
