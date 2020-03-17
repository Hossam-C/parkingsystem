package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.TicketDAO;

public class RecurringUserService {
    TicketDAO ticketDAOR = new TicketDAO();

    public boolean isRecurringUser(String vehicleRegNumber) {
        boolean recurring;
        int nbTicket = ticketDAOR.getNumberPaidTicket(vehicleRegNumber);

        if (nbTicket >= 1) {
            recurring = true;
        } else {
            recurring = false;
        }

        return recurring;
    }

    public void setTicketDAOR(TicketDAO ticketDAOR) {
        this.ticketDAOR = ticketDAOR;
    }
}
