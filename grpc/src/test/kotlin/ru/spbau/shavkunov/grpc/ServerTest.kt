package ru.spbau.shavkunov.grpc

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import junit.framework.Assert.assertEquals
import org.junit.Test


class ServerTest {
    private val host = "localhost"
    private val port = 9091
    private val name = "name"

    @Test
    fun verifyServerMessage() {
        val channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build()
        val stub = ChatServiceGrpc.newStub(channel)

        val server = Server(port)
        Thread({
            server.start()
        }).start()
        Thread.sleep(1000)

        var receivedMessage: String? = null
        val observer = stub.chat(object : StreamObserver<Chat.ChatMessageFromServer> {
            override fun onNext(value: Chat.ChatMessageFromServer) {
                receivedMessage = value.message.message
            }

            override fun onError(t: Throwable) {
            }

            override fun onCompleted() {

            }
        })

        val defaultMessage = "message"
        observer!!.onNext(Chat.ChatMessage.newBuilder()
                .setFrom(name)
                .setMessage(defaultMessage)
                .build())

        Thread.sleep(1000)
        assertEquals(defaultMessage, receivedMessage)

        server.shutdown()
    }
}