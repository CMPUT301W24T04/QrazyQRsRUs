/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.qrazyqrsrus;

import androidx.test.espresso.matcher.BoundedDiagnosingMatcher;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A Matcher for Espresso that checks if an ImageView has a drawable applied to it.
 */

//this custom matcher is from the code sample https://github.com/android/testing-samples/blob/main/ui/espresso/IntentsAdvancedSample/app/src/androidTest/java/com/example/android/testing/espresso/intents/AdvancedSample/ImageViewHasDrawableMatcher.java Accessed on April 7th, 2024
//this resource was recommended by the Android Developer pages https://developer.android.com/training/testing/espresso/intents?_gl=1*1rhenrf*_up*MQ..*_ga*MTcxMTIzOTY3MC4xNzEyNDk1ODg4*_ga_6HH9YJMN9M*MTcxMjQ5NTg4OC4xLjAuMTcxMjQ5NTg4OC4wLjAuMA..#java

public class ImageViewHasDrawableMatcher {

    public static Matcher<View> hasDrawable() {
        return new BoundedDiagnosingMatcher<View, ImageView>(ImageView.class) {
            @Override
            protected void describeMoreTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            protected boolean matchesSafely(ImageView imageView, Description mismatchDescription) {
                return imageView.getDrawable() != null;
            }
        };
    }
    public static Matcher<View> hasNoDrawable() {
        return new BoundedDiagnosingMatcher<View, ImageView>(ImageView.class) {
            @Override
            protected void describeMoreTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            protected boolean matchesSafely(ImageView imageView, Description mismatchDescription) {
                return imageView.getDrawable() == null;
            }
        };
    }
}