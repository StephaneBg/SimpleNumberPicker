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
    const val androidGradle = "3.1.3"
    const val kotlin = "1.2.60"
    const val supportLibrary = "27.1.1"
    const val constraintLayout = "1.1.0"
    const val rangeView = "0.1.1"
}

object Build {
    val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Android {
    val buildToolsVersion = "28.0.1"
    val minSdkVersion = 16
    val targetSdkVersion = 27
    val compileSdkVersion = 27
    val applicationId = "com.sbgapps.simplenumberpicker"
    val versionCode = 1
    val versionName = "0.1"
}

object Libs {
    val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val appcompat = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    val design = "com.android.support:design:${Versions.supportLibrary}"
    val vectorDrawable = "com.android.support:support-vector-drawable:${Versions.supportLibrary}"
    val gridlayout = "com.android.support:gridlayout-v7:${Versions.supportLibrary}"
    val constraintLayout =
        "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    val rangeView = "me.bendik.simplerangeview:simplerangeview:${Versions.rangeView}"
}
