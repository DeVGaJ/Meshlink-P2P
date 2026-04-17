package com.devgaj.meshlink

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.devgaj.meshlink.ui.screens.VibrantPurple
import com.devgaj.meshlink.ui.screens.TrueBlack
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Developed by DeVGaJ - Senior QA Automation Engineer
 * Comprehensive UI Test Suite for MeshLink.
 */
@RunWith(AndroidJUnit4::class)
class MeshLinkUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        *(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            emptyArray()
        }),
        *(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            emptyArray()
        })
    )

    @Test
    fun testNightshadeUIThemeRendering() {
        // Assert TopAppBar existence and Branding
        composeTestRule.onNodeWithText("MeshLink").assertExists()
        
        // Assert Theme Colors (Implicitly via checking background or specific elements)
        // Note: Espresso/Compose UI tests usually check for visibility and presence.
        // Direct pixel color checking is done via Screenshot testing, but we verify nodes here.
        composeTestRule.onNodeWithContentDescription("About").assertIsDisplayed()
    }

    @Test
    fun testChatFlow_SendMessage() {
        val testMessage = "P2P Mesh Test Message"

        // 1. Find Chat Input
        val inputField = composeTestRule.onNodeWithText("Encrypted message...")
        inputField.assertExists()

        // 2. Type Message
        inputField.performTextInput(testMessage)

        // 3. Click Send
        composeTestRule.onNodeWithContentDescription("Send").performClick()

        // 4. Verify message appears in LazyColumn
        composeTestRule.onNodeWithText(testMessage).assertIsDisplayed()
    }
}
