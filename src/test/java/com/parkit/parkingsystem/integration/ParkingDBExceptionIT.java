package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestExceptConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.RecurringUserService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ParkingDBExceptionIT {

    private static DataBaseTestExceptConfig dataBaseTestExceptConfig = new DataBaseTestExceptConfig();

    private static ParkingSpotDAO parkingSpotDAO;

    private static TicketDAO ticketDAO;

    private static ParkingSpot parkingSpot;

    private static Ticket ticket;

    private static RecurringUserService recurringUser;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestExceptConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestExceptConfig;


        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        ticket = new Ticket();

        recurringUser = new RecurringUserService();
    }


    @Test
    public void getNextAvailableSlotExceptionTest() {
        assertThrows(SQLException.class, () -> {
            parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);});
    }


    @Test
    public void updateParkingExceptionTest() {
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void saveTicketExceptionTest() {
        assertThrows(SQLException.class, () -> {ticketDAO.saveTicket(ticket);});
    }
}
