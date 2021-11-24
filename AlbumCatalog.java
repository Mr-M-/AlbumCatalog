import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class AlbumCatalog {
	public static void main(String[] args) {
		File file = new File(".\\AlbumCatalogDoc.txt");
		System.out.println(file.getAbsolutePath());
		
		int numAlbums = 0;
		
		ArrayList<Artist> artists = new ArrayList<Artist>();
		ArrayList<Date> dates = new ArrayList<Date>();
		ArrayList<String> genres = new ArrayList<String>();
		ArrayList<Album> viewList = new ArrayList<Album>();//Stores albums for user to interact with
		ArrayList<Artist> artistViewList = new ArrayList<Artist>();//Stores artists for user to interact with
		Artist lastViewedArtist = null;
		Date lastViewedDate = null;
		String lastViewed = "";//Artist or date
		ArrayList<Album> tempOTD = new ArrayList<Album>();//Stores albums for latest mmdd date
		ArrayList<Artist> tempArtists = new ArrayList<Artist>();//Stores artists for other functions
		
		//Parsing the text file:
		numAlbums += AlbumCatalog.parse(file, artists, dates);//<-- VERY IMPORTANT! DON'T DELETE!
		AlbumCatalog.alphabetize(artists);
		for(Artist a : artists) {
			a.sortDiscography();
		}
		
		//User input
		System.out.println("Welcome to the Intelligent Album Catalog, copyright Monke Software 2021.");
		System.out.println("Type HELP to see a list of functions.");
		
		Scanner keyboard = new Scanner(System.in);
		boolean run = true;
		while(run) {//QUIT quits the program
			System.out.println("What can we do for you?");
			String input = keyboard.nextLine();
			
			if(input.length() > 3 && input.substring(0,4).equals("QUIT")) {
				run = false;
			}
			
			if(input.equals("PA LAST")) {
				if(lastViewedArtist != null) {
					System.out.println(lastViewedArtist);
				}
				else {
					System.out.println("No artist saved.");
				}
			}
			
			else if(input.length() > 4 && input.substring(0,3).equals("PA ")) {//PA for Print Artist Info
				//Will have more functionality once genres are implemented
				String artistName = input.substring(3);
				Artist artist = new Artist("Artist not in catalog.","ERR");
				for(Artist a : artists) {
					if(a.getName().equals(artistName)) {
						artist = a;
						lastViewedArtist = artist;
					}
				}
				System.out.println(artist.getName());
			}
			
			else if(input.equals("PAD LAST")) {
				System.out.println(lastViewedArtist.getDiscographyString());
				lastViewed = "ARTIST";
			}
			
			else if(input.length() > 5 && input.substring(0,4).equals("PAD ")) {//Print Artist Discography
				String artistName = input.substring(4);
				Artist artist = new Artist("Artist not in catalog.","ERR");
				for(Artist a : artists) {
					if(a.getName().equals(artistName)) {
						artist = a;
					}
				}
				lastViewedArtist = artist;
				lastViewed = "ARTIST";
				if(!artist.getArtistCode().equals("ERR")) {
					System.out.println(artist.getDiscographyString());
				}
				else {
					System.out.println(artist.getName());
				}
			}
			
			else if(input.length() > 5 && input.substring(0,4).equals("OTD ")) {
				String date = input.substring(4);
				lastViewed = "DATE";
				if(date.length() == 8) {
					lastViewedDate = dates.get(AlbumCatalog.dateToIndex(date, dates));
				}
				System.out.println(AlbumCatalog.albumDateString(date, dates, tempOTD));
			}
			
			else if(input.equals("PRINT ARTISTS")) {
				int num = 1;
				tempArtists = new ArrayList<Artist>();
				for(Artist a : artists) {
					System.out.println("["+num+"] "+a);
					tempArtists.add(a);
					num++;
				}
				lastViewed = "ARTISTLIST";
			}
			
			else if(input.length() > 14 && input.substring(0,14).equals("PRINT ARTISTS ")) {
				input = input.toUpperCase();
				char letter = input.charAt(14);
				int num = 1;
				tempArtists = new ArrayList<Artist>();
				for(Artist a : artists) {
					if(a.getName().toUpperCase().charAt(0) == letter) {
						System.out.println("["+num+"] "+a);
						tempArtists.add(a);
						num++;
					}
				}
				lastViewed = "ARTISTLIST";
			}
			
			else if(input.length() > 4 && input.substring(0,4).equals("ADD ")){
				int num = Integer.parseInt(input.substring(4));
				if(num < 1) {
					System.out.println("Operation not possible.");
				}
				else if(lastViewed.equals("DATE") && num - 1 < tempOTD.size()) {
					viewList.add(tempOTD.get(num-1));
				}
				else if(lastViewed.equals("ARTIST") && num - 1 < lastViewedArtist.getDiscography().size()) {
					viewList.add(lastViewedArtist.getDiscography().get(num-1));
				}
				else if(lastViewed.equals("ARTISTLIST") && num - 1 < tempArtists.size()) {
					artistViewList.add(tempArtists.get(num-1));
					lastViewedArtist = tempArtists.get(num-1);//Stores selected artist in lastViewedArtist variable
				}
				else {
					System.out.println("Operation not possible.");
				}
			}
			
			else if(input.equals("ADD LAST")) {
				artistViewList.add(lastViewedArtist);
			}
			
			else if(input.equals("VIEWLIST")) {
				if(viewList.size() < 1) {
					System.out.println("Viewlist is empty.");
				}
				else {
					for(int i = 0; i < viewList.size(); i++) {
						System.out.println("["+(i+1)+"] "+viewList.get(i));
					}
				}
			}
			
			else if(input.equals("CLEAR VIEWLIST")) {
				viewList = new ArrayList<Album>();
			}
			
			else if(input.length() == 15 && input.substring(0,13).equals("RMV VIEWLIST ")) {
				int num = Integer.parseInt(input.substring(13));
				if(num < 1 || num > viewList.size()) {
					System.out.println("Operation not possible.");
				}
				else {
					viewList.remove(num-1);
				}
			}
			
			else if(input.equals("AVW")) {
				int num = 1;
				if(artistViewList.size() < 1) {
					System.out.println("Artist view list is empty.");
				}
				else {
					for(Artist a : artistViewList) {
						System.out.println("["+num+"] "+a);
						num++;
					}
				}
				lastViewed = "AVW";
			}
			
			else if(input.equals("CLEAR AVW")) {
				artistViewList = new ArrayList<Artist>();
			}
			
			else if(input.length() > 7 && input.substring(0,8).equals("RMV AVW ")) {
				int num = Integer.parseInt(input.substring(8));
				if(num < 1 || num > artistViewList.size()) {
					System.out.println("Operation not possible.");
				}
				else {
					artistViewList.remove(num-1);
				}
			}
			
			else if(input.length() > 7 && input.substring(0,8).equals("SETLAST ")) {
				int num = Integer.parseInt(input.substring(8));
				if(lastViewed.equals("ARTISTLIST")) {
					lastViewedArtist = tempArtists.get(num-1);
				}
				else if(lastViewed.equals("AVW")) {
					lastViewedArtist = artistViewList.get(num-1);
				}
			}
			
			else if(input.length() == 13 && input.substring(0,11).equals("ALBUM INFO ")) {
				int num = Integer.parseInt(input.substring(11));
				Album a = viewList.get(num-1);
				System.out.println("Album title: "+a.getName());
				System.out.println("Artist: "+a.getArtist());
				System.out.println("Released on "+a.getMonth()+" "+a.getDate()+" "+a.getYear());
				System.out.println(a.getMainGenre());
				System.out.println("You rated this album "+a.getRating()+"/10");
			}
			
			else if(input.equals("HELP")) {
				System.out.println("PA Artist = print artist info");
				System.out.println("PA LAST = print last saved artist info");
				System.out.println("PAD Artist = print artist discography");
				System.out.println("PAD LAST = print discography of last saved artist");
				System.out.println("OTD Date = print albums from date mmdd or mmddyyyy");
				System.out.println("PRINT ARTISTS = print list of artists in catalog");
				System.out.println("PRINT ARTISTS letter = print out artists whose names start with letter");
				System.out.println("SETLAST num = set artist number num to last saved artist");
				System.out.println("-----ADDING-----");
				System.out.println("ADD num = add album number num to viewlist or "
						+ "artist number num to artist view list");
				System.out.println("ADD LAST = add last viewed artist to artist view list");
				System.out.println("-----LISTS-----");
				System.out.println("VIEWLIST = print viewlist");
				System.out.println("CLEAR VIEWLIST = clear viewlist");
				System.out.println("RMV VIEWLIST num = remove album at num in viewlist");
				System.out.println("ALBUM INFO num = print complete album info for album num in viewlist");
				System.out.println("AVW = print artist view list");
				System.out.println("CLEAR AVW = clear artist view list");
				System.out.println("RMV AVW num = remove artist number num from artist view list");
				System.out.println("QUIT = quit program");
			}
			
		}
		System.out.println("Thanks for using the Intelligent Album Catalog!");
		keyboard.close();
		
	}
	
	public static String getDiscographyString(String artist, ArrayList<Artist> aList) {
		for(Artist a : aList) {
			if(a.getName().equals(artist)) {
				return a.getDiscographyString();
			}
		}
		return "Artist has no albums in catalog.";
	}
	
	public static Date getAlbumDate(ArrayList<Date> dates, Album thisAlbum) {//Returns the date of a given album
		for(int j = 0; j < dates.size(); j++) {
			if(dates.get(j).getDate().equals(thisAlbum.getFullDate())){
				return dates.get(j);
			}
		}
		return null;
	}
	
	public static int dateToIndex(String date, ArrayList<Date> dates) {//Return the index of given date
		for(int j = 0; j < dates.size(); j++) {
			if(dates.get(j).getDate().equals(date)){
				return j;
			}
		}
		return -1;
	}
	
	public static ArrayList<Integer> indecesOfDate(String date, ArrayList<Date> dates) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int j = 0; j < dates.size(); j++) {
			if(dates.get(j).getMMDD().equals(date)){
				out.add(j);
			}
		}
		return out;
	}
	
	public static ArrayList<Integer> indecesOfAlbumDate(String date, ArrayList<Date> dates) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int j = 0; j < dates.size(); j++) {
			if(dates.get(j).getMMDD().equals(date) && dates.get(j).getOnThisDay().size() > 0){
				out.add(j);
			}
		}
		return out;
	}
	
	public static String albumDateString(String date, ArrayList<Date> dates, ArrayList<Album> temp) {//Prints all albums on a given MMDD or MMDDYYYY
		String out = "";
		if(date.length() == 4) {
			ArrayList<Integer> dateIndeces = AlbumCatalog.indecesOfAlbumDate(date, dates);
			int num = 1;
			for(int k = 0; k < dateIndeces.size(); k++) {
//				out = out+"\n"+dates.get(dateIndeces.get(k)).getOTDString();
				Date d = dates.get(dateIndeces.get(k));
				for(Album a : d.getOnThisDay()) {
					out = out+"\n["+num+"] "+a;
					temp.add(a);
					num++;
				}
			}
			if(out.length() > 1) {
				return "On this day in history, "+date.substring(0,2)+" "
						+date.substring(2)+", the following albums were released:\n"
						+out.substring(1);
			}
			return "...";
		}
		else if(date.length() == 8) {
			int dateIndex = AlbumCatalog.dateToIndex(date, dates);
			temp = dates.get(dateIndex).getOnThisDay();
			return "On this day in history, "+date.substring(0,2)+" "
			+date.substring(2,4)+" "+date.substring(4)+", the following albums were released:\n"
			+dates.get(dateIndex).getOTDString();
		}
		return "Date not properly formatted.";
	}
	
	public static void alphabetize(ArrayList<Artist> artists) {
		for(int i = 0; i < artists.size(); i++) {
			for(int j = i+1; j < artists.size(); j++) {
				if(artists.get(i).getName().compareTo(artists.get(j).getName()) > 0) {
					Artist temp = artists.get(i);
					artists.set(i, artists.set(j, temp));
				}
			}
		}
	}
	
	public static int parse(File f, ArrayList<Artist> artistsList, ArrayList<Date> dates) {
		int numAlbumsHere = 0;	
		try {

	        Scanner scan = new Scanner(f);

	        while(scan.hasNext()) {
	        	String line = scan.nextLine();//Read each line as an album
	        	while(line.indexOf("//") != -1) {
	        		line = scan.nextLine();
	        	}//Allows for commenting in txt file
	        	String lineName = line.substring(line.indexOf("OPNM")+5,line.indexOf("OPRT")-1);
	        	String lineArtist = line.substring(line.indexOf("OPRT")+5,line.indexOf("OPID")-1);
	        	String lineArtistCode = line.substring(line.indexOf("OPID")+5,line.indexOf("OPID")+8);
	        	String lineMonth = line.substring(line.indexOf("OPID")+8,line.indexOf("OPID")+10);
	        	String lineDate = line.substring(line.indexOf("OPID")+10,line.indexOf("OPID")+12);
	        	String lineYear = line.substring(line.indexOf("OPID")+12,line.indexOf("OPID")+16);
	        	String lineRating = line.substring(line.indexOf("OPID")+16,line.indexOf("OPID")+18);
	        	String lineMainGenre = line.substring(line.indexOf("OPID")+18,line.indexOf("OPTX")-1);
	        	String lineID = lineArtistCode+lineMonth+lineDate+lineYear+lineRating+lineMainGenre;
//	        	int indexOfDate = AlbumCatalog.dateToIndex(lineMonth+lineDate+lineYear, dates);
	        	String lineDateFull = lineMonth+lineDate+lineYear;
	        	
	        	boolean isNewArtist = true;
	        	Artist aTemp = null;
	        	for(Artist a : artistsList) {
	        		if(lineArtistCode.equals(a.getArtistCode())){
	        			isNewArtist = false;
	        			aTemp = a;
	        		}
	        	}
	        	
	        	boolean isNewDate = true;
	        	Date dTemp = null;
	        	for(Date d : dates) {
	        		if(d.getDate().equals(lineDateFull)) {
	        			isNewDate = false;
	        			dTemp = d;
	        		}
	        	}
	        	
	        	if(!isNewArtist) {
	        		Album album = new Album(lineName, aTemp, lineID);
	        		aTemp.add(album);
	        		if(isNewDate) {
	        			Date dNew = new Date(lineDateFull);
	        			dates.add(dNew);
	        			dNew.getOnThisDay().add(album);
	        		}
	        		else {
	        			dTemp.getOnThisDay().add(album);
	        			dTemp.sortOTD();
	        		}
	        		numAlbumsHere++;
	        	}
	        	else {
	        		Artist newArtist = new Artist(lineArtist, lineArtistCode);
	        		Album album = new Album(lineName, newArtist, lineID);
	        		artistsList.add(newArtist);
	        		newArtist.add(album);
	        		if(isNewDate) {
	        			Date dNew = new Date(lineDateFull);
	        			dates.add(dNew);
	        			dNew.getOnThisDay().add(album);
	        		}
	        		else {
	        			dTemp.getOnThisDay().add(album);
	        			dTemp.sortOTD();
	        		}
	        		numAlbumsHere++;
	        	}	        
	        }
	        scan.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.out.println("File not found.");
	    }
		return numAlbumsHere;
	}	
}
