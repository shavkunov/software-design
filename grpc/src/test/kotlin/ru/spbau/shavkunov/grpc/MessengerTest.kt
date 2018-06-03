package ru.spbau.shavkunov.grpc

import io.grpc.stub.StreamObserver
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify


class MessengerTest {
    val localhost = "localhost"
    val user1 = "user1"
    val port1 = 9005
    val user2 = "user2"
    val port2 = 9000
    val message1 = "test1"
    val message2 = "test2"


    @Test
    fun sendMessageTest() {
        val checkMessage1: (Chat.ChatMessage) -> Unit  = { request ->
            assertEquals(user2, request.from)
            assertEquals(message2, request.message)
        }

        val checkMessage2: (Chat.ChatMessage) -> Unit  = { request ->
            assertEquals(user1, request.from)
            assertEquals(message1, request.message)
        }

        val messenger1 = Messenger(user1, localhost, port1, port2, checkMessage1)
        messenger1.sendMessage(message1)

        val messenger2 = Messenger(user2, localhost, port2, port1, checkMessage2)
        messenger2.sendMessage(message2)

        Thread.sleep(1000)
        messenger1.shutdown()
        messenger2.shutdown()
    }

    @Test
    fun oneUserTest() {
        val checkMessage: (Chat.ChatMessage) -> Unit  = { request ->
        }

        val messenger = Messenger(user1, localhost, port1, port2, checkMessage)
        val isSent = messenger.sendMessage(message1)

        assertFalse(isSent)
        messenger.shutdown()
    }
}
