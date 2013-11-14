package reservation;

import java.sql.Time;

public class ReservationObject {
		
	private int id;
	private String PartyName;
	private Time ReservationTime;
	private int PartySize;
	private String status;
	private String SeatType;
	private Time SeatedTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Time getSeatedTime() {
		return SeatedTime;
	}

	public void setSeatedTime(Time seatedTime) {
		SeatedTime = seatedTime;
	}

	public ReservationObject(String name, String time, String size) {
		this.PartyName = name;
		this.ReservationTime = java.sql.Time.valueOf(time);
		this.PartySize = Integer.parseInt(size);
		this.status = "waiting";
	}
	
	public ReservationObject(int id, String name, String time, String seatType, 
			String size, String status, String seatedTime) {
		this.PartyName = name;
		
		// check for null values from database, because java in it's wisdom
		// will try to call .length() on null and result in the clubbing of
		// baby seals.
		if (time != null) {
			if ((time.length() == 5) && (time != null)) 
				time += ":00";
		}
		
		this.id = id;
		this.ReservationTime = Time.valueOf(time);
		this.SeatType = seatType;
		this.PartySize = Integer.parseInt(size);
		this.status = status;
		if (seatedTime != null) {
			if (seatedTime.length() == 5)
				time += ":00";
			this.SeatedTime = Time.valueOf(seatedTime);
		}
	}
	
	public String getPartyName() {
		return PartyName;
	}
	
	public void setPartyName(String partyName) {
		PartyName = partyName;
	}
	
	public Time getReservationTime() {
		return ReservationTime;
	}
	
	public void setReservationTime(Time reservationTime) {
		ReservationTime = reservationTime;
	}

	public int getPartySize() {
		return PartySize;
	}

	public void setPartySize(int partySize) {
		PartySize = partySize;
	}

	public String getSeatType() {
		return SeatType;
	}

	public void setSeatType(String seatType) {
		SeatType = seatType;
	}
	
	

}
