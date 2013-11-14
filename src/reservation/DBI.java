package reservation;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.almworks.sqlite4java.*;
import com.almworks.sqlite4java.SQLiteBusyException;


public class DBI {
	
	public SQLiteConnection db = null;
	CircularLinkedQueue<ReservationObject> myQueue = null;
	
	public DBI() {
		db = new SQLiteConnection(new File("/tmp/database"));
		try {
			db.open(true);
			this.CreateTables();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} 
		
		this.myQueue = new CircularLinkedQueue<ReservationObject>();
	
	}
	
	public void close() {
		db.dispose();
	}
	
	private void CreateTables() throws SQLiteException {
		
		SQLiteStatement st;
		try {
			st = db.prepare("SELECT key FROM config;");
			while(st.step());
			st.dispose();
		
		} catch (SQLiteException e) {
			try {
				System.err.println("WARN: Database doesn't seem to be initialized");
				st = db.prepare("CREATE TABLE IF NOT EXISTS "
						+ "reservations(id integer primary key, name text, reserved_time text, "
						+ "seat text, party int, status text, seated_time text);");
				while(st.step());
				st.dispose();
				
				st = db.prepare("CREATE TABLE IF NOT EXISTS "
						+ "config(\"key\" text primary key, \"value\" text); ");
				while(st.step());
				st.dispose();
				
				this.SetDefaults();
			} catch (SQLiteBusyException f) {
					System.out.print("Sorry, this table is unavailable.");
			}
		}
		
	}
	
	private void InsertConfigEntry(String key, String value)  {
		try {
			SQLiteStatement st = db.prepare("INSERT INTO config(\"key\", \"value\") "
					+ "VALUES ('" + key + "', '" + value + "');");
			while(st.step());
			st.dispose();
		} catch (SQLiteException e) {
			System.err.println("Defaults already set");
		}
	}
	
	private void SetDefaults() {
		this.InsertConfigEntry("windows_max", "20");
		this.InsertConfigEntry("booths_max", "20");
		this.InsertConfigEntry("bar_max", "10");
		this.InsertConfigEntry("dining_time", "45");
		
	}
	
	public String GetConfigEntry(String key) {
		SQLiteStatement st = null;
		try {
			st = db.prepare("SELECT \"value\" FROM \"config\" WHERE \"key\" = ?;");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		String value = null;
		try {
			st.bind(1, key);
			while (st.step()) {
				value = st.columnString(0); 
			}
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		return value;
	}
	
	public class FreeTables {
		public int window;
		public int booth;
		public int bar;
		public FreeTables(int window, int booth, int bar) {
			this.window = window;
			this.window = booth;
			this.bar = bar;
		}
	}
	
	public FreeTables GetFreeTables(String time) {
		return new FreeTables(this.GetFreeWindowSeats(time), 
							   this.GetFreeBoothSeats(time),
							   this.GetFreeBarSeats(time));
	}
	
	public int GetFreeWindowSeats(String time) {
		SQLiteStatement st = null;
		try {
			float inter = Float.parseFloat(this.GetConfigEntry("dining_time")) / 2;
			String interval = Float.toString(inter);
			st = db.prepare("SELECT SUM(PARTY) FROM reservations "
					+ "WHERE TIME(reserved_time) >= TIME(?, '-" + interval + " minutes') "
					+ "AND   TIME(reserved_time) <= TIME(?, '+" + interval + " minutes') "
					+ "AND seat = 'window';");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		int value = 0;
		try {
			st.bind(1, time);
			st.bind(2, time);
			while (st.step()) {
				value = st.columnInt(0); 
			}
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		
		return Integer.parseInt(this.GetConfigEntry("windows_max")) - value;
	}
	
	public int GetFreeBoothSeats(String time) {
		SQLiteStatement st = null;
		try {
			float inter = Float.parseFloat(this.GetConfigEntry("dining_time")) / 2;
			String interval = Float.toString(inter);
			st = db.prepare("SELECT SUM(PARTY) FROM reservations "
					+ "WHERE TIME(reserved_time) >= TIME(?, '-" + interval + " minutes') "
					+ "AND   TIME(reserved_time) <= TIME(?, '+" + interval + " minutes') "
					+ "AND seat = 'booth';");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		int value = 0;
		try {
			st.bind(1, time);
			st.bind(2, time);
			while (st.step()) {
				value = st.columnInt(0); 
			}
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		return Integer.parseInt(this.GetConfigEntry("booths_max")) - value;
	}
	
	public int GetFreeBarSeats(String time) {
		SQLiteStatement st = null;
		try {
			float inter = Float.parseFloat(this.GetConfigEntry("dining_time")) / 2;
			String interval = Float.toString(inter);
			st = db.prepare("SELECT SUM(PARTY) FROM reservations "
					+ "WHERE TIME(reserved_time) >= TIME(?, '-" + interval + " minutes') "
					+ "AND   TIME(reserved_time) <= TIME(?, '+" + interval + " minutes') "
					+ "AND seat = 'bar';");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		int value = 0;
		try {
			st.bind(1, time);
			st.bind(2, time);
			while (st.step()) {
				value = st.columnInt(0); 
			}
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		return Integer.parseInt(this.GetConfigEntry("bar_max")) - value;
	}
	
	public int GetWaitTime(String time) {
		//SELECT COUNT(id) FROM reservations WHERE status='waiting' OR status='seated';
		return 0;
	}
	
	public void MarkSeated(String id) {
		SQLiteStatement st = null;
		try {
			st = db.prepare("UPDATE reservations SET status = 'seated', " +
					"seated_time = TIME('now') WHERE id = ?;");
			st.bind(1, id);
			while(st.step());
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	
	public void MarkPaid(String id) {
		SQLiteStatement st = null;
		try {
			st = db.prepare("UPDATE reservations SET status = 'paid' WHERE id = ?");
			st.bind(1, id);
			while(st.step());
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	public int MakeReservation(String name, String time, 
			String seatPreference, int partySize) {
		int ret = -1;
		SQLiteStatement st = null;
		try {
			//name text, reserved_time text, seat text, party int, status text, seated_time text
			st = db.prepare("INSERT INTO reservations(name, reserved_time, "
					+ "seat, party, status) VALUES (?, ?, ?, ?, 'waiting');");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		
		try {
			st.bind(1, name);
			st.bind(2, time.toString());
			st.bind(3, seatPreference);
			st.bind(4, partySize);
			while (st.step());
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		
		// get and return the resulting ID:
		try {
			st = db.prepare("SELECT id FROM reservations WHERE name = ? AND "
					+ "reserved_time = ? AND seat = ? AND party = ? AND "
					+ "status = 'waiting';");
			st.bind(1, name);
			st.bind(1, name);
			st.bind(2, time.toString());
			st.bind(3, seatPreference);
			st.bind(4, partySize);
			while (st.step()) {
				ret = st.columnInt(0);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public ReservationObject GetReservation(String ref) {
		SQLiteStatement st = null;
		ReservationObject ret = null;
		try {
			st = db.prepare("SELECT id, name, reserved_time, seat, party, "
					+ "status, seated_time FROM \"reservations\" WHERE "
					+ "id = '" + ref + "';");
			
			// read out each row into a ReservationObject
			while (st.step()) {
				int id = st.columnInt(0);
				String partyName = st.columnString(1);
				String reservedTime = st.columnString(2);
				String seatType = st.columnString(3);
				String partySize = st.columnString(4);
				String status = st.columnString(5);
				String seatedTime = st.columnString(6);
				ret = new ReservationObject(id, partyName, reservedTime, 
						seatType, partySize, status, seatedTime);
			}
		} catch (SQLiteException e) {
			System.err.println("Error while fetching reservation queue.");
			e.printStackTrace();

		} finally {
			st.dispose();
		}
		this.myQueue.enqueue(ret);
		return ret;
		
	}
	
	public void CancelReservation(String id) {
		SQLiteStatement st = null;
		try {
			st = db.prepare("DELETE FROM reservations WHERE id = ?;");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		try {
			st.bind(1, id);
			while (st.step());
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st.dispose();
		}
		this.myQueue.remove(id);
	}
	
	public List<ReservationObject> GetSeated() {
		List<ReservationObject> ret = new ArrayList<ReservationObject>();
		SQLiteStatement st = null;
		try {
			st = db.prepare("SELECT id, name, reserved_time, seat, party, "
					+ "status, seated_time FROM \"reservations\" WHERE "
					+ "status == 'seated' ORDER BY seated_time;");
			
			// read out each row into a ReservationObject
			while (st.step()) {
				int id = st.columnInt(0);
				String partyName = st.columnString(1);
				String reservedTime = st.columnString(2);
				String seatType = st.columnString(3);
				String partySize = st.columnString(4);
				String status = st.columnString(5);
				String seatedTime = st.columnString(6);
				ret.add(new ReservationObject(id, partyName, reservedTime, 
						seatType, partySize, status, seatedTime));
			}
		} catch (SQLiteException e) {
			System.err.println("Error while fetching reservation queue.");
			e.printStackTrace();

		} finally {
			st.dispose();
		}
		
		return ret;
	}
	
	public List<ReservationObject> GetWaitingReservations() {
		List<ReservationObject> ret = new ArrayList<ReservationObject>();
		SQLiteStatement st = null;
		try {
			st = db.prepare("SELECT id, name, reserved_time, seat, party, "
					+ "status, seated_time FROM \"reservations\" WHERE "
					+ "status == 'waiting';");
			
			// read out each row into a ReservationObject
			while (st.step()) {
				int id = st.columnInt(0);
				String partyName = st.columnString(1);
				String reservedTime = st.columnString(2);
				String seatType = st.columnString(3);
				String partySize = st.columnString(4);
				String status = st.columnString(5);
				String seatedTime = st.columnString(6);
				ret.add(new ReservationObject(id, partyName, reservedTime, 
						seatType, partySize, status, seatedTime));
			}
		} catch (SQLiteException e) {
			System.err.println("Error while fetching reservation queue.");
			e.printStackTrace();

		} finally {
			st.dispose();
		}
		
		return ret;
	}

}
