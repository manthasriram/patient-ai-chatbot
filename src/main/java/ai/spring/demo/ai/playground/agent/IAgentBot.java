package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;

public interface IAgentBot {
    AgentResponse agentRequest(ChatMessage chatMessage);
}
