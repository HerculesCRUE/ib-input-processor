package es.um.asio.inputprocessor.service.config;

import org.hibernate.dialect.MariaDB53Dialect;

public class CustomDialect extends MariaDB53Dialect {
	
	@Override
	public String getTableTypeString() {			
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

}
