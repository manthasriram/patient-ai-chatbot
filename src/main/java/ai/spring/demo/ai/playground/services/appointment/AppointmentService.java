package ai.spring.demo.ai.playground.services.appointment;

import ai.spring.demo.ai.playground.data.appointment.Appointment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private List<Appointment> appointments = new ArrayList<>();

    public List<Appointment> getAppointments() {
        appointments.add(Appointment.builder().firstName("Sriram").lastName("Kumar").context("1")
                .date(LocalDate.parse("2024-11-11"))
                        .address("123, Main Street, New York, USA")
                .appointmentStatus(Appointment.APPOINTMENT_STATUS.BOOKED)
                        .appointmentNumber("1")
                        .context("1")
                        .provider("Dr. Smith")
                .billingStatus(Appointment.BILLING_STATUS.UNPAID).build());

        appointments.add(Appointment.builder().firstName("John").lastName("Doe").context("1")
                .date(LocalDate.parse("2024-11-24"))
                        .address("123, Main Street, New York, USA")
                        .appointmentNumber("2")
                        .provider("Dr. Smith")
                        .context("1")
                .appointmentStatus(Appointment.APPOINTMENT_STATUS.CHECK_IN)
                .billingStatus(Appointment.BILLING_STATUS.PAID).build());

        appointments.add(Appointment.builder().firstName("Tom").lastName("Cruise").context("1")
                .date(LocalDate.parse("2024-11-24"))
                        .appointmentNumber("3")
                        .context("1")
                        .provider("Dr Bruce")
                        .address("123, Main Street, New York, USA")
                .appointmentStatus(Appointment.APPOINTMENT_STATUS.CANCELLED)
                .billingStatus(Appointment.BILLING_STATUS.UNPAID).build());

        appointments.add(Appointment.builder().firstName("Brad").lastName("Pitt").context("1")
                .date(LocalDate.parse("2024-11-24"))
                        .context("1")
                        .provider("Dr Gupta")
                        .appointmentNumber("4")
                        .address("123, Main Street, New York, USA")
                .appointmentStatus(Appointment.APPOINTMENT_STATUS.CHECK_OUT)
                .billingStatus(Appointment.BILLING_STATUS.UNPAID).build());
        return  appointments;
    }

    public Optional<Appointment> findAppointment(String firstName, String lastName) {
        return appointments.stream().filter(a ->
                a.getFirstName().equalsIgnoreCase(firstName)
                        && a.getLastName().equalsIgnoreCase(lastName)).findFirst();
    }

    public Optional<Appointment> ScheduleAppointment(String firstName, String lastName, String date, String provider) {
        return Optional.empty();
    }

    public Optional<Appointment> CancelAppointment(String firstName, String lastName, String date) {
        return Optional.empty();
    }

    public Optional<Appointment> ChangeAppointment(String firstName, String lastName,
                                                   String date, String provider, String from, String to) {
        return Optional.empty();
    }

    public Optional<String> getBillingInfo(String firstName, String lastName ) {
        return Optional.empty();
    }

    public Optional<String> doesPatientHaveBills(String firstName, String lastName ) {
        return Optional.empty();
    }

}
