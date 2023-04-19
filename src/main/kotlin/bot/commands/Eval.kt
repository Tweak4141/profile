package bot.commands


import discord4j.core.event.domain.interaction.SlashCommandEvent
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.ApplicationCommandOptionType
import kotlinx.coroutines.reactor.awaitSingle
import utils.getOrNull
import java.nio.channels.UnresolvedAddressException
import javax.script.*

/**
 * Command to eval Nashorn code.
 */
object Eval : Command { 
    override val name: String = "eval"
    override fun commandRequest(): ApplicationCommandRequest =
        ApplicationCommandRequest.builder()
            .name(name)
            .description("Evaluate code.")
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
        val engine = manager.getEngineByExtension("kts")!!
        
        if (userId != ownerId) {
        event.replyEphemeral("You don't have access to this command.").awaitSingle()
        return;
        }
        if (code_to_exec == null) {
            event.replyEphemeral("No code was provided, cancelling.").awaitSingle()
        }
        // Validate if code exists
        if (code_to_exec != null) {
           val startTime = System.nanoTime()

        val result = try {
            println(code_to_exec)
            engine.eval(code_to_exec)
            
        } catch (e: Exception) {
            event.replyEphemeral("Error: $e").awaitSingle()
        }
          val endTime = System.nanoTime()
        val timeUsed = endTime - startTime

        val response = "Executed in ${timeUsed}ns"
        if (result is ScriptException) {
            result.printStackTrace()

            val cause = result.cause
            if (cause == null) {
                event.replyEphemeral("$response with ${result.javaClass.simpleName}: ${result.message} on line ${result.stackTrace[0].lineNumber}").awaitSingle()
            } else {
                event.replyEphemeral("$response with ${cause.javaClass.simpleName}: ${cause.message} on line ${cause.stackTrace[0].lineNumber}").awaitSingle()
            }; 
        } else if (result != null) {
            event.replyEphemeral("$response , result = $result").awaitSingle()
        } else {
            event.replyEphemeral("$response , result = $result").awaitSingle()
        }
      }
    }
}

