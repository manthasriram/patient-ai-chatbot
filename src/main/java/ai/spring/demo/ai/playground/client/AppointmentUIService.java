package ai.spring.demo.ai.playground.client;

import ai.spring.demo.ai.playground.data.appointment.Appointment;
import ai.spring.demo.ai.playground.services.appointment.AppointmentService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AppointmentUIService {

    private AppointmentService appointmentService;

    public AppointmentUIService( AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    public List<Appointment> getAppointments() {
        return appointmentService.getAppointments();
    }

}
