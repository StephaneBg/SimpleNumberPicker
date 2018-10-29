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
    const val androidGradle = "3.2.1"
    const val kotlin = "1.3.0"
    const val appcompat = "1.0.0"
    const val vectorDrawable = "1.0.0"
    const val constraintLayout = "1.1.3"
    const val material = "1.0.0"
    const val rangeView = "0.1.1"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Android {
    val buildToolsVersion = "28.0.3"
    val minSdkVersion = 16
    val targetSdkVersion = 28
    val compileSdkVersion = 28
    val applicationId = "com.sbgapps.simplenumberpicker"
    val versionCode = 1
    val versionName = "0.1"
}

object Libs {
    val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Versions.vectorDrawable}"
    val material = "com.google.android.material:material:${Versions.material}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val rangeView = "me.bendik.simplerangeview:simplerangeview:${Versions.rangeView}"
}
