package ai.spring.demo.ai.playground.services;

import ai.spring.demo.ai.playground.controller.ChatMessage;
import jakarta.validation.constraints.NotNull;
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

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class PatientAssistant {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private ChatClient chatClient;

    public PatientAssistant(ChatModel chatModel, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
        configureAssistant();
    }

    public  void configureAssistant() {
        ChatClient.Builder builder = ChatClient.builder(this.chatModel);
        chatClient  = builder
                .defaultSystem("""
						You are a customer chat support agent of an Doctors office."
						Respond in a friendly, helpful manner.
						You are interacting with patients through an online chat system.
						Before providing information about an appointment, you MUST always
						get the following information from the patient:  customer first name and last name.
    					Check the message history for this information before asking the patient.

						Always check if the patient has an upcoming appointment.Use the provided function  to fetch the appointment.
						Additionally, check if the patient has any outstanding bills.Use the provided function  to fetch the bill.
						If the patient billing status is unpaid on, you should ask the patient if they want to pay the bill.
						If the patient agrees to pay give them the bill pay link.
						After completing the payment step. check if the patient the upcoming appointment  requires self check-in completed before the appointment. Get the link for self check-in by calling the getSelfCheckinLink function.
						After completing the self check-in step. you should ask the patient if they want the address of the provider location. Get the address by calling the getProviderAddress function.
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

                .defaultFunctions("getAppointment","getBillPayLink", "getSelfCheckinLink", "getProviderAddress","doesPatientHaveOutstandingBills") // FUNCTION CALLING
                .build();
    }

    public String chat(@RequestBody @NotNull ChatMessage chatMessage) {
        ChatResponse chatResponse = chatClient.prompt()
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .user(chatMessage.getMessage())
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatMessage.getChatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().chatResponse();

        return chatResponse.getResults()
                .stream()
                .map(Generation::getOutput)
                .map(AssistantMessage::getContent)
                .collect(Collectors.joining());
    }


}
