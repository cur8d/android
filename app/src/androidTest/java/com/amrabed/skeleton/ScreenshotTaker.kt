package com.amrabed.skeleton

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar
import tools.fastlane.screengrab.locale.LocaleTestRule

@RunWith(AndroidJUnit4::class)
class ScreenshotTaker {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun takeScreenshots() {
        Screengrab.screenshot("main_activity")
    }

    companion object {
        @get:ClassRule
        @JvmStatic
        val localeTestRule = LocaleTestRule()

        @BeforeClass
        @JvmStatic
        fun cleanStatusBar() {
            CleanStatusBar()
                .setShowNotifications(false)
                .enable()
        }

        @AfterClass
        @JvmStatic
        fun done() {
            CleanStatusBar.disable()
        }
    }
}