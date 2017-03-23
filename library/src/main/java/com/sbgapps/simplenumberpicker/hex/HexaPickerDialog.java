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

package com.sbgapps.simplenumberpicker.hex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sbgapps.simplenumberpicker.R;
import com.sbgapps.simplenumberpicker.utils.ThemeUtil;

public class HexaPickerDialog extends DialogFragment {

    private static final String ARG_REFERENCE = "ARG_REFERENCE";
    private static final String ARG_MIN_LENGTH = "ARG_MIN_LENGTH";
    private static final String ARG_MAX_LENGTH = "ARG_MAX_LENGTH";
    private static final String ARG_THEME = "ARG_THEME";
    private static final String ARG_SAVED_VALUE = "ARG_SAVED_VALUE";

    private static final int NB_KEYS = 16;
    private static final int DEFAULT_REFERENCE = 0;
    private static final int NO_MIN_LENGTH = -1;
    private static final int NO_MAX_LENGTH = -1;

    private AlertDialog dialog;
    private TextView numberTextView;
    private ImageButton backspaceButton;

    private int reference = DEFAULT_REFERENCE;
    private int minLength = NO_MIN_LENGTH;
    private int maxLength = NO_MAX_LENGTH;
    private int theme = R.style.SimpleNumberPickerTheme;

    private static HexaPickerDialog newInstance(int reference, int minLength, int maxLength, int theme) {
        Bundle args = new Bundle();
        args.putInt(ARG_REFERENCE, reference);
        args.putInt(ARG_MIN_LENGTH, minLength);
        args.putInt(ARG_MAX_LENGTH, maxLength);
        args.putInt(ARG_THEME, theme);
        HexaPickerDialog fragment = new HexaPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            assignArguments(savedInstanceState);
        } else if (null != getArguments()) {
            assignArguments(getArguments());
        }

        setStyle(STYLE_NO_TITLE, theme);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TypedArray attributes = getContext().obtainStyledAttributes(theme, R.styleable.SimpleNumberPicker);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.snp_dialog_hexadecimal_picker, null);

        // Init number
        int color = attributes.getColor(R.styleable.SimpleNumberPicker_snpKeyColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        numberTextView = (TextView) view.findViewById(R.id.tv_hex_number);
        numberTextView.setTextColor(color);
        if (null != savedInstanceState && savedInstanceState.containsKey(ARG_SAVED_VALUE))
            numberTextView.setText(savedInstanceState.getString(ARG_SAVED_VALUE));

        // Init backspace
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpBackspaceColor,
                ContextCompat.getColor(getContext(), android.R.color.secondary_text_light));
        backspaceButton = (ImageButton) view.findViewById(R.id.key_backspace);
        backspaceButton.setImageDrawable(
                ThemeUtil.makeSelector(getContext(), R.drawable.snp_ic_backspace_black_24dp, color));
        backspaceButton.setOnClickListener(v -> {
            CharSequence number = numberTextView.getText()
                    .subSequence(0, numberTextView.getText().length() - 1);
            numberTextView.setText(number);
            onNumberChanged();
        });
        backspaceButton.setOnLongClickListener(v -> {
            numberTextView.setText("");
            onNumberChanged();
            return true;
        });

        // Create dialog
        dialog = new AlertDialog.Builder(getContext(), theme)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String result = numberTextView.getText().toString();
                    if (result.isEmpty()) result = "0";
                    final String number = numberTextView.getText().toString();
                    final Activity activity = getActivity();
                    final Fragment fragment = getParentFragment();
                    if (activity instanceof HexaPickerHandler) {
                        final HexaPickerHandler handler = (HexaPickerHandler) activity;
                        handler.onHexaNumberPicked(reference, number);
                    } else if (fragment instanceof HexaPickerHandler) {
                        final HexaPickerHandler handler = (HexaPickerHandler) fragment;
                        handler.onHexaNumberPicked(reference, number);
                    }
                    dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dismiss())
                .create();
        // Init dialog
        color = attributes.getColor(R.styleable.SimpleNumberPicker_snpDialogBackground,
                ContextCompat.getColor(getContext(), android.R.color.white));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(color));

        // Init keys
        View.OnClickListener listener = v -> {
            if (NO_MAX_LENGTH != maxLength && maxLength == numberTextView.length()) return;
            int key = (int) v.getTag();
            String id = numberTextView.getText() + Integer.toString(key, 16).toUpperCase();
            numberTextView.setText(id);
            onNumberChanged();
        };

        color = attributes.getColor(
                R.styleable.SimpleNumberPicker_snpKeyColor,
                ThemeUtil.getThemeAccentColor(getContext()));
        TypedArray ids = getResources().obtainTypedArray(R.array.snp_key_ids);
        for (int i = 0; i < NB_KEYS; i++) {
            TextView key = (TextView) view.findViewById(ids.getResourceId(i, -1));
            key.setTag(i);
            key.setOnClickListener(listener);
            key.setTextColor(color);
        }
        ids.recycle();
        attributes.recycle();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        onNumberChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_REFERENCE, reference);
        outState.putInt(ARG_MIN_LENGTH, minLength);
        outState.putInt(ARG_MAX_LENGTH, maxLength);
        outState.putInt(ARG_THEME, theme);
        outState.putString(ARG_SAVED_VALUE, numberTextView.getText().toString());
    }

    private void onNumberChanged() {
        backspaceButton.setEnabled(0 != numberTextView.length());
        if (0 == numberTextView.getText().length()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else if (NO_MIN_LENGTH != minLength && numberTextView.length() < minLength) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void assignArguments(Bundle args) {
        if (args.containsKey(ARG_REFERENCE))
            reference = args.getInt(ARG_REFERENCE);
        if (args.containsKey(ARG_MIN_LENGTH))
            minLength = args.getInt(ARG_MIN_LENGTH);
        if (args.containsKey(ARG_MAX_LENGTH))
            maxLength = args.getInt(ARG_MAX_LENGTH);
        if (args.containsKey(ARG_THEME))
            theme = args.getInt(ARG_THEME);
    }

    public static class Builder {

        private int reference = DEFAULT_REFERENCE;
        private int minLength = NO_MIN_LENGTH;
        private int maxLength = NO_MAX_LENGTH;
        private int theme = R.style.SimpleNumberPickerTheme;

        public Builder setReference(int reference) {
            this.reference = reference;
            return this;
        }

        public Builder setMinLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public HexaPickerDialog create() {
            return newInstance(reference, minLength, maxLength, theme);
        }
    }
}
