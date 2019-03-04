package com.techelevator.view;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.campgrounds.Campgrounds;
import com.techelevator.parks.Parks;
import com.techelevator.reservations.Reservations;

import Site.Sites;

import java.text.DateFormatSymbols;


public class Menu {
	
	private PrintWriter out;
	private Scanner in;
	private DateTimeFormatter dTFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static final String[] PARK_COMMAND_OPTIONS = {"View Campgrounds", "Search for Reservation", "View Upcoming Reservations", "Return to Previous Screen"};
	private static final String[] CAMPGROUND_COMMAND_OPTIONS = {"Search for Available Reservation", "Return to Previous Screen"};
	private DateFormatSymbols getMonth = new DateFormatSymbols();
	
	
	public Menu(Scanner in, PrintWriter out) {
		this.out = out;
		this.in = in;
	}	
	
	public Object getParkChoiceFromOptions(List<Parks> options) {
		Object choice = null;
		while(choice == null) {
			displayParkOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	protected Object getChoiceFromUserInput(List<Parks> options) {
		Object choice = null;
		String userInput = in.nextLine();
		out.println();
		if (userInput.equalsIgnoreCase("Q")) {
			String userInputToUpperCase = userInput.toUpperCase();
			return userInputToUpperCase;
		}
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= options.size()) {
				choice = options.get(selectedOption - 1);
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	protected void displayParkOptions(List<Parks> options) {
		
		out.println();
		out.println("Select a Park for Further Details");
		out.println();
		for(int i = 0; i < options.size(); i++) {
			int optionNum = i + 1;
			out.println(optionNum+") "+options.get(i).getName());
		}
		out.flush();
		displayQuitOption();
		
	}
	
	public void displaySelectedParkInfo(Parks park) {
		out.println(park.getName() + " National Park");
		out.printf("%-18s %s\n","Location:", park.getLocation());
		out.printf("%-18s %s\n","Established:", park.getEstablishDate().format(dTFormat));
		out.printf("%-18s %,d sq km\n","Area:", park.getArea());
		out.printf("%-18s %,d\n\n","Annual Visitors:", park.getVisitors());
		out.flush();
		displayWrappedDescription(park.getDescription());
	}
	private void displayWrappedDescription(String description) {
		List<String> descriptionLines = wrapString(description, 84);
		for (String line : descriptionLines) {
			out.println(line);
			out.flush();
		}
		
	}
	
	private List<String> wrapString(String string, int desiredSpace) {
		String[] splatString = string.split(" ");
		List<String> wrappedLines = new ArrayList<String>();
		String printString = "";
		int lengthCount = 0;
		
		for (String word : splatString) {
			lengthCount++;
			if (printString.length() < desiredSpace) {
				printString += word + " ";
			}
			if (printString.length() >= desiredSpace) {
				wrappedLines.add(printString);
				printString = "";
			}
			if (lengthCount == splatString.length) {
				wrappedLines.add(printString);

			}
		}
		return wrappedLines;
	}
	
	public void displayQuitOption() {
		out.println("Q) Quit");
		out.flush();
	}
	
	public void displayExitMessage() {
		out.println("Thank you for using Parks Reservation Master 1.0!");
		out.flush();
	}
	
	public void displaySelectedParkName(Parks park) {
		out.println(park.getName() + " National Park Campgrounds\n");
		out.flush();
	}
	
	public void displayCampgroundsInsideSelectedPark(Parks park, List<Campgrounds> campgroundList) {
		out.printf("%-5s%-33s%-12s%-12s%-12s\n", "", "Name", "Open", "Close", "Daily Fee");
		int campgroundCounter = 1;
		for (Campgrounds campground : campgroundList) {
			out.printf("%-5s%-33s%-12s%-12s$%-12.2f\n", "#" + campgroundCounter, campground.getName(), 
					getMonth.getMonths()[Integer.parseInt(campground.getOpenFromMonth()) - 1], getMonth.getMonths()[Integer.parseInt(campground.getOpenUntilMonth()) - 1], campground.getDailyFee());
			campgroundCounter++;
		}
		out.flush();
	}
	public void displayReserationHeader() {
		out.println("Search for a Campground Reservation");
		out.println();
		out.flush();
	}

	
	public String getParkCommandChoiceFromOptions() {
		String choice = null;
		while(choice == null) {
			displayParkCommandList();
			choice = getParkCommandChoiceFromUserInput();
		}
		return choice;
	}
	
	private String getParkCommandChoiceFromUserInput() {
		String choice = null;
		String userInput = in.nextLine();
		out.println();
		
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= PARK_COMMAND_OPTIONS.length) {
				choice = PARK_COMMAND_OPTIONS[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}
	
	private void displayParkCommandList() {
		out.println();
		out.println();
		out.println("Select a Command");
		for (int i = 0; i < PARK_COMMAND_OPTIONS.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + PARK_COMMAND_OPTIONS[i]);
		}
		out.flush();
	}
	
	private void displayCampgroundCommandList() {
		out.println();
		out.println();
		out.println("Select a Command");
		for (int i = 0; i < CAMPGROUND_COMMAND_OPTIONS.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + CAMPGROUND_COMMAND_OPTIONS[i]);
		}
		out.flush();
	
	}
	
	public String getCampgroundCommandChoiceFromOptions() {
		String choice = null;
		while(choice == null) {
			displayCampgroundCommandList();
			choice = getCampgroundCommandChoiceFromUserInput();
		}
		return choice;
	}
	
	private String getCampgroundCommandChoiceFromUserInput() {
		String choice = null;
		String userInput = in.nextLine();
		out.println();
		
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= CAMPGROUND_COMMAND_OPTIONS.length) {
				choice = CAMPGROUND_COMMAND_OPTIONS[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}
	
	public Object getDesiredCampgroundFromUser(List<Campgrounds> campgroundList) {
		out.println();
		out.print("Which campground (enter 0 to cancel)? ");
		out.flush();
		Object choice = null;
		String userInput = in.nextLine();
		
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption == 0) {
				return "Quit";
			}
			if(selectedOption > 0 && selectedOption <= campgroundList.size()) {
				choice = campgroundList.get(selectedOption - 1);
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		
		return choice;
	}

	public LocalDate getArrivalDateFromUser() {
		out.println();
		out.print("What is the arrival date? (mm/dd/yyyy) ");
		out.flush();
		String choice = null;
		LocalDate userInputAsLocalDate = null;
		String userInputString = in.nextLine();
		try {
			userInputAsLocalDate = LocalDate.parse(userInputString, dTFormat);
			choice = "not null";
		} catch (DateTimeParseException e) {
			//catch invalid date, loop will ask for their input again
		}
		if(choice == null || userInputAsLocalDate.isBefore(LocalDate.now())) {
			userInputAsLocalDate = null;
			out.println("\n*** "+userInputString+" is not a valid option ***\n");
		}
		return userInputAsLocalDate;
	}

	public LocalDate getDepartureDateFromUser(LocalDate arrival) {
		out.println();
		out.print("What is the departure date? (mm/dd/yyyy) ");
		out.flush();
		String choice = null;
		LocalDate userInputAsLocalDate = null;
		String userInputString = in.nextLine();
		try {
			userInputAsLocalDate = LocalDate.parse(userInputString, dTFormat);
			choice = "not null";
		} catch (DateTimeParseException e) {
			//catch invalid date, loop will ask for their input again
		}
		if(choice == null || userInputAsLocalDate.isBefore(arrival) || userInputAsLocalDate.isEqual(arrival)) {
			userInputAsLocalDate = null;
			out.println("\n*** "+userInputString+" is not a valid option ***\n");
		}
		return userInputAsLocalDate;
	}
	
	public Object waitForValidCampgroundFromUser(List<Campgrounds> campgroundList) {
		Object choice = null;
		while(choice == null) {
		choice = getDesiredCampgroundFromUser(campgroundList);
		}
		return choice;
	}
	
	public Object waitForValidArrivalDateFromUser() {
		Object choice = null;
		while(choice == null) {
			choice = getArrivalDateFromUser();
		}
		return choice;
	}
	
	public Object waitForValidDepartureDateFromUser(LocalDate arrival) {
		Object choice = null;
		while(choice == null) {
			choice = getDepartureDateFromUser(arrival);
		}
		return choice;
	}
	
	public void displaySiteResults(List<Sites> siteList, Campgrounds campground, Period totalDays) {
		out.println("\nResults Matching Your Search Criteria\n");
		out.printf("%-12s%-12s%-18s%-18s%-12s%-4s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
		out.flush();
		
		for (Sites site : siteList) {
			String siteAccessibleBooleanAsString = site.isAccessible() ? "Yes" : "No";
			String siteUtilitiesAsString = site.isHasUtilities() ? "Yes" : "N/A";
			String rVLengthAsString = Integer.toString(site.getMaxRVLength());
			if (site.getMaxRVLength() == 0) {
				rVLengthAsString = "N/A";
			} 
			out.printf("%-12s%-12s%-18s%-18s%-12s$%-4.0f\n", site.getSiteNumber(), site.getMaxOccupancy(), 
					siteAccessibleBooleanAsString, rVLengthAsString, siteUtilitiesAsString, 
					(campground.getDailyFee().multiply(new BigDecimal(totalDays.getDays()))));
		}
		out.flush();
	}
	
	public String displayNoSitesAvailable() {
		while(true) {
			out.print("Sorry -- No sites matched your search. Would you like to search again? (Y/N) ");
			out.flush();
			String userChoice = in.nextLine();
			if (userChoice.equalsIgnoreCase("Y")) {
				return "Yes";
			} else if (userChoice.equalsIgnoreCase("N")) {
				return "No";
			} else {
				System.out.println("Choice was invalid, please enter Y for yes or N for no.");
			}
		}
	}
	
	public Object waitForValidSiteFromUser(List<Sites> siteList) {
		Object choice = null;
		while(choice == null) {
		choice = getDesiredSiteFromUser(siteList);
		
		}
		return choice;
	}
	
	private Object getDesiredSiteFromUser(List<Sites> siteList) {
		out.println();
		out.print("Which site should be reserved (enter 0 to cancel)? ");
		out.flush();
		Object choice = null;
		String userInput = in.nextLine();
		
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption == 0) {
				return "Quit";
			}
			for (Sites site : siteList) {
				if (selectedOption == site.getSiteNumber()) {
					return site;
				}
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		
		return choice;
	}
	
	public String getReservationName() { 
		out.print("What name should the reservation be made under? ");
		out.flush();
		String userInput = in.nextLine();
		return userInput;
	}
	public void confirmReservation(Long resId) {
		out.println("\nThe reservation has been made and the confirmation id is: " + resId);
		out.println();
		out.flush();
	}
	
	public String promptUserForAdvancedSearch() {
		while(true) {
			out.print("\nWould you like to advanced search? (Y/N) ");
			out.flush();
			String userChoice = in.nextLine();
			
			if (userChoice.equalsIgnoreCase("Y")) {
				return "Yes";
			} else if (userChoice.equalsIgnoreCase("N")) {
				return "No";
			} else {
				System.out.println("Choice was invalid, please enter Y for yes or N for no.");
			}
		}
	}
	
	public int getMaxOccupancyFromUser() {
		out.println();
		out.print("What Max Occupancy do you need? ");
		out.flush();
		Object choice = null;
		String userInput = in.nextLine();
		int selectedOption = 0;
		while (choice == null) {
			try {
				selectedOption = Integer.valueOf(userInput);
				choice = "not null";
			} catch(NumberFormatException e) {
				// eat the exception, an error message will be displayed below since choice will be null
			}
			if(choice == null) {
				out.println("\n*** "+userInput+" is not a valid option ***\n");
			}
		}
		
		return selectedOption;
	}
	
	public boolean askUserForRVPreference() {
		while(true) {
			out.print("\nAre you going to bring an RV? (Y/N) ");
			out.flush();
			String userChoice = in.nextLine();
			
			if (userChoice.equalsIgnoreCase("Y")) {
				return true;
			} else if (userChoice.equalsIgnoreCase("N")) {
				return false;
			} else {
				System.out.println("Choice was invalid, please enter Y for yes or N for no.");
			}
		}
	}
	
	public int getMaxRVLengthFromUser() {
		out.println();
		out.print("What RV Length do you need? (in feet) ");
		out.flush();
		Object choice = null;
		String userInput = in.nextLine();
		int selectedOption = 0;
		while (choice == null) {
			try {
				selectedOption = Integer.valueOf(userInput);
				choice = "not null";
			} catch(NumberFormatException e) {
				// eat the exception, an error message will be displayed below since choice will be null
			}
			if(choice == null) {
				out.println("\n*** "+userInput+" is not a valid option ***\n");
			}
		}
		
		return selectedOption;
	}
	
	public boolean askUserForUtilityPreference() {
		while(true) {
			out.print("\nWould you like the site to have utiities? (Y/N) ");
			out.flush();
			String userChoice = in.nextLine();
			
			if (userChoice.equalsIgnoreCase("Y")) {
				return true;
			} else if (userChoice.equalsIgnoreCase("N")) {
				return false;
			} else {
				System.out.println("Choice was invalid, please enter Y for yes or N for no.");
			}
		}
	}
	
	public boolean askUserForAccessiblePreference() {
		while(true) {
			out.print("\nWould you like for the site to be accessible? (Y/N) ");
			out.flush();
			String userChoice = in.nextLine();
			
			if (userChoice.equalsIgnoreCase("Y")) {
				return true;
			} else if (userChoice.equalsIgnoreCase("N")) {
				return false;
			} else {
				System.out.println("Choice was invalid, please enter Y for yes or N for no.");
			}
		}
	}
	public void displayReservations30DaysByParkId(List<Reservations> resList) {
		out.printf("%-20s%-15s%-20s%-20s%-20s\n", "Reservation ID", "Site ID", "Arrival", "Departure", "Date Signed Up");
		out.println();
		for (Reservations res : resList) {
			out.printf("%-20s%-15s%-20s%-20s%-20s\n", Long.toString(res.getReservationId()), Long.toString(res.getSiteId()), 
					res.getFromDate().format(dTFormat), res.getToDate().format(dTFormat), res.getCreateDate().format(dTFormat));
		}
		out.println();
		out.flush();
	}
	public void displayNoReservationsNext30Days() {
		out.println("***No Reservations In Next 30 Days***\n");
		out.flush();
	}
	
	public void displaySiteResultsHeader() {
		out.println("\nResults Matching Your Search Criteria\n");
		out.printf("%-33s%-12s%-12s%-18s%-18s%-12s%-4s\n", "Camp Name", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
		out.flush();
	}
	
	public void displaySiteResultsBasedOnPark(Sites site, BigDecimal dailyFee, Period totalDays, String campName) {
		String siteAccessibleBooleanAsString = site.isAccessible() ? "Yes" : "No";
		String siteUtilitiesAsString = site.isHasUtilities() ? "Yes" : "N/A";
		String rVLengthAsString = Integer.toString(site.getMaxRVLength());
		if (site.getMaxRVLength() == 0) {
				rVLengthAsString = "N/A";
		} 
		out.printf("%-33s%-12s%-12s%-18s%-18s%-12s$%-4.0f\n", campName, site.getSiteNumber(), site.getMaxOccupancy(), 
					siteAccessibleBooleanAsString, rVLengthAsString, siteUtilitiesAsString, 
					(dailyFee.multiply(new BigDecimal(totalDays.getDays()))));
		out.flush();
	}
	
	public void displayParksReservationBranding() {
		out.println("\n" + 
				"  ____            _          ____                                _   _              \n" + 
				" |  _ \\ __ _ _ __| | _____  |  _ \\ ___  ___  ___ _ ____   ____ _| |_(_) ___  _ __   \n" + 
				" | |_) / _` | '__| |/ / __| | |_) / _ \\/ __|/ _ \\ '__\\ \\ / / _` | __| |/ _ \\| '_ \\  \n" + 
				" |  __/ (_| | |  |   <\\__ \\ |  _ <  __/\\__ \\  __/ |   \\ V / (_| | |_| | (_) | | | | \n" + 
				" |_|   \\__,_|_|  |_|\\_\\___/ |_| \\_\\___||___/\\___|_|    \\_/ \\__,_|\\__|_|\\___/|_| |_| \n" + 
				"  __  __           _              _   ___         \n" + 
				" |  \\/  | __ _ ___| |_ ___ _ __  / | / _ \\      \n" + 
				" | |\\/| |/ _` / __| __/ _ \\ '__| | || | | |     \n" + 
				" | |  | | (_| \\__ \\ ||  __/ |    | || |_| |      \n" + 
				" |_|  |_|\\__,_|___/\\__\\___|_|    |_(_)___/      \n" + 
				"                                                   \n" + 
				"      application by Zach Walker and Julian Calip-DuBois\n\n");
		


   
         

    




	}
	
}
