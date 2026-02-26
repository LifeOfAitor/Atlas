package daw.developer.atlas

import kotlinx.coroutines.*
import java.io.*
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException

object TCPConnection {
    // Konexio datuak
    var ip: InetAddress = InetAddress.getByName("127.0.0.1")
        private set
    var port: Int = 5000
        private set

    // Bezero objektuak
    private var client: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    // Bezeroa funtzionatzen ari den
    @Volatile
    var alive: Boolean = false
        private set

    // Bezeroaren izena eta mota
    var izena: String? = null
        private set

    @Volatile
    var connected: Boolean = false

    // Konektatuta gertaera
    var connectedEvent: (() -> Unit)? = null

    // Deskonektatuta gertaera
    var disconnectedEvent: (() -> Unit)? = null

    // Log berria gertaera
    class LogSentEventArgs(val log: String, val mota: LogType)
    var logSentEvent: ((LogSentEventArgs) -> Unit)? = null

    // Mezu berria gertaera
    class MessageArrivedEventArgs(val mezua: String)
    var messageArrivedEvent: ((MessageArrivedEventArgs) -> Unit)? = null

    // Log motak
    enum class LogType {
        INFO,
        WARN,
        ERROR
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var connectionJob: Job? = null

    // Bezeroa zerbitzarira konektatu eta saioa hasten saiatu
    private fun connect(ip: InetAddress, port: Int) {
        try {
            // Itxi aurreko konexioa
            if (alive) closeClient("Konexio zaharra itxi da")

            newLog("Saiatzen $ip:$port -ra konektatzen...", LogType.INFO)
            println("DEBUG: Konexio saiakera: $ip:$port")

            // Sortu socket-a timeout-arekin
            client = Socket()
            client?.connect(java.net.InetSocketAddress(ip, port))

            alive = true
            this@TCPConnection.ip = ip
            this@TCPConnection.port = port

            newLog("Socket-a sortu da, stream-ak irekitzen...", LogType.INFO)

            reader = BufferedReader(InputStreamReader(client!!.getInputStream()))
            writer = PrintWriter(client!!.getOutputStream(), true)

            newLog("Stream-ak ireki dira, autentifikazioa bidaltzen...", LogType.INFO)

        } catch (e: Exception) {
            e.printStackTrace()
            closeClient("Errorea connect: ${e.message}")
        }
    }

    suspend fun login(ip: InetAddress, port: Int, izena: String, pasahitza: String) {
        withContext(Dispatchers.IO) {
            try
            {
                // Konektatu
                connect(ip, port)

                // Bidali eta jaso erantzuna (blokeoa)
                writer?.println("LOGIN:$izena:$pasahitza")
                writer?.flush()

                newLog("Autentifikazioa bidali da: LOGIN:$izena:$pasahitza", LogType.INFO)
                newLog("Zerbitzariaren erantzuna itxaroten...", LogType.INFO)

                waitForMessage()

                if (TCPConnection.connected) {
                    this@TCPConnection.izena = izena

                    // connectedEvent hilo nagusian deitu behar da
                    withContext(Dispatchers.Main) {
                        newLog("Zerbitzarira konektatuta: $izena", LogType.INFO)
                        connectedEvent?.invoke()
                    }

                    // Hasi mezuak jasotzen (hau ez da blokeatzailea)
                    startConnectionMonitoring()
                } else closeClient("Autentifikazioak huts egin du")

            } catch (e: Exception) {
                e.printStackTrace()
                closeClient("Errorea login: ${e.message}")
            }
        }
    }

    suspend fun signup(ip: InetAddress, port: Int, izena: String, email: String, pasahitza: String) {
        withContext(Dispatchers.IO) {
            try
            {
                // Konektatu
                connect(ip, port)

                // Bidali eta jaso erantzuna (blokeoa)
                writer?.println("SIGNUP:$izena:$email:$pasahitza")
                writer?.flush()

                newLog("Autentifikazioa bidali da: SIGNUP:$izena:$pasahitza", LogType.INFO)
                newLog("Zerbitzariaren erantzuna itxaroten...", LogType.INFO)

                waitForMessage()
                closeClient()

            } catch (e: Exception) {
                e.printStackTrace()
                closeClient("Errorea signup: ${e.message}")
            }
        }
    }

    private fun startConnectionMonitoring() {
        connectionJob?.cancel()

        connectionJob = scope.launch {
            val receiverJob = launch {
                while (alive && isActive) {
                    try {
                        waitForMessage()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        if (alive) {
                            newLog("Errorea mezua jasotzean: ${e.message ?: "Errore ezezaguna"}", LogType.ERROR)
                        }
                        delay(500)
                    }
                }
            }

            val checkerJob = launch {
                while (alive && isActive) {
                    delay(1000)
                    try {
                        client?.let { socket ->
                            if (socket.isClosed) {
                                if (alive) {
                                    newLog("Socket-a itxita dago", LogType.WARN)
                                    closeClient("Konexioa itxi da")
                                }
                            } else {
                                // Bidali ping mezua konexioa egiaztatzeko
                                send("PING")
                            }
                        }
                    } catch (e: Exception) {
                        if (alive) {
                            newLog("Konexioa galdu da: ${e.message ?: "Errore ezezaguna"}", LogType.WARN)
                            closeClient("Konexioa galdu da")
                        }
                    }
                }
            }

            joinAll(receiverJob, checkerJob)
        }
    }

    private suspend fun waitForMessage() {
        withContext(Dispatchers.IO) {
            try {
                val mezua = reader?.readLine()
                if (mezua != null) {
                    withContext(Dispatchers.Main) {
                        messageArrivedEvent?.invoke(MessageArrivedEventArgs(mezua))
                        CommandDecoder.executeCommand(mezua)
                    }
                }
            } catch (e: SocketException) {
                if (alive) throw e
            } catch (e: IOException) {
                if (alive) throw e
            }
        }
    }

    fun send(mezua: String) {
        if (alive) {
            try {
                writer?.println(mezua)
                writer?.flush()
                if (mezua != "PING") { // Ez log-eatu PING mezuak
                    newLog("Bidalita: $mezua", LogType.INFO)
                }
            } catch (e: Exception) {
                newLog("Errorea mezua bidaltzean: ${e.message ?: "Errore ezezaguna"}", LogType.ERROR)
                closeClient("Konexioa galdu da")
            }
        } else {
            newLog("Ezin da mezua bidali: konexiorik ez", LogType.WARN)
        }
    }

    fun closeClient(log: String? = null) {
        if (!alive) return

        alive = false
        connected = false

        connectionJob?.cancel()
        connectionJob = null

        try {
            reader?.close()
            writer?.close()
            client?.close()
        } catch (e: Exception) {
            // Ignoratu
        }

        reader = null
        writer = null
        client = null
        izena = null

        disconnectedEvent?.invoke()
        if (log != null) newLog(log, LogType.ERROR)
    }

    fun newLog(log: String, mota: LogType) {
        println("LOG [$mota]: $log") // Hau beti ikusiko da Logcat-en
        logSentEvent?.invoke(LogSentEventArgs(log, mota))
    }

    fun cleanup() {
        closeClient()
        scope.coroutineContext.cancelChildren()
    }
}