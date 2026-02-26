package daw.developer.atlas

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object TCPConnection {
    data class LogEventArgs(val mota: String, val log: String)

    var logSentEvent: ((LogEventArgs) -> Unit)? = null
    var connectedEvent: (() -> Unit)? = null
    var disconnectedEvent: (() -> Unit)? = null

    @Volatile
    var connected: Boolean = false
        private set

    private val _connectedState = MutableStateFlow(false)
    val connectedState = _connectedState.asStateFlow()

    private var socket: Socket? = null
    private var writer: BufferedWriter? = null
    private var reader: BufferedReader? = null
    private val notifiedConnected = AtomicBoolean(false)
    private val mainScope: CoroutineScope = MainScope()

    suspend fun connect(
        address: InetAddress,
        port: Int,
        username: String,
        password: String,
        isRegister: Boolean
    ) {
        withContext(Dispatchers.IO) {
            disconnect()
            try {
                socket = Socket(address, port)
                writer = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                sendAuth(username, password, isRegister)
                readLoop()
            } catch (e: Exception) {
                logSentEvent?.invoke(LogEventArgs("ERROR", e.message ?: "Error desconocido"))
                disconnect()
            }
        }
    }

    private fun sendAuth(username: String, password: String, isRegister: Boolean) {
        val command = if (isRegister) "REGISTER" else "LOGIN"
        sendCommand(command, username, password)
    }

    fun sendCommand(command: String, vararg args: String) {
        val payload = buildString {
            append(command)
            if (args.isNotEmpty()) {
                append(":")
                append(args.joinToString(":"))
            }
        }
        try {
            writer?.apply {
                write(payload)
                write("\n")
                flush()
            }
            logSentEvent?.invoke(LogEventArgs("SEND", payload))
        } catch (e: Exception) {
            logSentEvent?.invoke(LogEventArgs("ERROR", e.message ?: "Error al enviar"))
        }
    }

    private fun readLoop() {
        try {
            while (socket?.isConnected == true) {
                val received = reader?.readLine() ?: break
                logSentEvent?.invoke(LogEventArgs("RECV", received))

                val wasConnected = connected
                try {
                    CommandDecoder.executeCommand(received)
                } catch (_: Exception) {
                    // Errors already routed via CommandDecoder events.
                }

                if (!wasConnected && connected && notifiedConnected.compareAndSet(false, true)) {
                    mainScope.launch {
                        connectedEvent?.invoke()
                    }
                }
            }
        } catch (e: Exception) {
            logSentEvent?.invoke(LogEventArgs("ERROR", e.message ?: "Error de lectura"))
        } finally {
            disconnect()
        }
    }

    fun disconnect(suppressEvent: Boolean = false) {
        val wasConnected = connected
        try {
            reader?.close()
        } catch (_: Exception) {
        }
        try {
            writer?.close()
        } catch (_: Exception) {
        }
        try {
            socket?.close()
        } catch (_: Exception) {
        }
        reader = null
        writer = null
        socket = null
        if (connected) {
            setConnected(false)
        }
        notifiedConnected.set(false)
        if (!suppressEvent && wasConnected) {
            mainScope.launch {
                disconnectedEvent?.invoke()
            }
        }
    }

    private fun setConnected(value: Boolean) {
        connected = value
        _connectedState.value = value
    }
}
