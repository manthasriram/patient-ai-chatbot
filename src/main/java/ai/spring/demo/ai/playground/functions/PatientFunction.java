package ai.spring.demo.ai.playground.functions;

import ai.spring.demo.ai.playground.domain.appointment.Appointment;
import ai.spring.demo.ai.playground.services.appointment.AppointmentService;
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

    public record SelfCheckinStatusRequest(String appointment) {};

    public record SelfCheckinStatusResponse(String status) {};

    public record ProviderAddressRequest(String context) {};

    public record ProviderAddressResponse(String address) {};

    public record  FindPatientRequest(String firstName, String lastName) {};

    public record  FindPatientResponse(String patientId) {};

    @Bean
    @Description("Get Patient details by first and last name")
    public Function<FindPatientRequest, FindPatientResponse> findPatient() {
        return request -> {
            try {
                log.info("Finding patient for {} {}", request.firstName(), request.lastName());
                return new FindPatientResponse("1234");
            }
            catch (Exception e) {
                log.error("Exception while finding patient: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new FindPatientResponse("");
            }
        };
    }

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
    @Description("Get the self check-in status for the patient for the appointment")
    public  Function<SelfCheckinStatusRequest, SelfCheckinStatusResponse>  getSelfCheckinStatus() {
        return request -> {
            try {
                SelfCheckinStatusResponse selfCheckinStatusResponse = new SelfCheckinStatusResponse("pending");
                return selfCheckinStatusResponse;
            } catch (Exception e) {
                return new SelfCheckinStatusResponse("");
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
