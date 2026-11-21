package com.example

import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.UserProfile
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun userProfile_avatarInitials_calculatedCorrectly() {
        val singleNameProfile = UserProfile(fullName = "Westerveld")
        assertEquals("W", singleNameProfile.avatarInitials)

        val doubleNameProfile = UserProfile(fullName = "Jan-Paul Westerveld")
        assertEquals("JW", doubleNameProfile.avatarInitials)

        val multiNameProfile = UserProfile(fullName = "Jan Paul Westerveld")
        assertEquals("JW", multiNameProfile.avatarInitials)
    }

    @Test
    fun userProfile_customAvatar_detectedCorrectly() {
        val noAvatar = UserProfile()
        assertFalse(noAvatar.hasCustomAvatar)

        val withAvatar = UserProfile(profileImageUrl = "https://example.com/pic.jpg")
        assertTrue(withAvatar.hasCustomAvatar)
    }

    @Test
    fun chatMessage_defaults_valid() {
        val message = ChatMessage(text = "Systems check", sender = ChatSender.USER)
        assertEquals("Systems check", message.text)
        assertEquals(ChatSender.USER, message.sender)
        assertFalse(message.isLoading)
        assertTrue(message.timestamp > 0)
    }
}
