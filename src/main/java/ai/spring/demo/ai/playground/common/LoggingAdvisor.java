package ai.spring.demo.ai.playground.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;

import reactor.core.publisher.Flux;

@Slf4j
public class LoggingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	@Override
	public String getName() {
		return "LoggingAdvisor";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
		log.debug("\nRequest: {}", advisedRequest);
		AdvisedResponse response = chain.nextAroundCall(advisedRequest);
		log.debug("\nResponse: {} ", response);
		return response;

	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
		log.debug("\nRequest: ", advisedRequest);
		Flux<AdvisedResponse> responses = chain.nextAroundStream(advisedRequest);
		return new MessageAggregator().aggregateAdvisedResponse(responses, aggregatedAdvisedResponse -> {
			log.info("\nResponse: " , aggregatedAdvisedResponse);
		});
	}

}