package bet.astral.unity.database.impl;

import bet.astral.unity.database.Database;
import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;
import bet.astral.unity.database.impl.sql.source.SQLDatabaseSource;
import lombok.Getter;

@Getter
public enum DatabaseType {
	MYSQL(HikariDatabaseSource.class, "com.mysql.cj.jdbc.MysqlDataSource"),
	SQLITE(SQLDatabaseSource.class, "org.sqlite.JDBC"),
	MONGODB(null)
	;

	private final Class<? extends Database> dataSourceClass;
	private final String actualDataSourceClass;

	DatabaseType(Class<? extends Database> dataSourceClass, String actualDataSourceClass) {
		this.dataSourceClass = dataSourceClass;
		this.actualDataSourceClass = actualDataSourceClass;
	}
	DatabaseType(Class<? extends Database> dataSourceClass){
		this.dataSourceClass = dataSourceClass;
		this.actualDataSourceClass = null;
	}
}
