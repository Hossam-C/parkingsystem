package com.parkit.parkingsystem;


import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.RecurringUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class RecurringUserServiceTest {

    private static RecurringUserService recurringUser;
    private Ticket ticket;

    @BeforeAll
    private static void setUpUser() { recurringUser = new RecurringUserService} ;


    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void testRecurrentUser () {
        //GIVEN

        //WHEN
        ticket.setNumberOfPaidTicket(1);
        //THEN
        assertThat(recurringUser("ABCDE").isRecurringUser()).isEqualTo(TRUE);
    }

    @Test
    public void testNonRecurrentUser () {
        //GIVEN

        //WHEN
        ticket.setNumberOfPaidTicket(0);
        //THEN
        assertThat(isRecurringUser("ABCDE")).isEqualTo(FALSE);
    }

}
