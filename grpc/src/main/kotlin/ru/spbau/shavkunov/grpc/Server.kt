package ru.spbau.shavkunov.grpc

import com.google.protobuf.Timestamp
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.time.Instant



private val LOGGER = LoggerFactory.getLogger("ChatServiceImpl")

internal class ChatServiceImpl(val shutdownCallback: () -> Unit) : ChatServiceGrpc.ChatServiceImplBase() {
    override fun chat(responseObserver: StreamObserver<Chat.ChatMessageFromServer>): StreamObserver<Chat.ChatMessage> {
        observers.add(responseObserver)

        return object : StreamObserver<Chat.ChatMessage> {
            override fun onNext(value: Chat.ChatMessage) {
                LOGGER.info("Received from client {} message: {}", value.from, value)

                val time = Instant.now()
                val timestamp = Timestamp
                        .newBuilder()
                        .setSeconds(time.epochSecond)
                        .setNanos(time.nano)
                        .build()

                val message = Chat.ChatMessageFromServer.newBuilder()
                        .setMessage(value)
                        .setTimestamp(timestamp)
                        .build()

                for (observer in observers) {
                    observer.onNext(message)
                }
            }

            override fun onError(t: Throwable) {
                shutdownCallback()
            }

            override fun onCompleted() {
                observers.remove(responseObserver)
                shutdownCallback()
            }
        }
    }

    companion object {
        private val observers = ConcurrentHashMap.newKeySet<StreamObserver<Chat.ChatMessageFromServer>>()
    }
}

private val SERVER_LOGGER = LoggerFactory.getLogger("Server")

class Server(port: Int) {
    private val grpcServer = ServerBuilder
            .forPort(port)
            .addService(ChatServiceImpl(shutdownCallback()))
            .build()

    fun start() {
        SERVER_LOGGER.info("Server starting...")
        grpcServer.start()
        println("Server ready for connections")
        grpcServer.awaitTermination()
    }

    fun shutdown() {
        shutdownCallback()()
    }

    private fun shutdownCallback(): () -> Unit {
        return {
            SERVER_LOGGER.info("Shutdown the server")
            grpcServer.shutdownNow()
        }
    }
}

fun main(args: Array<String>) {
    val port = Integer.parseInt(args[0])
    Server(port).start()
}