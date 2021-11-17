package main;

import database.sources.DataSource;

public final class Properties {
	
	private static DataSource dataSource;
	
	public static void setDataSource(DataSource dataSource) {
		if (dataSource == null) {
			Properties.dataSource = dataSource;
		}
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
}