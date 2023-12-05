package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean discount) {

	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	long inHour = ticket.getInTime().getTime();
	long outHour = ticket.getOutTime().getTime();

	double duration = ((double) (outHour - inHour)) / 3600000;

	double discountRate = discount ? 0.95 : 1.0;

	if (duration < 0.5) {
	    ticket.setPrice(0.0);

	} else {

	    switch (ticket.getParkingSpot().getParkingType()) {
	    case CAR: {
		ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * discountRate);
		break;
	    }
	    case BIKE: {
		ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * discountRate);
		break;
	    }
	    default:
		throw new IllegalArgumentException("Unkown Parking Type");
	    }
	}
    }

    public void calculateFare(Ticket ticket) {
	calculateFare(ticket, false);
    }
}