package ai.spring.demo.ai.playground.agent.configuration;

public interface AgentConfiguration {

    String getAgentName();
    String promptTemplate();
    String[] getDefaultFunctions();
}
