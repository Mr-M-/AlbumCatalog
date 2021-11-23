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
		File file = new File("C:\\Users\\matth\\Documents\\AlbumCatalogDoc.txt");//Change this to the file location on your computer
		int numAlbums = 0;
		//Counts number of lines; now unnecessary:
//		try {
//		BufferedReader reader = new BufferedReader(new FileReader("C:\\\\Users\\\\matth\\\\Documents\\\\AlbumCatalogDoc.txt"));
//		while (reader.readLine() != null) numAlbums++;
//		reader.close();
//		}
//		catch(IOException e){
//			e.printStackTrace();
//			System.out.println("File not found.");
//		}
		
		ArrayList<Artist> artists = new ArrayList<Artist>();
		ArrayList<Date> dates = new ArrayList<Date>();
		ArrayList<String> genres = new ArrayList<String>();
		ArrayList<Album> viewList = new ArrayList<Album>();//Stores albums for user to interact with
		Artist lastViewedArtist = null;
		Date lastViewedDate = null;
		String lastViewed = "";//Artist or date
		ArrayList<Album> tempOTD = new ArrayList<Album>();//Stores albums for latest mmdd date
		
		//Generating dates 1900 to 2022 with brute force:
		for(int m = 1; m < 10; m++) {
			for(int d = 1; d < 10; d++) {
				for(int y = 1900; y < 2023; y++) {
					dates.add(new Date("0"+String.valueOf(m)+"0"+String.valueOf(d)+String.valueOf(y)));
				}
			}
			for(int d = 10; d < 32; d++) {
				for(int y = 1900; y < 2023; y++) {
					dates.add(new Date("0"+String.valueOf(m)+String.valueOf(d)+String.valueOf(y)));
				}
			}
		}
		for(int m = 10; m < 13; m++) {
			for(int d = 1; d < 10; d++) {
				for(int y = 1900; y < 2023; y++) {
					dates.add(new Date(String.valueOf(m)+"0"+String.valueOf(d)+String.valueOf(y)));
				}
			}
			for(int d = 10; d < 32; d++) {
				for(int y = 1900; y < 2023; y++) {
					dates.add(new Date(String.valueOf(m)+String.valueOf(d)+String.valueOf(y)));
				}
			}
		}
		
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
				for(Artist a : artists) {
					System.out.println("["+num+"] "+a);
					num++;
				}
				lastViewed = "ARTISTLIST";
			}
			
			else if(input.length() > 5 && input.substring(0,4).equals("ADD ")){
				int num = Integer.parseInt(input.substring(4,6));
				if(num < 1) {
					System.out.println("Operation not possible.");
				}
				else if(lastViewed.equals("DATE") && num - 1 < tempOTD.size()) {
					viewList.add(tempOTD.get(num-1));
				}
				else if(lastViewed.equals("ARTIST") && num - 1 < lastViewedArtist.getDiscography().size()) {
					viewList.add(lastViewedArtist.getDiscography().get(num-1));
				}
				else if(lastViewed.equals("ARTISTLIST") && num - 1 < artists.size()) {
					lastViewedArtist = artists.get(num-1);//Stores selected artist in lastViewedArtist variable
				}
				else {
					System.out.println("Operation not possible.");
				}
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
			
			else if(input.length() == 13 && input.substring(0,11).equals("ALBUM INFO ")) {
				int num = Integer.parseInt(input.substring(11,13));
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
				System.out.println("ADD Num = add album number num of artist or date to viewlist, num in 0X or XX format");
				System.out.println("VIEWLIST = print viewlist");
				System.out.println("CLEAR VIEWLIST = clear viewlist");
				System.out.println("RMV VIEWLIST num = remove album at num in viewlist, nums in 0X or XX format");
				System.out.println("ALBUM INFO num = print complete album info for album num in viewlist, nums in 0X or XX format");
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
	        	int indexOfDate = AlbumCatalog.dateToIndex(lineMonth+lineDate+lineYear, dates);

	        	boolean isNewArtist = true;
	        	Artist aTemp = null;
	        	for(Artist a : artistsList) {
	        		if(lineArtistCode.equals(a.getArtistCode())){
	        			isNewArtist = false;
	        			aTemp = a;
	        		}
	        	}
	        	if(!isNewArtist) {
	        		Album album = new Album(lineName, aTemp, lineID);
	        		aTemp.add(album);
	        		dates.get(indexOfDate).getOnThisDay().add(album);
	        		dates.get(indexOfDate).sortOTD();
	        		//Testing:
//	        		System.out.println(dates.get(indexOfDate).getOnThisDay());
	        		numAlbumsHere++;
	        	}
	        	else {
	        		Artist newArtist = new Artist(lineArtist, lineArtistCode);
	        		Album album = new Album(lineName, newArtist, lineID);
	        		artistsList.add(newArtist);
	        		newArtist.add(album);
	        		dates.get(indexOfDate).getOnThisDay().add(album);
	        		dates.get(indexOfDate).sortOTD();
	        		//Testing:
//	        		System.out.println(dates.get(indexOfDate).getOnThisDay());
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
	
	
	
	
	
	
	
//	public static void parse(File wholeFile, ArrayList<Artist> artistsList, int albumTracker, ArrayList<Date> dates) {
//		int j = 1;
//		try {
//			Scanner scanWhole = new Scanner(wholeFile);
//			
//			while (scanWhole.hasNext()) {
//				AlbumCatalog.readLine(j, wholeFile, artistsList, albumTracker, dates);
//				j++;
//			}
//		} 
//		catch (FileNotFoundException e) {
//			System.out.println("File not found.");
//		}
//	}
//	
//	public static void readLine(int i, File f, ArrayList<Artist> artistsList, int albumTracker, ArrayList<Date> dates) {
//		if(i < 1) {
//			throw new IndexOutOfBoundsException();
//		}
//		
//		try {
//
//	        Scanner scan = new Scanner(f);
//	        
//	        for(int j = 0; j < i - 1; j++) {
//	        	scan.nextLine();
//	        }
//	        
//	        String line = scan.nextLine();
//	        String lineName = line.substring(line.indexOf("OPNM")+5,line.indexOf("OPRT"));
//	        String lineArtist = line.substring(line.indexOf("OPRT")+5,line.indexOf("OPID"));
//	        String lineID = line.substring(line.indexOf("OPID"+5), line.indexOf("OPTX"));
//	        String lineArtistCode = line.substring(line.indexOf("OPID")+5,line.indexOf("OPID")+9);
//	        String lineMonth = line.substring(line.indexOf("OPID")+9,line.indexOf("OPID")+11);
//	        String lineDate = line.substring(line.indexOf("OPID")+11,line.indexOf("OPID")+13);
//	        String lineYear = line.substring(line.indexOf("OPID")+13,line.indexOf("OPID")+17);
//	        String lineRating = line.substring(line.indexOf("OPID")+17,line.indexOf("OPID")+19);
//	        String lineMainGenre = line.substring(line.indexOf("OPID")+19,line.indexOf("OPTX"));
//	        int indexOfDate = ((Integer.parseInt(lineMonth) - 1) * 31) + Integer.parseInt(lineDate) - 1;
//	        
//	        boolean isNewArtist = true;
//	        Artist aTemp = null;
//	        for(Artist a : artistsList) {
//	        	if(lineID.substring(0, 3).equals(a.getArtistCode())){
//	        		isNewArtist = false;
//	        		aTemp = a;
//	        	}
//	        }
//	        if(!isNewArtist) {
//	        	Album album = new Album(lineName, aTemp, lineID);
//	        	aTemp.add(album);
//	        	dates.get(indexOfDate).add(album);
//	        	albumTracker++;
//	        }
//	        else {
//	        	Artist newArtist = new Artist(lineArtist, lineID.substring(0,3));
//	        	Album album = new Album(lineName, newArtist, lineID);
//	        	artistsList.add(newArtist);
//	        	newArtist.add(album);
//	        	dates.get(indexOfDate).add(album);
//	        	albumTracker++;
//	        }	        
//	        
//	        scan.close();
//	    } 
//	    catch (FileNotFoundException e) {
//	        e.printStackTrace();
//	        System.out.println("File not found.");
//	    }
//	}
	
}
