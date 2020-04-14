package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TicketModelTest {

    private static Ticket ticket = new Ticket();
    private static ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

    @Test
    public void testSetterGetterId() {

        ticket.setId(1);

        assertThat(ticket.getId()).isEqualTo(1);
    }

    @Test
    public void testSetterGetterParkingSpot() {

        ticket.setParkingSpot(parkingSpot);

        assertThat(ticket.getParkingSpot()).isEqualTo(parkingSpot);
    }

    @Test
    public void testSetterGetterVehicleRegNumber() {

        ticket.setVehicleRegNumber("ABCDEF");

        assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");
    }

    @Test
    public void testSetterGetterPrice() {

        ticket.setPrice(15);

        assertThat(ticket.getPrice()).isEqualTo(15);
    }

    @Test
    public void testSetterGetterInTime() {

        Date inTime = new Date();

        ticket.setInTime(inTime);

        assertThat(ticket.getInTime()).isEqualTo(inTime);
    }

    @Test
    public void testSetterGetterOutTime() {

        Date outTime = new Date();

        ticket.setInTime(outTime);

        assertThat(ticket.getInTime()).isEqualTo(outTime);
    }

    @Test
    public void testSetterGetterNumberOfPaidTicket() {

        ticket.setNumberOfPaidTicket(10);

        assertThat(ticket.getNumberOfPaidTicket()).isEqualTo(10);
    }
}
