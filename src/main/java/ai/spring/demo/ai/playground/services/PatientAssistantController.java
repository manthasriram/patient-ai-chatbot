package ai.spring.demo.ai.playground.services;

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
    public String nurseAssistant(@RequestBody @NotNull ChatMessage chatMessage) {
        return nurseAssistant.chat(chatMessage);
    }
}
