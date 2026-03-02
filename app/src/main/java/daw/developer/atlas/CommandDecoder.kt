package daw.developer.atlas

object CommandDecoder {
    // Komando zerrenda
    private val commands: Map<String, ICommand> = mapOf(
        "SUCCESS" to SuccessCommand(),
        "ERROR" to ErrorCommand(),
        "Data" to DataCommand()
    )

    // Komandoa ez dela existzen salbuespena
    class UnexistingCommandException(message: String) : Exception(message)

    // Komandoaren formatu okerra salbuespena
    class WrongCommandFormatException(message: String) : Exception(message)

    // Ukatua salbuespena
    class DeniedException(message: String) : Exception(message)

    // Komandoa prozesatu eta exekutatu
    fun executeCommand(command: String?) {
        command?.let { cmd ->
            // Komandoa lortu
            val splitCommand = cmd.trim().split(':')
            val rawName = splitCommand[0].trim()
            val commandName = if (rawName.isNotEmpty() && !rawName[0].isLetterOrDigit()) {
                rawName.substring(1)
            } else {
                rawName
            }

            // Komandoaren argumentuak lortu
            val args = splitCommand.drop(1).toTypedArray()

            try {
                // Komandoa existitzen den egiaztatu
                val commandExe = commands[commandName]
                    ?: throw UnexistingCommandException("'$commandName' komandoa ez da existitzen")

                // Komandoaren formatu egokia egiaztatu
                val splitFormat = commandExe.format.split(':')
                val isValidFormat = if (splitFormat.last() == "..." && args.size >= splitFormat.size - 1) true
                else args.size == splitFormat.size - 1

                if (!isValidFormat) throw WrongCommandFormatException("'$commandName' formatu okerra: ${commandExe.format}")

                // Komandoa exekutatu
                commandExe.execute(args)
            } catch (e: Exception) {
                when (e) {
                    is UnexistingCommandException,
                    is WrongCommandFormatException,
                    is DeniedException -> throw e
                    else -> throw e
                }
            }
        }
    }

    private interface ICommand {
        val format: String
        fun execute(args: Array<String>)
    }

    // Arrakasta gertaera
    class SuccessEventArgs(val count: Int?, val payload: String?, val context: String?)
    var successEvent: ((SuccessEventArgs) -> Unit)? = null

    // Saioa ondo hasita komandoa
    private class SuccessCommand : ICommand {
        override val format: String = "SUCCESS:..."

        override fun execute(args: Array<String>) {
            TCPConnection.connected = true

            val count = args.getOrNull(0)?.toIntOrNull()
            val payload = if (args.size >= 2) args.drop(1).joinToString(":") else null
            val context = if (count == null) args.getOrNull(0) else null

            successEvent?.invoke(SuccessEventArgs(count, payload, context))
        }
    }

    // Ukatuta komandoa eta gertaera
    class DeniedEventArgs(val reason: String)
    var deniedEvent: ((DeniedEventArgs) -> Unit)? = null

    private class ErrorCommand : ICommand {
        override val format: String = "ERROR:<message>"

        override fun execute(args: Array<String>) {
            val reason = args[0]
            deniedEvent?.invoke(DeniedEventArgs(reason))
            throw DeniedException(reason)
        }
    }

    // Datu berriko gertaera
    class DataEventArgs(val mota: DataType, val data: Array<String>)
    var dataEvent: ((DataEventArgs) -> Unit)? = null

    // Datu motak
    enum class DataType {
        TRIP
    }

    // Datu berriko komandoa
    private class DataCommand : ICommand {
        override val format: String = "Data <mota> ..."

        override fun execute(args: Array<String>) {
            try {
                val mota = DataType.valueOf(args[0])
                val data = args.drop(1).toTypedArray()
                dataEvent?.invoke(DataEventArgs(mota, data))
            } catch (e: Exception) {
                throw WrongCommandFormatException("${args[0]} mota ez da existitzen")
            }
        }
    }
}
