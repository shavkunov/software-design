package ru.spbau.shavkunov.grpc

import com.google.protobuf.Timestamp
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.time.Instant


private val LOGGER = LoggerFactory.getLogger("ChatServiceImpl")

/**
 * Server observer handling
 * Shutdown callback shutdowns the underlying gRPC server
 */
internal class ChatServiceImpl(val shutdownCallback: () -> Unit) : ChatServiceGrpc.ChatServiceImplBase() {
    override fun chat(responseObserver: StreamObserver<Chat.ChatMessageFromServer>): StreamObserver<Chat.ChatMessage> {
        observers.add(responseObserver)

        /**
         * Handling messages from client
         */
        return object : StreamObserver<Chat.ChatMessage> {
            /**
             * Sending message with timestamp
             */
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

            /**
             * Shutdowns underlying server
             */
            override fun onError(t: Throwable) {
                shutdownCallback()
            }

            /**
             * Removes client
             */
            override fun onCompleted() {
                observers.remove(responseObserver)
            }
        }
    }

    companion object {
        private val observers = ConcurrentHashMap.newKeySet<StreamObserver<Chat.ChatMessageFromServer>>()
    }
}

private val SERVER_LOGGER = LoggerFactory.getLogger("Server")

/**
 * gRPC Server wrapper
 */
class Server(port: Int) {
    private val grpcServer = ServerBuilder
            .forPort(port)
            .addService(ChatServiceImpl(shutdownCallback()))
            .build()

    /**
     * Start the server
     */
    fun start() {
        SERVER_LOGGER.info("Server starting...")
        grpcServer.start()
        println("Server ready for connections")
        grpcServer.awaitTermination()
    }

    /**
     * Shutdown the server
     */
    fun shutdown() {
        shutdownCallback()()
    }

    /**
     * Shutdown callback, that need to be executed in implementation of chat service due to failure.
     */
    private fun shutdownCallback(): () -> Unit {
        return {
            SERVER_LOGGER.info("Shutdown the server")
            grpcServer.shutdownNow()
        }
    }
}

/**
 * CLI for server
 * Only arg is port number
 */
fun main(args: Array<String>) {
    val port = Integer.parseInt(args[0])
    Server(port).start()
}