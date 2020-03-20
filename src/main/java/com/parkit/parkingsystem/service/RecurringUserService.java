package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class RecurringUserService {
    TicketDAO ticketDAOR = new TicketDAO();
    //Ticket ticket = new Ticket();

    public boolean isRecurringUser(String vehicleRegNumber) {
        boolean recurring;
        int nbTicket = ticketDAOR.getNumberPaidTicket(vehicleRegNumber);
        //int nbTicket = ticket.getNumberOfPaidTicket();

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
