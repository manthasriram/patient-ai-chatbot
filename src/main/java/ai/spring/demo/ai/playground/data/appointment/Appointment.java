package ai.spring.demo.ai.playground.data.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {


    private String firstName;
    private String lastName;
    private LocalDate date;
    private String provider;
    private String address;
    private String context;
    private String appointmentNumber;

    public enum BILLING_STATUS {
        UNPAID,
        PAID
    }

    private BILLING_STATUS billingStatus;

    public enum APPOINTMENT_STATUS {
        BOOKED,
        CANCELLED,
        CHECK_IN,
        CHECK_OUT
    }

    private APPOINTMENT_STATUS appointmentStatus;
}
