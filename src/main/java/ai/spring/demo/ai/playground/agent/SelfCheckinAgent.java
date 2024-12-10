package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.agent.configuration.SelfCheckinAgentConfiguration;
import ai.spring.demo.ai.playground.common.LoggingAdvisor;
import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class SelfCheckinAgent  extends AbstractAgent implements IAgentBot {


    public SelfCheckinAgent(ChatModel chatModel, VectorStore vectorStore, ChatMemory chatMemory) {
        super(chatModel, vectorStore, chatMemory, new SelfCheckinAgentConfiguration());
    }


    @Override
    public AgentResponse agentRequest(ChatMessage chatMessage) {
        ChatResponse chatResponse = chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .user(chatMessage.getMessage())
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatMessage.getChatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().chatResponse();

        String response =  chatResponse.getResults()
                .stream()
                .map(Generation::getOutput)
                .map(AssistantMessage::getContent)
                .collect(Collectors.joining());
        return new AgentResponse(response, new AgentMetaData("SelfCheckinAgent"));
    }
}
