package com.cri.smart_home.controller

import com.cri.smart_home.param.ESP32Param
import com.cri.smart_home.service.device.IDeviceService
import com.cri.smart_home.service.esp32.IESPService
import com.cri.smart_home.spec.ESP32Spec
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import java.nio.charset.StandardCharsets.UTF_8
import java.time.LocalDateTime

@Component
class Esp32WebSocketHandler(
    private val esp32Service: IESPService,
    @Lazy private val deviceService: IDeviceService
) : BinaryWebSocketHandler() {

    private val sessions = mutableListOf<SessionModel>()
    private val AUTH_PREFIX = "auth:"
    private val BIND_PREFIX = "bind:"

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log("New ESP32 Connected - SessionId: ${session.id}")
        log("Active sessions count: ${sessions.size}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.removeIf { it.sessionId == session.id }
        log("Connection closed for SessionId: ${session.id}")
        log("Close status: ${status.code} - ${status.reason}")
        log("Remaining active sessions: ${sessions.size}")
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val receivedMessage = String(message.payload.array(), UTF_8)
        log("Received binary message from SessionId: ${session.id}")
        log("Message size: ${message.payload.remaining()} bytes")
        log("Received raw message: $receivedMessage")

        if (!receivedMessage.startsWith(AUTH_PREFIX) && sessions.none { it.sessionId == session.id }) {
            session.sendBinaryMessage("Not authenticated".toByteArray())
            session.close()
            log("Not authenticated message sent and session closed for SessionId: ${session.id}")
            return
        }

        when {
            receivedMessage.startsWith(AUTH_PREFIX) -> handleAuthMessage(session, receivedMessage)
            receivedMessage.startsWith(BIND_PREFIX) -> handleBindMessage(session, receivedMessage)
        }
    }

    private fun handleAuthMessage(session: WebSocketSession, message: String) {
        val token = message.substringAfter(AUTH_PREFIX).trim()
        log("Extracted token: ${token.take(20)}... (truncated)")

        val esp32 = esp32Service.find(ESP32Spec(ESP32Param()).apply {
            this.token = token
            deleted = false
            archived = false
        })

        sessions.find { it.token == token }?.apply {
            this.token = token
            this.espId = esp32.getId()
            this.sessionId = session.id
            this.session = session
        } ?: sessions.add(SessionModel(token, session.id, session, esp32.getId()))

        log("Session added - SessionId: ${session.id}")
        log("Total active sessions: ${sessions.size}")

        session.sendBinaryMessage("Successfully connected".toByteArray())
        log("Sent success response to SessionId: ${session.id}")
    }

    private fun handleBindMessage(session: WebSocketSession, message: String) {
        val parts = message.substringAfter(BIND_PREFIX).split(":")
        if (parts.size == 2) {
            val deviceId = parts[0].trim().toLongOrNull() ?: throw Exception("Device ID must not be null")
            val value = parts[1].trim()
            log("Bind message received - DeviceId: $deviceId, Value: $value")
            deviceService.setData(deviceId, value)
        } else {
            session.sendBinaryMessage("Invalid bind message format".toByteArray())
            log("Invalid bind message format received from SessionId: ${session.id}")
        }
    }

    fun sendBinaryMessageToEsp32(espId: Long, message: ByteArray): Boolean {
        val session = sessions.find { it.espId == espId }?.session ?: return false.also {
            log("No active session found for ESP ID: $espId")
        }
        session.sendBinaryMessage(message)
        log("Message sent successfully to ESP ID: $espId")
        log("Message size: ${message.size} bytes")
        return true
    }

    private fun WebSocketSession.sendBinaryMessage(message: ByteArray) {
        sendMessage(BinaryMessage(message))
    }

    private fun log(message: String) {
        println("[${LocalDateTime.now()}] $message")
    }

    data class SessionModel(
        var token: String,
        var sessionId: String,
        var session: WebSocketSession,
        var espId: Long
    )
}