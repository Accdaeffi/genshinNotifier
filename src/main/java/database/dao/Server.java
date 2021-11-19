package database.dao;

import java.time.ZoneId;
import java.util.TimeZone;

public enum Server {
	EU {
		@Override
		public String toRussian() {
			return "Европа";
		}

		@Override
		public TimeZone getTimezone() {
			// Server time GMT+1, Domain restart time - 4:00 at server time
			// So in GMT-3 timezone domain get changed at 0:00
			final String TIME_OFFSET = "-3"; 
			
			return TimeZone.getTimeZone(ZoneId.of(TIME_OFFSET)); 
		}

		@Override
		public String toString() {
			return "eu";
		}
	},
	US {
		@Override
		public String toRussian() {
			return "Америка";
		}

		@Override
		public TimeZone getTimezone() {
			// Server time GMT-5, Domain restart time - 4:00 at server time
			// So in GMT-9 timezone domain get changed at 0:00
			final String TIME_OFFSET = "-9"; 
			
			return TimeZone.getTimeZone(ZoneId.of(TIME_OFFSET)); 
		}

		@Override
		public String toString() {
			return "us";
		}
	},
	AS {
		@Override
		public String toRussian() {
			return "Азия";
		}

		@Override
		public TimeZone getTimezone() {
			// Server time GMT+8, Domain restart time - 4:00 at server time
			// So in GMT+4 timezone domain get changed at 0:00
			final String TIME_OFFSET = "+4"; 
			
			return TimeZone.getTimeZone(ZoneId.of(TIME_OFFSET)); 
		}
		
		@Override
		public String toString() {
			return "as";
		}
	};
	
	public abstract String toRussian();
	
	public abstract TimeZone getTimezone();
	
	public abstract String toString();

	public static Server fromString(String server) {
		Server result;
		
		switch (server) {
			case "eu": result = Server.EU; break;
			case "as": result = Server.AS; break;
			case "us": result = Server.US; break;
			default: throw new IllegalArgumentException();
		}
		
		return result;
	}
	
}
