import org.gradle.api.JavaVersion

/*
 * Copyright 2018 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Versions {
    val java = JavaVersion.VERSION_1_8
    val androidGradle = "3.3.0"
    val kotlin = "1.3.20"
    val appcompat = "1.0.0"
    val vectorDrawable = "1.0.0"
    val ktx = "1.0.1"
    val anko = "0.10.8"
    val constraintLayout = "1.1.3"
    val material = "1.0.0"
    val rangeView = "0.2.0"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
}

object Android {
    val minSdkVersion = 16
    val targetSdkVersion = 28
    val compileSdkVersion = targetSdkVersion
}

object Libs {
    val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val anko = "org.jetbrains.anko:anko-commons:${Versions.anko}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Versions.vectorDrawable}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val material = "com.google.android.material:material:${Versions.material}"
    val rangeView = "me.bendik.simplerangeview:simplerangeview:${Versions.rangeView}"
}
