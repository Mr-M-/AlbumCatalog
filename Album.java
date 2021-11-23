
public class Album {
	String name;
	Artist artist;
	String id;//ARTMMDDYYYYRTGEN -- artist, month, date, year, rating, main genre
	//Example: ZEP0112196909ROK
	String[] genres;
	int numTracks = -1;
	int runTime = -1; //In minutes
	
	public Album(String name, Artist artist, String id) {
		super();
		this.name = name;
		this.artist = artist;
		this.id = id;
	}

	public Album(String name, Artist artist, String id, String[] genres) {
		super();
		this.name = name;
		this.artist = artist;
		this.id = id;
		this.genres = genres;
	}

	public Album(String name, Artist artist, String id, String[] genres, int numTracks, int runTime) {
		super();
		this.name = name;
		this.artist = artist;
		this.id = id;
		this.genres = genres;
		this.numTracks = numTracks;
		this.runTime = runTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist.getName();
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public int getNumTracks() {
		return numTracks;
	}

	public void setNumTracks(int numTracks) {
		this.numTracks = numTracks;
	}

	public int getRunTime() {
		return runTime;
	}

	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}
	
	//Special getters and setters
	public String getArtistCode() {
		return id.substring(0, 3);
	}
	
	public void setArtistCode(String str) {
		id = str+id.substring(3);
	}
	
	public String getMonth() {
		return id.substring(3, 5);
	}
	
	public void setMonth(String str) {
		id = id.substring(0,3)+str+id.substring(5);
	}
	
	public String getDate() {
		return id.substring(5,7);
	}
	
	public void setDate(String str) {
		id = id.substring(0,5)+str+id.substring(7);
	}
	
	public String getYear() {
		return id.substring(7,11);
	}
	
	public void setyear(String str) {
		id = id.substring(0,7)+str+id.substring(11);
	}
	
	public String getFullDate() {
		return id.substring(3,11);
	}
	
	public String getRating() {
		if (Integer.parseInt(this.id.substring(11,13)) < 10) {
			return id.substring(12,13);
		}
		else {
			return id.substring(11,13);
		}
	}
	
	public void setRating(String str) {
		id = id.substring(0,11)+str+id.substring(13);
	}
	
	public String getMGCode() {//3letter main genre code
		return id.substring(13);
	}
	
	public String getMainGenre() {
		String gen = this.getMGCode();
		if(gen.equals("ROK")) {
			return name+" is a rock album.";
		}
		else if(gen.equals("MET")) {
			return name+" is a metal album.";
		}
		return "We're not sure what genre this album is.";
	}
	
	public void setMainGenre(String str) {
		id = id.substring(0,13)+str;
	}
	
	public String toString() {
		return name+" ("+this.getYear()+"), "+artist+", "+this.getRating()+"/10.";			
	}
	
}