package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.controller.InformationController;
import com.techelevator.parks.Parks;
import com.techelevator.reservations.Reservations;
import com.techelevator.view.Menu;

import Site.Sites;


public class CampgroundCLI {
	
	private InformationController infoControl;
	private static Menu menu;
	
	public static void main(String[] args) {
		OutputStream out = System.out;
		InputStream in = System.in;
		PrintWriter writer = new PrintWriter(out);
		Scanner reader = new Scanner(in);
		menu = new Menu(reader, writer);
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		infoControl = new InformationController(dataSource);
	}
	
	public void run() {
		
		while(true) {
			menu.displayParksReservationBranding();
			List<Parks> allParks = infoControl.getAllParks();
			Object userChoice = menu.getParkChoiceFromOptions(allParks);
			boolean hasUserSelectedPrevScreenInParkCommands = false;
			
			if (userChoice.equals("Q")) {
				menu.displayExitMessage();
				break;
			}
			
			while (!hasUserSelectedPrevScreenInParkCommands) {	
			Parks userChoiceAsPark = (Parks)userChoice;
			menu.displaySelectedParkInfo(userChoiceAsPark);
			String parkCommandChoice = menu.getParkCommandChoiceFromOptions();
			

				if (parkCommandChoice.equals("View Campgrounds")) {
					boolean hasUserSelectedPreviousScreenInCampgroundCommands = false;
					while (!hasUserSelectedPreviousScreenInCampgroundCommands) {
						handleViewCampgroundsMenu(userChoiceAsPark);
				
					String campgroundCommandChoice = menu.getCampgroundCommandChoiceFromOptions();
				
					if(campgroundCommandChoice.equals("Search for Available Reservation")) {
						List<Sites> siteListBasedOnUserChoices = null;
						Period totalDays = null;
						Campgrounds userCampgroundChoiceAsCampground = null;
						LocalDate userArrivalDate = null;
						LocalDate userDepartureDate = null;
						String enterDatesAgainUserChoice = "";
						boolean hasUserEnteredValidSearch = false;
						Object userCampgroundChoice = "";
						
						while (!hasUserEnteredValidSearch) {
							
							menu.displayReserationHeader();
							menu.displayCampgroundsInsideSelectedPark(userChoiceAsPark, infoControl.getCampgroundsByParkId(userChoiceAsPark));
							userCampgroundChoice = menu.waitForValidCampgroundFromUser(infoControl.getCampgroundsByParkId(userChoiceAsPark));
							
							if (userCampgroundChoice.equals("Quit")) {
								break;
							}
							
							userCampgroundChoiceAsCampground = (Campgrounds)userCampgroundChoice;
							userArrivalDate = (LocalDate)menu.waitForValidArrivalDateFromUser();
							userDepartureDate = (LocalDate)menu.waitForValidDepartureDateFromUser(userArrivalDate);
							totalDays = Period.between(userArrivalDate, userDepartureDate);
							String advancedSearchChoice = menu.promptUserForAdvancedSearch();
							
							if (advancedSearchChoice.equals("Yes")) {
								
							int maxOccupancy = menu.getMaxOccupancyFromUser();
							boolean isAccessible = menu.askUserForAccessiblePreference();
							boolean hasRV = menu.askUserForRVPreference();
							int maxRVLength = 0;
							
							if (hasRV) {
								maxRVLength = menu.getMaxRVLengthFromUser();
							}
							
							boolean hasUtilities = menu.askUserForUtilityPreference();
								siteListBasedOnUserChoices = infoControl.getTopFiveSitesAdvancedSearch(userCampgroundChoiceAsCampground, 
										userArrivalDate, userDepartureDate, maxOccupancy, isAccessible, maxRVLength, hasUtilities);
							}
							
							if (advancedSearchChoice.equals("No")) {
								siteListBasedOnUserChoices = infoControl.getTopFiveSites(userCampgroundChoiceAsCampground, 
															userArrivalDate, userDepartureDate);
							}
							
							if (siteListBasedOnUserChoices.isEmpty()) {
								enterDatesAgainUserChoice = menu.displayNoSitesAvailable();
							}
							
							if (enterDatesAgainUserChoice.equals("No")) {
								break;
							}
							
							if (!siteListBasedOnUserChoices.isEmpty()) {
								hasUserEnteredValidSearch = true;
							}
							
						}
						
						handleInvalidDateLogic(enterDatesAgainUserChoice, userCampgroundChoice, siteListBasedOnUserChoices, 
								userCampgroundChoiceAsCampground, totalDays, userArrivalDate, userDepartureDate);

						
								hasUserSelectedPreviousScreenInCampgroundCommands = true;
								hasUserSelectedPrevScreenInParkCommands = true;

					}
					
					if (campgroundCommandChoice.equals("Return to Previous Screen")) {
						hasUserSelectedPreviousScreenInCampgroundCommands= true;
						
					}
				}

				
			}
				
			if (parkCommandChoice.equals("Search for Reservation")) {
				LocalDate userArrivalDate = (LocalDate)menu.waitForValidArrivalDateFromUser();
				LocalDate userDepartureDate = (LocalDate)menu.waitForValidDepartureDateFromUser(userArrivalDate);
				List<Sites> sitesBasedOnUserParkChoice = infoControl.getTopFiveSitesByPark(userChoiceAsPark, userArrivalDate, userDepartureDate);
				menu.displaySiteResultsHeader();
				handleSiteAvailabilitySearchParkWide(sitesBasedOnUserParkChoice, userArrivalDate, userDepartureDate);

				
				Object siteChoiceAsObj = menu.waitForValidSiteFromUser(sitesBasedOnUserParkChoice);
				if (siteChoiceAsObj == "Quit") {
					break;
				}
				handleMakeReservation(siteChoiceAsObj, userArrivalDate, userDepartureDate);
				hasUserSelectedPrevScreenInParkCommands = true;
				break;
			}
			
			if (parkCommandChoice.equals("View Upcoming Reservations")) {
				handleDisplayUpcomingReservations(userChoiceAsPark);

			}
			
			if (parkCommandChoice.equals("Return to Previous Screen")) {
					hasUserSelectedPrevScreenInParkCommands = true;
				}
			
			}
		}
	}
	
	private void handleInvalidDateLogic(String enterDatesAgainUserChoice, Object userCampgroundChoice, List<Sites> siteListBasedOnUserChoices, 
			Campgrounds userCampgroundChoiceAsCampground, Period totalDays, LocalDate userArrivalDate, LocalDate userDepartureDate) {
		while (true) {
			if (enterDatesAgainUserChoice.equals("No") || userCampgroundChoice.equals("Quit")) {
				break;
			}
			
			menu.displaySiteResults(siteListBasedOnUserChoices, userCampgroundChoiceAsCampground, totalDays);
			Object siteChoiceAsObj = menu.waitForValidSiteFromUser(siteListBasedOnUserChoices);
			if (siteChoiceAsObj == "Quit") {
				break;
			}
			
			handleMakeReservation(siteChoiceAsObj, userArrivalDate, userDepartureDate);
			break;
		}
	}
	
	private void handleViewCampgroundsMenu(Parks userChoiceAsPark) {
		menu.displaySelectedParkName(userChoiceAsPark);
		menu.displayCampgroundsInsideSelectedPark(userChoiceAsPark, 
				infoControl.getCampgroundsByParkId(userChoiceAsPark));
	}
	
	private void handleMakeReservation(Object siteChoiceAsObj, LocalDate userArrivalDate, LocalDate userDepartureDate) {
		String resName = menu.getReservationName();
		Sites siteChoiceAsSite = (Sites)siteChoiceAsObj;
		long reservationId = infoControl.makeReservation(userArrivalDate, userDepartureDate, resName, siteChoiceAsSite);;
		menu.confirmReservation(reservationId);
	}
	
	private void handleSiteAvailabilitySearchParkWide(List<Sites> sitesBasedOnUserParkChoice, LocalDate userArrivalDate, LocalDate userDepartureDate) {
		for (Sites site : sitesBasedOnUserParkChoice) {
			BigDecimal dailyFee = infoControl.getDailyFeeBasedOnCampgroundId(site);
			Period totalDays = Period.between(userArrivalDate, userDepartureDate);
			String campName = infoControl.getCampNameFromDataBase(site);
			menu.displaySiteResultsBasedOnPark(site, dailyFee, totalDays, campName);
		}
	}
	
	private void handleDisplayUpcomingReservations(Parks userChoiceAsPark) {
		List<Reservations> resList = infoControl.returnReservations30DaysByParkId(userChoiceAsPark.getParkId());
		if(!resList.isEmpty()) {
			menu.displayReservations30DaysByParkId(resList);
		}
		else {
			menu.displayNoReservationsNext30Days();
		}
	}

}
