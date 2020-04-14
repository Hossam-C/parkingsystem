package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.service.RecurringUserService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private static Ticket ticket;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static RecurringUserService recurringUser;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    private void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.setRecurringUSer(recurringUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest() {
        //GIVEN
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

        //WHEN
        parkingService.processExitingVehicle();

        //THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testDisplayMessageRecurringUser() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
        when(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber())).thenReturn(null);
        parkingService.setRecurringUSer(recurringUser);
        when(recurringUser.isRecurringUser(anyString())).thenReturn(true);
        System.setOut(new PrintStream(outContent));

        //WHEN
        parkingService.processIncomingVehicle();

        //THEN
        assertTrue(outContent.toString().contains("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount."));
    }

    @Test
    public void wrongTypeofVehicleTest() {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(3);

        //WHEN
        System.setOut(new PrintStream(outContent));
        parkingService.getNextParkingNumberIfAvailable();

        //THEN
        assertTrue(outContent.toString().contains("Incorrect input provided"));

    }
}
