import java.util.ArrayList;

public class Date {
	public String date;//MMDDYYYY
	ArrayList<Album> onThisDay = new ArrayList<Album>();
	
	public Date(String dateIn) {
		date = dateIn;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMMDD() {
		return date.substring(0,4);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Album> getOnThisDay() {
		return onThisDay;
	}
	
	public String getOTDString() {
		String out = "";
		int num = 1;
		for(Album a : onThisDay) {
//			out = out+"\n"+a;
			out = out+"\n["+num+"] "+a;
		}
		if(out.length() > 1) {
			return out.substring(1);
		}
		else {
			return "...";
		}
	}

	public void setOnThisDay(ArrayList<Album> onThisDay) {
		this.onThisDay = onThisDay;
	}
	
	public void sortOTD() {
		for(int i = 0; i < onThisDay.size(); i++) {
			for(int j = i+1; j < onThisDay.size(); j++) {
				if(onThisDay.get(i).getArtist().compareTo(onThisDay.get(j).getArtist()) > 0) {
					Album temp = onThisDay.get(i);
					onThisDay.set(i, onThisDay.set(j, temp));
				}
				else if(onThisDay.get(i).getArtist().compareTo(onThisDay.get(j).getArtist()) == 0) {
					if(Integer.parseInt(onThisDay.get(i).getYear()) > Integer.parseInt(onThisDay.get(j).getYear())) {
						Album temp = onThisDay.get(i);
						onThisDay.set(i, onThisDay.set(j, temp));
					}
					else if(Integer.parseInt(onThisDay.get(i).getYear()) == Integer.parseInt(onThisDay.get(j).getYear())
							&& Integer.parseInt(onThisDay.get(i).getMonth()) > Integer.parseInt(onThisDay.get(j).getMonth())) {
						Album temp = onThisDay.get(i);
						onThisDay.set(i, onThisDay.set(j, temp));
					}
					else if(Integer.parseInt(onThisDay.get(i).getYear()) == Integer.parseInt(onThisDay.get(j).getYear())
							&& Integer.parseInt(onThisDay.get(i).getMonth()) == Integer.parseInt(onThisDay.get(j).getMonth())
							&& Integer.parseInt(onThisDay.get(i).getDate()) > Integer.parseInt(onThisDay.get(j).getDate())) {
						Album temp = onThisDay.get(i);
						onThisDay.set(i, onThisDay.set(j, temp));
					}
				}
			}
		}
	}
	
	public String toString() {
		return date.substring(0, 2)+" "+date.substring(2,4)+" "+date.substring(4);
	}
}
