package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.TicketDAO;

public class RecurringUserService {
    TicketDAO ticketDAOR = new TicketDAO();
    //Ticket ticket = new Ticket();

    public boolean isRecurringUser(String vehicleRegNumber) {
        boolean recurring;
        int nbTicket = ticketDAOR.getNumberPaidTicket(vehicleRegNumber);
        //int nbTicket = ticket.getNumberOfPaidTicket();

        recurring = nbTicket >= 1;

        return recurring;
    }

    public void setTicketDAOR(TicketDAO ticketDAOR) {
        this.ticketDAOR = ticketDAOR;
    }
}
