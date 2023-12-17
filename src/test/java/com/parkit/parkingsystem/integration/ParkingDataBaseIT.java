package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	dataBasePrepareService.clearDataBaseEntries();
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    }

    @AfterAll
    private static void tearDown() {
	dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();

	assertNotNull(ticketDAO.getTicket("ABCDEF"));
	assertEquals("ABCDEF", ticketDAO.getTicket("ABCDEF").getVehicleRegNumber());
	assertNotNull(ticketDAO.getTicket("ABCDEF").getParkingSpot());
	assertFalse(ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable());
    }

    @Test
    public void testParkingLotExit() {
	testParkingACar();
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	
	parkingService.processExitingVehicle();

	assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
	assertNotNull(ticketDAO.getTicket("ABCDEF").getPrice());
    }

    @Test
    public void testParkingLotExitRecurringUser() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	
	Ticket ticket = new Ticket();
	ticket.setVehicleRegNumber("ABCDEF");
	ticket.setInTime(new Date(System.currentTimeMillis() - (2* 60 * 60 * 1000)));
	ticket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
	
	ticketDAO.saveTicket(ticket);
	
	parkingService.processIncomingVehicle();
	parkingService.processExitingVehicle();
	
	assertEquals(2, ticketDAO.getNbTicket("ABCDEF"));
    }
}
