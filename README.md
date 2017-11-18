[![Release](https://jitpack.io/v/StephaneBg/SimpleNumberPicker.svg)](https://jitpack.io/#StephaneBg/SimpleNumberPicker)

# SimpleNumberPicker

A customisable decimal and hexadecimal material picker view for Android.

<img src="https://raw.githubusercontent.com/StephaneBg/SimpleNumberPicker/master/artwork/decimal_picker.png"><img src="https://raw.githubusercontent.com/StephaneBg/SimpleNumberPicker/master/artwork/hexa_picker.png">

## Download
Add the JitPack repository in your build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
And add the dependencies
```
	dependencies {
	        implementation 'com.github.StephaneBg:SimpleNumberPicker:{latest-version}'
		implementation "com.android.support:gridlayout-v7:$supportVersion"
		implementation "com.android.support.constraint:constraint-layout:$constraintVersion"
	}
```

## Usage

### DecimalPickerDialog

To display a decimal picker `DialogFragment`:
``` java
        new DecimalPickerDialog.Builder()
                .setReference(REF_DEC_DIALOG) // Optional
                .setNatural(false) // Optional - false is default
                .setRelative(true) // Optional - true is default
                .setTheme(R.style.DecimalPickerTheme) // Optional
                .create()
                .show(getSupportFragmentManager(), TAG_DEC_DIALOG);
```

### HexaPickerDialog

To display a hexadecimal picker `DialogFragment`:
``` java
        new HexaPickerDialog.Builder()
                .setReference(REF_HEX_DIALOG) // Optional
                .setMinLength(2) // Optional - Default is none
                .setMaxLength(8) // Optional - Default is none
                .setTheme(R.style.HexaPickerTheme) // Optional
                .create()
                .show(getSupportFragmentManager(), TAG_HEX_DIALOG);
```

## Handler
Your parent `Activity` or parent `Fragment` must implement `DecimalPickerHandler` or `HexaPickerHandler`.

## Styling

 1. You can use your own themes if you'd like to change certain attributes.  SimpleNumberPicker currently allows for customization of the following attributes:

        snpKeyColor              :: color of the keys
        snpNumberColor           :: color of the entered number
        snpBackspaceColor        :: color of the backspace button
        snpDialogBackground      :: color of the dialog background

 2. Create your own custom style in `styles.xml`:

  ```xml
    <style name="DecimalPickerTheme" parent="SimpleNumberPickerTheme">
        <item name="snpKeyColor">@android:color/white</item>
        <item name="snpNumberColor">@android:color/white</item>
        <item name="snpBackspaceColor">@android:color/white</item>
        <item name="colorAccent">@android:color/white</item>
        <item name="snpDialogBackground">@color/color_primary</item>
    </style>
  ```

See sample for more details.

## Contribution

### Pull requests are welcome!

Feel free to contribute to SimpleNumberPicker.

If you've fixed a bug or have a feature you've added, just create a pull request. If you've found a bug, want a new feature, or have other questions, file an issue. I will try to answer as soon as possible.

### Applications using SimpleNumberPicker

Please send a pull request if you would like to be added here.

## License
Copyright 2017 Stéphane Baiget

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
