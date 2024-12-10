package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.agent.configuration.IntentionAgentConfiguration;
import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class IntentionAgent extends AbstractAgent implements IAgentBot {

    public IntentionAgent(ChatModel chatModel) {
        super(chatModel, null, null, new IntentionAgentConfiguration());
    }

    @Override
    public AgentResponse agentRequest(ChatMessage chatMessage) {
        ChatResponse chatResponse = chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .user(chatMessage.getMessage())
                .call().chatResponse();

        String response =  chatResponse.getResults()
                .stream()
                .map(Generation::getOutput)
                .map(AssistantMessage::getContent)
                .collect(Collectors.joining());
        return new AgentResponse(response, new AgentMetaData("IntentionAgent"));
    }
}
