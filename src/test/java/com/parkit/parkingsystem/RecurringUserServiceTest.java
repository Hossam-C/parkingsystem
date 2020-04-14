package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.RecurringUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecurringUserServiceTest {

    private static RecurringUserService recurringUser;

    @Mock
    private static TicketDAO ticketDAOR;
    @Mock
    private static Ticket ticket;

    @Test
    public void testRecurrentUser() {
        //GIVEN
        System.out.println("debut test");
        when(ticketDAOR.getNumberPaidTicket(anyString())).thenReturn(1);

        //WHEN
        recurringUser = new RecurringUserService();
        recurringUser.setTicketDAOR(ticketDAOR);

        //THEN
        assertThat(recurringUser.isRecurringUser("ABCDE")).isEqualTo(true);
    }

    @Test
    public void testNonRecurrentUser() {
        //GIVEN
        when(ticketDAOR.getNumberPaidTicket(anyString())).thenReturn(0);
        //WHEN
        recurringUser = new RecurringUserService();
        recurringUser.setTicketDAOR(ticketDAOR);
        //THEN
        assertThat(recurringUser.isRecurringUser("ABCDE")).isEqualTo(false);
    }


}
