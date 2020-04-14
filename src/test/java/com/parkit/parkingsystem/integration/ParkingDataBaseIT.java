package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.service.RecurringUserService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static RecurringUserService recurringUser;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO2;
    @Mock
    private static TicketDAO ticketDAO2;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO2 = new TicketDAO();
        ticketDAO2.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        recurringUser = new RecurringUserService();
        parkingSpotDAO2 = new ParkingSpotDAO();
    }

    @AfterAll
    private static void tearDown() {

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.setRecurringUSer(recurringUser);
        parkingService.processIncomingVehicle();
        //Done : TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        Ticket ticketVerif = new Ticket();
        ticketVerif = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());

        // Verification que le ticket du véhicule est bien enregistré
        assertThat(ticketVerif.getId()).isEqualTo(1);
        //Verification que la place de parking n'est plus disponible
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isGreaterThan(1);
    }

    @Test
    public void testParkingLotExit() throws Exception {

        ParkingService parkingServiceEntree = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingServiceEntree.setRecurringUSer(recurringUser);
        parkingServiceEntree.processIncomingVehicle();
        //Done : TODO: check that the fare generated and out time are populated correctly in the database

        Thread.sleep(2000);


        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingServiceSortie.setRecurringUSer(recurringUser);
        parkingServiceSortie.processExitingVehicle();
        Ticket ticketVerifSortie = new Ticket();
        ticketVerifSortie = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());

        //On verifie que l'on a toujours le ticket 1 enregistré
        assertThat(ticketVerifSortie.getId()).isEqualTo(1);
        // On verifie que la durée OutTime est bien insérée avec la bonne valeur
        assertThat(ticketVerifSortie.getOutTime().getTime() - ticketVerifSortie.getInTime().getTime()).isEqualTo(2000);
        // On vérifie que la place de parking correspondante est libre.
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
        //On vérifie que le montant est enregistré en base est bien celui voulu
        assertThat(ticketVerifSortie.getPrice()).isEqualTo(0);
    }

    @Test
    public void testParkingACarWithNoPlace() throws Exception {
        when(parkingSpotDAO2.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);
        System.setOut(new PrintStream(outContent));
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO2, ticketDAO);
        parkingService.processIncomingVehicle();

        assertTrue(outContent.toString().contains("Error fetching next available parking slot"));
    }

    @Test
    public void testParkingLotExitWithException() throws Exception {

        ParkingService parkingServiceEntree = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingServiceEntree.setRecurringUSer(recurringUser);
        parkingServiceEntree.processIncomingVehicle();

        Thread.sleep(2000);

        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO2);
        parkingServiceSortie.setRecurringUSer(recurringUser);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setInTime(new Date());
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO2.getTicket("ABCDEF")).thenReturn(ticket);
        when(ticketDAO2.updateTicket(any())).thenReturn(false);

        System.setOut(new PrintStream(outContent));
        parkingServiceSortie.processExitingVehicle();
        assertTrue(outContent.toString().contains("Unable to update ticket information. Error occurred"));


    }
}
