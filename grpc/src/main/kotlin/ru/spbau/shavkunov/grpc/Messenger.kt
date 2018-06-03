package ru.spbau.shavkunov.grpc

import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.util.*


private val LOGGER = LoggerFactory.getLogger("Messenger")
const val MILLIS_IN_SECONDS = 1000

class Messenger(
        val name: String,
        val peerAddress: String,
        val port: Int,
        val peerPort: Int,
        val messageProcessor: (Chat.ChatMessage) -> Unit
) {
    private val blockingStub: ChatServiceGrpc.ChatServiceBlockingStub
    private val server =
            ServerBuilder.forPort(port)
                    .addService(MessengerService(messageProcessor))
                    .build()
                    .start()

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                server.shutdown()
            }
        })

        LOGGER.info("Creating channel")
        val channel = ManagedChannelBuilder
                .forAddress(peerAddress, peerPort)
                .usePlaintext()
                .build()

        blockingStub = ChatServiceGrpc.newBlockingStub(channel)
    }

    fun sendMessage(body: String): Boolean {
        LOGGER.info("sending message with body {}", body)
        val message = Chat.ChatMessage
                .newBuilder()
                .setFrom(name)
                .setMessage(body)
                .build()

        try {
           blockingStub.chat(message)
        } catch (e: StatusRuntimeException) {
            LOGGER.debug("StatusRuntimeException", e)
            return false
        }

        return true
    }

    fun shutdown() {
        server.shutdown()
    }
}

class MessengerService(val messageProcessor: (Chat.ChatMessage) -> Unit) : ChatServiceGrpc.ChatServiceImplBase() {
    override fun chat(
            request: Chat.ChatMessage,
            responseObserver: StreamObserver<Chat.Empty>
    ) {
        messageProcessor(request)

        responseObserver.onNext(Chat.Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}

fun main(args: Array<String>) {
    if (args.size != 4) {
        println("Usage: port name peer-port peer-address")
        return
    }

    val port = args[0].toInt()
    val name = args[1]
    val peerPort = args[2].toInt()
    val peerAddress = args[3]

    val defaultMessageProcessor: (Chat.ChatMessage) -> Unit  = { request ->
        val secs = request.timestamp.seconds
        val date = Date(secs * MILLIS_IN_SECONDS)

        println("<$date> [${request.from}]: ${request.message}")
    }

    val messenger = Messenger(name, peerAddress, port, peerPort, defaultMessageProcessor)

    while (true) {
        val body = readLine() ?: break
        if (!messenger.sendMessage(body)) {
            println("sendMessage failed()")
        }
    }
}