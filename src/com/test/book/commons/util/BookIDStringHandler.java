package com.test.book.commons.util;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookIDStringHandler implements ResultSetHandler {
    @Override
    public Object handle(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? resultSet.getString("id") : UUID.randomUUID().toString();
    }
}
