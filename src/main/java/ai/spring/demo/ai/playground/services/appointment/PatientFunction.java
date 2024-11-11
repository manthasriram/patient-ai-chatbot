package ai.spring.demo.ai.playground.services.appointment;

import ai.spring.demo.ai.playground.data.appointment.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class PatientFunction {

    @Autowired
    private AppointmentService appointmentService;


    public record AppointmentRequest(String firstName, String lastName) {};

    public record BillPayRequest(String firstName, String lastName) {};

    public record PatientHasOutstandingBillsRequest(String firstName, String lastName) {};

    public record PatientHasOutstandingBillsResponse(String hasBills) {};

    public record BillPayResponse(String billPayLink) {};

    public record SelfCheckinLinkRequest(String appointment) {};

    public record SelfCheckinLink(String url) {};

    public record ProviderAddressRequest(String context) {};

    public record ProviderAddressResponse(String address) {};

    @Bean
    @Description("Get Patient appointment details by first and last name")
    public Function<AppointmentRequest, Appointment> getAppointment() {
        return request -> {
            try {
                log.info("Finding appointment for {} {}", request.firstName(), request.lastName());
                Optional<Appointment> appointment = appointmentService.findAppointment(request.firstName(), request.lastName());
                log.info("Found appointment: {}", appointment.orElse(new Appointment()));
                return appointment.orElse(new Appointment());
            }
            catch (Exception e) {
                log.error("Exception while finding appointment: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new Appointment(request.firstName(), request.lastName(),
                        null, null, null, null, null, null, null);
            }
        };
    }


    @Bean
    @Description("Get bill pay link for a patient")
    public Function<BillPayRequest, BillPayResponse> getBillPayLink(  ) {
        return request -> {
            try {
                BillPayResponse billPayResponse = new BillPayResponse("https://mybillpaylink.com/pay/appointment/1234");
                return billPayResponse;
            }
            catch (Exception e) {
               return new BillPayResponse("");
            }
        };
    }

    @Bean
    @Description("Checks if the patient has outstanding bills")
    public Function<PatientHasOutstandingBillsRequest   , PatientHasOutstandingBillsResponse> doesPatientHaveOutstandingBills(  ) {
        return request -> {
            try {
               return new  PatientHasOutstandingBillsResponse("true");
            }
            catch (Exception e) {
                return new PatientHasOutstandingBillsResponse("false");
            }
        };
    }

    @Bean
    @Description("Get the self check-in link for the patient for the appointment")
    public Function<SelfCheckinLinkRequest, SelfCheckinLink> getSelfCheckinLink( ) {
        return request -> {
            try {
                SelfCheckinLink billPayResponse = new SelfCheckinLink("https://mybillpaylink.com/pay/appointment/1234");
                return billPayResponse;
            }
            catch (Exception e) {
                return new SelfCheckinLink("");
            }
        };
    }


    @Bean
    @Description("Get the self check-in link for the patient for the appointment")
    public Function<ProviderAddressRequest, ProviderAddressResponse> getProviderAddress() {
        return request -> {
            try {
                ProviderAddressResponse providerAddressResponse = new ProviderAddressResponse("1234 Ceaser Chavez, San Francisco, CA");
                return providerAddressResponse;
            }
            catch (Exception e) {
                return new ProviderAddressResponse("");
            }
        };
    }
}
