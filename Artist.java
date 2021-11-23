import java.util.ArrayList;

public class Artist {
	String name;
	ArrayList<Album> discography = new ArrayList<Album>();
	String artistCode;
	ArrayList<String> genres = new ArrayList<String>();
	
	public Artist(String nameIn, String artistCodeIn) {
		name = nameIn;
		artistCode = artistCodeIn;
	}
	
	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	public ArrayList<Album> getDiscography() {
		return discography;
	}
	
	public String getDiscographyString() {
		String out = "";
		int num = 1;
		for(Album a : discography) {
			out = out+"\n["+num+"] "+a;
			num++;
		}
		return out.substring(1);
	}

	public void setDiscography(ArrayList<Album> discography) {
		this.discography = discography;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtistCode() {
		return artistCode;
	}

	public void setArtistCode(String artistCode) {
		this.artistCode = artistCode;
	}

	public void add(Album album) {
		discography.add(album);
//		for(String str : album.getGenres()) {
//			boolean newGenre = true;
//			for(String chk : genres) {
//				if (str.equals(chk)){
//					newGenre = false;
//				}
//			}
//			if(newGenre) {
//				genres.add(str);
//			}
//		}
	}
	
	public void sortDiscography() {//Sorts by date
		for(int i = 0; i < discography.size(); i++) {
			for(int j = i+1; j < discography.size(); j++) {
				if(Integer.parseInt(discography.get(i).getYear()) > Integer.parseInt(discography.get(j).getYear())) {
					Album temp = discography.get(i);
					discography.set(i, discography.set(j, temp));
				}
				else if(Integer.parseInt(discography.get(i).getYear()) == Integer.parseInt(discography.get(j).getYear())
						&& Integer.parseInt(discography.get(i).getMonth()) > Integer.parseInt(discography.get(j).getMonth())) {
					Album temp = discography.get(i);
					discography.set(i, discography.set(j, temp));
				}
				else if(Integer.parseInt(discography.get(i).getYear()) == Integer.parseInt(discography.get(j).getYear())
						&& Integer.parseInt(discography.get(i).getMonth()) == Integer.parseInt(discography.get(j).getMonth())
						&& Integer.parseInt(discography.get(i).getDate()) > Integer.parseInt(discography.get(j).getDate())) {
					Album temp = discography.get(i);
					discography.set(i, discography.set(j, temp));
				}
			}
		}
	}
	
	public String toString() {
		return name;
	}
	
	public String toStringCode() {
		return name+" "+artistCode;
	}
}
