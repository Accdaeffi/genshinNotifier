package ru.dnoskov.main;

import ru.dnoskov.database.sources.DataSource;

public final class Properties {
	
	private static DataSource dataSource;
	
	public static void setDataSource(DataSource dataSource) {
		if (Properties.dataSource == null) {
			Properties.dataSource = dataSource;
		}
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}
}
