package database.dao;

import java.time.ZoneId;
import java.util.TimeZone;

import lombok.Getter;
import lombok.NonNull;

public enum Server {
	EU ("Европа", TimeZone.getTimeZone(ZoneId.of(ServerTime.EUTimeZone))),
	US ("Америка", TimeZone.getTimeZone(ZoneId.of(ServerTime.USTimeZone))),
	AS ("Азия", TimeZone.getTimeZone(ZoneId.of(ServerTime.ASTimeZone)));
	
	@Getter
	private final String russianName;
	@Getter
	private final TimeZone serverTimeZone;
	
	private Server(String russianName, TimeZone serverTimeZone) {
		this.russianName = russianName;
		this.serverTimeZone = serverTimeZone;
	}

	public static Server fromString(@NonNull String server) {
		return Server.valueOf(server.toUpperCase());
	}
	
	private static class ServerTime {
		// Server time GMT+1, Domain restart time - 4:00 at server time
		// So in GMT-3 timezone domain get changed at 0:00
		static String EUTimeZone = "-3";
		
		// Server time GMT-5, Domain restart time - 4:00 at server time
		// So in GMT-9 timezone domain get changed at 0:00
		static String USTimeZone = "-9";
		
		// Server time GMT-5, Domain restart time - 4:00 at server time
		// So in GMT-9 timezone domain get changed at 0:00
		static String ASTimeZone = "+4";
	}
}
