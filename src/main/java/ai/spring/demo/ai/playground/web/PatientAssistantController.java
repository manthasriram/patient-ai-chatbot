package ai.spring.demo.ai.playground.web;

import ai.spring.demo.ai.playground.agent.PatientAssistant;
import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientAssistantController {

    @Autowired
    private PatientAssistant nurseAssistant;

    @PostMapping("/ai/patient/chat")
    public AgentResponse nurseAssistant(@RequestBody @NotNull ChatMessage chatMessage) {
        return nurseAssistant.chat(chatMessage);
    }
}
