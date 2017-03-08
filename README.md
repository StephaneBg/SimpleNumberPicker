# SimpleNumberPicker

A customisable natural and hexadecimal material picker view for Android.

<img src="https://raw.githubusercontent.com/StephaneBg/SimpleNumberPicker/master/artwork/decimal_picker.png">
<img src="https://raw.githubusercontent.com/StephaneBg/SimpleNumberPicker/master/artwork/hexa_picker.png">

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
And add the dependency
```
	dependencies {
	        compile 'com.github.StephaneBg:SimpleNumberPicker:1.0'
	}
```

## Usage

Parameters are optional.

### DecimalPickerDialog

To display a natural picker `DialogFragment`:
``` java
        new DecimalPickerDialog.Builder()
                .setKey("CustomTag")
                .create()
                .show(getSupportFragmentManager(), TAG_DEC_DIALOG);
```

### HexadecimalPickerDialog
To display a hexadecimal picker `DialogFragment`:
``` java
        new HexaPickerDialog.Builder()
                .setKey("CustomTag")
                .setMinLength(2)
                .setMaxLength(8)
                .create()
                .show(getSupportFragmentManager(), TAG_HEX_DIALOG);
```

## Handler
Your parent `Activity` or parent `Fragment` must implement `DecimalPickerHandler` or `HexaPickerHandler`.

## TODO
Add ability to style dialog.

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
