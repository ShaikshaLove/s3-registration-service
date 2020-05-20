package io.s3soft.registration.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UserIdGernerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
       long id=502020;
		
		Connection con= session.connection();
		try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT nextval('hibernate_sequence')");
			rs.next();
			id=id+rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

}
