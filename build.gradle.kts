import org.jlleitschuh.gradle.ktlint.KtlintExtension

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

plugins {
    id("com.github.ben-manes.versions") version "0.20.0"
    id("org.jlleitschuh.gradle.ktlint") version "7.0.0"
    id("com.github.dcendents.android-maven") version "2.1"
    id("com.jfrog.bintray") version "1.8.4"
}

buildscript {
    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", Versions.kotlin))
        classpath(Build.androidGradle)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        android.set(true)
    }
}
