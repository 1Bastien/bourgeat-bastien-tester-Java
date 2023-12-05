package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @InjectMocks
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
	parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @Test
    public void processIncomingVehicleTest() throws Exception {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
	when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	when(ticketDAO.getNbTicket(eq("ABCDEF"))).thenReturn(0);

	parkingService.processIncomingVehicle();

	verify(parkingSpotDAO,Mockito.times(1)).updateParking(any(ParkingSpot.class));
	verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
	verify(ticketDAO, Mockito.times(1)).getNbTicket(eq("ABCDEF"));
    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

	ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

	assertNotNull(result);
	assertEquals(1, result.getId());
	assertEquals(ParkingType.CAR, result.getParkingType());
	assertTrue(result.isAvailable());
     }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(-1);

	assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
	when(inputReaderUtil.readSelection()).thenReturn(3);

	assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void processExitingVehicleTest() throws Exception {
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	Ticket ticket = new Ticket();
	ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");

	when(ticketDAO.getTicket(eq("ABCDEF"))).thenReturn(ticket);
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
	when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	when(ticketDAO.getNbTicket(eq("ABCDEF"))).thenReturn(0);

	parkingService.processExitingVehicle();

	verify(ticketDAO, Mockito.times(1)).getTicket(eq("ABCDEF"));
	verify(ticketDAO, Mockito.times(1)).getNbTicket(eq("ABCDEF"));
	verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleTestUnableUpdate() throws Exception {
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	Ticket ticket = new Ticket();
	ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");

	when(ticketDAO.getTicket(eq("ABCDEF"))).thenReturn(ticket);
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	when(ticketDAO.getNbTicket(eq("ABCDEF"))).thenReturn(0);

	parkingService.processExitingVehicle();

	verify(ticketDAO, Mockito.times(1)).getTicket(eq("ABCDEF"));
	verify(ticketDAO, Mockito.times(1)).getNbTicket(eq("ABCDEF"));
	verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }

}