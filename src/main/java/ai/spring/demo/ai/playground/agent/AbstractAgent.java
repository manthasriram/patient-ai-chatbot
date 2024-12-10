package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.agent.configuration.AgentConfiguration;
import ai.spring.demo.ai.playground.common.LoggingAdvisor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

@Getter
public class AbstractAgent {

    protected final ChatModel chatModel;
    protected final VectorStore vectorStore;
    protected final ChatMemory chatMemory;
    protected ChatClient chatClient;
    private final AgentConfiguration agentConfiguration;

    public AbstractAgent(ChatModel chatModel, VectorStore vectorStore, ChatMemory chatMemory, AgentConfiguration agentConfiguration) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
        this.agentConfiguration = agentConfiguration;
        configureChatClient();
    }

    protected ChatClient configureChatClient() {
        ChatClient.Builder builder = ChatClient.builder(this.chatModel);
        builder
                .defaultSystem(agentConfiguration.promptTemplate())
                .defaultAdvisors(
                        new LoggingAdvisor());
        if(vectorStore != null) {
            builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()));
        }
        if(chatMemory != null) {
            builder.defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory));
        }
        if (ArrayUtils.isNotEmpty(agentConfiguration.getDefaultFunctions())) {
            builder.defaultFunctions(agentConfiguration.getDefaultFunctions());
        }
        chatClient = builder.build();
        return chatClient;
    }
}
