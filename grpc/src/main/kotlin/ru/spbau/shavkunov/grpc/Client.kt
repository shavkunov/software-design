package ru.spbau.shavkunov.grpc

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.util.*


private val LOGGER = LoggerFactory.getLogger("Client")
const val MILLIS_IN_SECONDS = 1000

/**
 * Client for chatting
 * host -- name of connecting host
 * port -- host port
 * name -- user name in chat
 */
class Client(host: String,
             port: Int,
             private val name: String) {
    private val channel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext(true)
            .build()

    private val stub = ChatServiceGrpc.newStub(channel)
    var observer: StreamObserver<Chat.ChatMessage>?= null

    /**
     * Connecting to server. Need to be done before sending messages.
     */
    fun connect() {
        println("Enter your message: ")
        observer = stub.chat(object: StreamObserver<Chat.ChatMessageFromServer> {
            override fun onNext(value: Chat.ChatMessageFromServer) {
                LOGGER.info("Received from server message: {}", value)
                val secs = value.timestamp.seconds
                val date = Date(secs * MILLIS_IN_SECONDS)

                println("<$date> [${value.message.from}]: ${value.message.message}")
            }

            override fun onError(t: Throwable) {
                LOGGER.error("Failed on error: {}", t)
            }

            override fun onCompleted() {
                LOGGER.info("client completed")
            }
        })
    }

    /**
     * Send message to chat
     */
    fun sendMessage(message: String) {
        observer!!.onNext(Chat.ChatMessage.newBuilder()
                .setFrom(name)
                .setMessage(message)
                .build())
    }

    /**
     * Shutdown the client
     */
    fun shutdown() {
        channel.shutdown()
    }
}

/**
 * CLI for client. Args:
 * first -- host
 * second -- port(Integer value)
 * third -- name in chat
 */
fun main(args: Array<String>) {
    val host = args[0]
    val port = Integer.parseInt(args[1])
    val name = args[2]

    val client = Client(host, port, name)
    client.connect()

    while (true) {
        val input = readLine() ?: break
        client.sendMessage(input)
    }

    client.shutdown()
}