package ai.spring.demo.ai.playground.client;

import ai.spring.demo.ai.playground.controller.AgentResponse;
import ai.spring.demo.ai.playground.controller.ChatMessage;
import ai.spring.demo.ai.playground.domain.appointment.Appointment;
import ai.spring.demo.ai.playground.agent.PatientAssistant;
import ai.spring.demo.ai.playground.services.appointment.AppointmentService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AppointmentUIService {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientAssistant patientAssistant;

    public AppointmentUIService( AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    public List<Appointment> getAppointments() {
        return appointmentService.getAppointments();
    }

    public Flux<AgentResponse> chat(String chatId, String message) {
        return   Flux.just(patientAssistant.chat(new ChatMessage(message, chatId, null)));
    }
}
