package ai.spring.demo.ai.playground.agent;

import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AgentRouter {

    private SelfCheckinAgent selfCheckinAgent;
    private IntentionAgent intentionAgent;

    public AgentRouter(SelfCheckinAgent selfCheckinAgent, IntentionAgent intentionAgent) {
        this.selfCheckinAgent = selfCheckinAgent;
        this.intentionAgent = intentionAgent;
    }

    public Optional<IAgentBot> findRountingAgent(ChatMessage chatMessage) {
        AgentResponse agentResponse = intentionAgent.agentRequest(chatMessage);

        log.info("Agent Response: {}", agentResponse.getMessage());

        if(StringUtils.equals( agentResponse.getMessage(), "self-checkin")){
            return Optional.of(selfCheckinAgent);
        }
        else {
            return Optional.empty();
        }
    }
}
