package bot.commands


import discord4j.core.event.domain.interaction.SlashCommandEvent
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.ApplicationCommandOptionType
import kotlinx.coroutines.reactor.awaitSingle
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
        if (userId != ownerId) return event.replyEphemeral("You don't have access to this commmand.").awaitSingle()
        // Validate if code exists
        if (code_to_exec != null) {
            try {
                ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
                se.put("event", event);
                event.replyEphemeral(" Evaluated Successfully:\n```\n"+se.eval(code_to_exec)+" ```").awaitSingle()
                } catch(Exception e) {
                event.replyEphemeral(" An exception was thrown:\n```\n"+e+" ```").awaitSingle()
                }
        } else {
            event.replyEphemeral("No code was provided, cancelling.").awaitSingle()
        }
    }
}
