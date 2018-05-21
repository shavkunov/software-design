package ru.spbau.shavkunov.grpc

import io.grpc.stub.StreamObserver
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify


@RunWith(MockitoJUnitRunner::class)
class ClientTest {
    private val host = "localhost"
    private val port = 9091
    private val name = "name"

    @Mock
    lateinit var observer: StreamObserver<Chat.ChatMessage>

    lateinit var client: Client

    @Before
    fun setUp() {
        client = Client(host, port, name)
        client.observer = observer
    }

    @Test
    fun verifySendMessage() {
        val defaultMessage = "message"

        val argument = ArgumentCaptor.forClass(Chat.ChatMessage::class.java)
        client.sendMessage(defaultMessage)

        verify(observer).onNext(argument.capture())
        assertEquals(defaultMessage, argument.value.message)
    }
}