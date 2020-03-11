package com.parkit.parkingsystem;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.dao.TicketDAO;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.RecurringUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecurringUserServiceTest {

    private  static RecurringUserService recurringUser;
    //private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    //private Ticket ticket;
    //private TicketDAO ticketDAOR;

    @Mock
    private static TicketDAO ticketDAOR;



    //@BeforeAll
    //private  void setUpUser() {
    //    ticketDAOR.dataBaseConfig = dataBaseTestConfig;
    //      }


    //@BeforeEach
    //private void setUpPerTest() {
        //ticket = new Ticket();
    //    ticketDAOR = new TicketDAO();
    //}

    @Test
    public void testRecurrentUser () {
        //GIVEN
        System.out.println("debut test");
        when(ticketDAOR.getNumberPaidTicket(anyString())).thenReturn(1);
        //WHEN
        recurringUser = new RecurringUserService();
        recurringUser.setTicketDAOR(ticketDAOR);
        //recurringUser.isRecurringUser("ABCDE");
        //THEN
        assertThat(recurringUser.isRecurringUser("ABCDE")).isEqualTo(true);
    }

    @Test
    public void testNonRecurrentUser () {
        //GIVEN
        System.out.println("debut test");
        when(ticketDAOR.getNumberPaidTicket(anyString())).thenReturn(0);
        //WHEN
        recurringUser = new RecurringUserService();
        recurringUser.setTicketDAOR(ticketDAOR);
        //recurringUser.isRecurringUser("ABCDE");
        //THEN
        assertThat(recurringUser.isRecurringUser("ABCDE")).isEqualTo(false);
    }


}
