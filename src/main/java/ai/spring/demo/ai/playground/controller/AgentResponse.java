package ai.spring.demo.ai.playground.controller;

import ai.spring.demo.ai.playground.agent.AgentMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AgentResponse {

    private String message;
    private AgentMetaData agentMetaData;
}
