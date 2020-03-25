package com.parkit.parkingsystem.integration;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.service.RecurringUserService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceIT {

    private static DataBaseTestConfig dataBaseTestConfig1 = new DataBaseTestConfig();
    private static DataBaseTestConfig dataBaseTestConfig2 = new DataBaseTestConfig();

    private static ParkingSpotDAO parkingSpotDAO1;
    private static ParkingSpotDAO parkingSpotDAO2;

    private static TicketDAO ticketDAO1;
    private static TicketDAO ticketDAO2;

    private static DataBasePrepareService dataBasePrepareService;

    private static RecurringUserService recurringUser1;
    private static RecurringUserService recurringUser2;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Mock
    private static InputReaderUtil inputReaderUtil1;
    @Mock
    private static InputReaderUtil inputReaderUtil2;


    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO1 = new ParkingSpotDAO();
        parkingSpotDAO2 = new ParkingSpotDAO();
        parkingSpotDAO1.dataBaseConfig = dataBaseTestConfig1;
        parkingSpotDAO2.dataBaseConfig = dataBaseTestConfig2;
        ticketDAO1 = new TicketDAO();
        ticketDAO2 = new TicketDAO();
        ticketDAO1.dataBaseConfig = dataBaseTestConfig1;
        ticketDAO2.dataBaseConfig = dataBaseTestConfig2;
        dataBasePrepareService = new DataBasePrepareService();
        recurringUser1 = new RecurringUserService();
        recurringUser2 = new RecurringUserService();
    }

    @BeforeEach
    private  void setUpEach(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testMultipleEntrancesTwoDifferentVehicles() throws Exception {
        when(inputReaderUtil1.readSelection()).thenReturn(1);
        when(inputReaderUtil2.readSelection()).thenReturn(1);

        when(inputReaderUtil1.readVehicleRegistrationNumber()).thenReturn("TEST1");
        when(inputReaderUtil2.readVehicleRegistrationNumber()).thenReturn("TEST2");

        ParkingService parkingServiceEntree1 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree1.setRecurringUSer(recurringUser1);
        parkingServiceEntree1.processIncomingVehicle();

        ParkingService parkingServiceEntree2 = new ParkingService(inputReaderUtil2, parkingSpotDAO2, ticketDAO2);
        parkingServiceEntree2.setRecurringUSer(recurringUser2);
        parkingServiceEntree2.processIncomingVehicle();

        Ticket ticketVerif1 = ticketDAO1.getTicket(inputReaderUtil1.readVehicleRegistrationNumber());

        Ticket ticketVerif2 = ticketDAO2.getTicket(inputReaderUtil2.readVehicleRegistrationNumber());

        //Test1
        // Verification que le ticket du véhicule est bien enregistré
        assertThat(ticketVerif1.getId()).isEqualTo(1);
        //Verification que la place de parking n'est plus disponible
        assertThat(parkingSpotDAO1.getNextAvailableSlot(ParkingType.CAR)).isGreaterThan(1);

        //Test2
        // Verification que le ticket du véhicule est bien enregistré
        assertThat(ticketVerif2.getId()).isEqualTo(2);
        //Verification que la place de parking n'est plus disponible
        assertThat(parkingSpotDAO2.getNextAvailableSlot(ParkingType.CAR)).isGreaterThan(1);


    }

    @Test
    public void testMultipleEntrancesSameVehicleInIn() throws Exception {
        when(inputReaderUtil1.readSelection()).thenReturn(1);
        when(inputReaderUtil1.readVehicleRegistrationNumber()).thenReturn("TEST1");

        ParkingService parkingServiceEntree1 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree1.setRecurringUSer(recurringUser1);
        parkingServiceEntree1.processIncomingVehicle();

        //Changing the println sysout
        System.setOut(new PrintStream(outContent));

        parkingServiceEntree1.processIncomingVehicle();

        assertTrue(outContent.toString().contains("Sorry, but your vehicle is already parked"));

    }

    @Test
    public void testMultipleEntrancesSameVehicleInOutIn() throws Exception {
        when(inputReaderUtil1.readSelection()).thenReturn(1);

        when(inputReaderUtil1.readVehicleRegistrationNumber()).thenReturn("TEST1");
        when(inputReaderUtil2.readVehicleRegistrationNumber()).thenReturn("TEST1");

        ParkingService parkingServiceEntree1 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree1.setRecurringUSer(recurringUser1);
        parkingServiceEntree1.processIncomingVehicle();

        //Changing the println sysout
        System.setOut(new PrintStream(outContent));
        Thread.sleep(2000);

        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil2, parkingSpotDAO2, ticketDAO2);
        parkingServiceSortie.setRecurringUSer(recurringUser2);
        parkingServiceSortie.processExitingVehicle();

        ParkingService parkingServiceEntree2 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree2.setRecurringUSer(recurringUser1);
        parkingServiceEntree2.processIncomingVehicle();

        assertTrue(outContent.toString().contains("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount."));

    }
    @Test
    public void testMultipleEntrancesSameBikeInOutIn() throws Exception {
        when(inputReaderUtil1.readSelection()).thenReturn(2);

        when(inputReaderUtil1.readVehicleRegistrationNumber()).thenReturn("BIKE1");
        when(inputReaderUtil2.readVehicleRegistrationNumber()).thenReturn("BIKE1");

        ParkingService parkingServiceEntree1 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree1.setRecurringUSer(recurringUser1);
        parkingServiceEntree1.processIncomingVehicle();

        //Changing the println sysout
        System.setOut(new PrintStream(outContent));
        Thread.sleep(2000);

        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil2, parkingSpotDAO2, ticketDAO2);
        parkingServiceSortie.setRecurringUSer(recurringUser2);
        parkingServiceSortie.processExitingVehicle();

        ParkingService parkingServiceEntree2 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree2.setRecurringUSer(recurringUser1);
        parkingServiceEntree2.processIncomingVehicle();

        assertTrue(outContent.toString().contains("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount."));

    }

    @Test
    public void testMultipleEntrancesSameVehicleInOutInOut() throws Exception {
        when(inputReaderUtil1.readSelection()).thenReturn(1);

        when(inputReaderUtil1.readVehicleRegistrationNumber()).thenReturn("TEST1");
        when(inputReaderUtil2.readVehicleRegistrationNumber()).thenReturn("TEST1");

        ParkingService parkingServiceEntree1 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree1.setRecurringUSer(recurringUser1);
        parkingServiceEntree1.processIncomingVehicle();

        Thread.sleep(2000);

        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil2, parkingSpotDAO2, ticketDAO2);
        parkingServiceSortie.setRecurringUSer(recurringUser2);
        parkingServiceSortie.processExitingVehicle();

        ParkingService parkingServiceEntree2 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceEntree2.setRecurringUSer(recurringUser1);
        parkingServiceEntree2.processIncomingVehicle();

        Thread.sleep(2000);

        ParkingService parkingServiceSortie2 = new ParkingService(inputReaderUtil1, parkingSpotDAO1, ticketDAO1);
        parkingServiceSortie.setRecurringUSer(recurringUser1);
        parkingServiceSortie.processExitingVehicle();

        Ticket ticketVerif1 = ticketDAO1.getTicket(inputReaderUtil1.readVehicleRegistrationNumber());

        //Test1
        // Verification que le ticket du véhicule est bien considéré comme sorti
        assertTrue(ticketVerif1.getOutTime() != null);




    }
}
