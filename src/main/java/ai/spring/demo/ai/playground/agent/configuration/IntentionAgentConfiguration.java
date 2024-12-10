package ai.spring.demo.ai.playground.agent.configuration;

public class IntentionAgentConfiguration implements AgentConfiguration{
    @Override
    public String getAgentName() {
        return "IntetionAgent";
    }

    @Override
    public String promptTemplate() {
        return """
                        You are a customer chat support agent of an Doctors office."
						You are interacting with patients through an online chat system.
						Your job is understand the patient's question and classify the intention of the question.
						Billing related questions should be classified as 'billing'.
                        Appointment related questions should be classified as 'appointment'.
                        Self check-in related questions should be classified as 'self-checkin'.
                        Any other questions should be classified as 'other'. Default to 'other'.
                        You should only respond with one of the following answers: 'billing', 'self-checkin' or 'appointment'.
						Today is {current_date}.
                """;
    }

    @Override
    public String[] getDefaultFunctions() {
        return null;
    }
}
