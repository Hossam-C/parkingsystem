package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //Done : TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        Ticket ticketVerif;
        ticketVerif = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());

        // Verification que le ticket du véhicule est bien enregistré
        assertThat(ticketVerif.getId()).isEqualTo(1);
        //Verification que la place de parking n'est plus disponible
        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isGreaterThan(1);
    }

    @Test
    public void testParkingLotExit() throws Exception {

        ParkingService parkingServiceEntree = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingServiceEntree.processIncomingVehicle();
        //Done : TODO: check that the fare generated and out time are populated correctly in the database

        Thread.sleep(2000);


        ParkingService parkingServiceSortie = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
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

}
