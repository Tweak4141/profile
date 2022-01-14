package bot.commands


import discord4j.core.event.domain.interaction.SlashCommandEvent
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.ApplicationCommandOptionType
import kotlinx.coroutines.reactor.awaitSingle
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import utils.getOrNull
import java.nio.channels.UnresolvedAddressException


/**
 * Command to eval Nashorn code.
 */
object Eval : Command {
    override val name: String = "eval"
    override fun commandRequest(): ApplicationCommandRequest =
        ApplicationCommandRequest.builder()
            .name(name)
            .description("Evaluate Nashorn code.")
            .addOption(ApplicationCommandOptionData.builder()
                .name("code")
                .description("Code to evaluate.")
                .type(ApplicationCommandOptionType.STRING.value)
                .required(false)
                .build())
            .build()

    override suspend fun execute(event: SlashCommandEvent) {
        val userId = event.interaction.user.id.asLong()
        val ownerId = 440130952769830912
        val code_to_exec = event.getOption("code").getOrNull()?.value?.getOrNull()?.asString()
        val manager = ScriptEngineManager()
        val engine = manager.getEngineByExtension("kts")
        
        if (userId != ownerId) {
        event.replyEphemeral("You don't have access to this command.").awaitSingle()
        return;
        }
        // Validate if code exists
        if (code_to_exec != null) {
           val startTime = System.nanoTime()

        val result = try {
            println(code_to_exec)
            engine.eval(code_to_exec)
        } catch (e: ScriptException) {
            e
        }

        val endTime = System.nanoTime()
        val timeUsed = endTime - startTime

        val response = "Executed in ${timeUsed}ns"
        if (result is Exception) {
            result.printStackTrace()

            val cause = result.cause
            if (cause == null)
                event.replyEphemeral("$response with ${result.javaClass.simpleName}: ${result.message} on line ${result.stackTrace[0].lineNumber}")
            else
                event.replyEphemeral("$response with ${cause.javaClass.simpleName}: ${cause.message} on line ${cause.stackTrace[0].lineNumber}")
        } else if (result != null)
            event.replyEphemeral("$response , result = $result")
        } else {
            event.replyEphemeral("No code was provided, cancelling.").awaitSingle()
        }
    }
}
