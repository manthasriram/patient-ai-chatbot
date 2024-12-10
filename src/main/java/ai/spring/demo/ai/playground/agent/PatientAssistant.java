package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import ai.spring.demo.ai.playground.common.LoggingAdvisor;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class PatientAssistant  implements IAgentBot{

    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private ChatClient chatClient;
    private AgentRouter agentRouter;



    public PatientAssistant(ChatModel chatModel, VectorStore vectorStore, ChatMemory chatMemory, AgentRouter agentRouter) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
        this.agentRouter = agentRouter;
        configureAssistant();
    }

    public  void configureAssistant() {
        ChatClient.Builder builder = ChatClient.builder(this.chatModel);
        chatClient  = builder
                .defaultSystem("""
						You are a customer chat support agent of an Doctors office."
						Respond in a friendly, helpful manner.
						Your role is to establish the identity of the patient.
						You are interacting with patients through an online chat system.
						You will help them with generic questions like office address, office hours and details about the provider.
						Before proceeding with any requests, you MUST always get the following information from the patient:  customer first name and last name.
						Look up  the patient in the patient database by calling fetch patient.
						Always look at the history of the conversation for this information before asking the patient.
						Anything else, should be handled by the appropriate agent.
						Use parallel function calling if required.
						Today is {current_date}.
					""")
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                        // new VectorStoreChatMemoryAdvisor(vectorStore)),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()), // RAG
                        // new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()
                        // 	.withFilterExpression("'documentType' == 'terms-of-service' && region in ['EU', 'US']")),
                        new LoggingAdvisor())

                .defaultFunctions("findPatient","getAppointment", "getProviderAddress") // FUNCTION CALLING
                .build();
    }

    public AgentResponse chat(@RequestBody @NotNull ChatMessage chatMessage) {

       Optional<IAgentBot> agentBot  = agentRouter.findRountingAgent(chatMessage);
       if(agentBot.isPresent()) {
           return agentBot.get().agentRequest(chatMessage);
       }
       else {

           log.info("Chat Message ID: {}", chatMessage.getChatId());

           ChatResponse chatResponse = chatClient.prompt()
                   .system(s -> s.param("current_date", LocalDate.now().toString()))
                   .user(chatMessage.getMessage())
                   .advisors(a -> a
                           .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatMessage.getChatId())
                           .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                   .call().chatResponse();

           log.info("Chat Response: {}", chatResponse);

           String response = chatResponse.getResults()
                   .stream()
                   .map(Generation::getOutput)
                   .map(m -> {
                               AssistantMessage assistantMessage = (AssistantMessage) m;
                               return assistantMessage.getContent();
                           }
                           )
                   .collect(Collectors.joining());
           return  AgentResponse.builder().message(response)
                   .agentMetaData(new AgentMetaData("PatientAssistant")).build();
       }
    }


    @Override
    public AgentResponse agentRequest(ChatMessage chatMessage) {
        return null;
    }
}
