package daw.developer.atlas

object CommandDecoder {
    private val commands: Map<String, ICommand> = mapOf(
        "SUCCESS" to SuccessCommand(),
        "ERROR" to ErrorCommand(),
        "Data" to DataCommand()
    )

    class UnexistingCommandException(message: String) : Exception(message)
    class WrongCommandFormatException(message: String) : Exception(message)
    class DeniedException(message: String) : Exception(message)

    fun executeCommand(command: String?) {
        command?.let { cmd ->
            val splitCommand = cmd.trim().split(':')
            val rawName = splitCommand[0].trim()
            val commandName = if (rawName.startsWith(":")) {
                rawName.removePrefix(":")
            } else {
                rawName
            }
            val args = splitCommand.drop(1).toTypedArray()

            try {
                val commandExe = commands[commandName]
                    ?: throw UnexistingCommandException("'$commandName' komandoa ez da existitzen")

                if (commandName != "SUCCESS") {
                    val splitFormat = commandExe.format.split(':')
                    val isValidFormat = if (splitFormat.last() == "..." && args.size >= splitFormat.size - 1) {
                        true
                    } else {
                        args.size == splitFormat.size - 1
                    }

                    if (!isValidFormat) {
                        throw WrongCommandFormatException("'$commandName' formatu okerra: ${commandExe.format}")
                    }
                }

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

    private class SuccessCommand : ICommand {
        override val format: String = "SUCCESS:<context>"

        override fun execute(args: Array<String>) {
            daw.developer.atlas.TCPConnection.connected = true
        }
    }

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

    class DataEventArgs(val mota: DataType, val data: Array<String>)
    var dataEvent: ((DataEventArgs) -> Unit)? = null

    enum class DataType {
        // TODO
    }

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
