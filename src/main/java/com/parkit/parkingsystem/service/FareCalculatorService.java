package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private RecurringUserService recurringUser;

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        //Done TODO: Some tests are failing here. Need to check if this logic is correct


        double durationInMilliseconds = ticket.getOutTime().getTime() - ticket.getInTime().getTime();

        // Taking into account the 30-mn free parking
        durationInMilliseconds = durationInMilliseconds - Fare.FREE_TIME;
        if (durationInMilliseconds < 0) {
            durationInMilliseconds = 0;
        }


        double duration = Math.round((durationInMilliseconds / Fare.MILLISECONDS_BY_HOUR) * 100.00) / 100.00;

        //RecurringUserService recurringUser = new RecurringUserService();
        if (recurringUser.isRecurringUser(ticket.getVehicleRegNumber())) {
            duration = duration * (1 - Fare.RECURRING_REDUCTION);
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public void setRecurringUser(RecurringUserService recurringUser) {
        this.recurringUser = recurringUser;
    }

}