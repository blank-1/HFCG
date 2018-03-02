package com.external.yongyou.datasource;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

import org.apache.xml.security.Init;

import com.mchange.v2.c3p0.PooledDataSource;
import com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource;


public class NullDataSource   implements PooledDataSource,Referenceable , Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -5961521663010159580L;
	
	public NullDataSource(){}
	
	protected String driverClass ;
	
	protected String jdbcUrl ;
	
	protected String user ;
	
	protected String password ;
	

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null ;
	}
	
	protected void initializeNamedConfig(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reference getReference() throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection getAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataSourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getEffectivePropertyCycle(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEffectivePropertyCycleDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getIdentityToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastAcquisitionFailure(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastAcquisitionFailureDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastCheckinFailure(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastCheckinFailureDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastCheckoutFailure(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastCheckoutFailureDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastConnectionTestFailure(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastConnectionTestFailureDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastIdleTestFailure(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Throwable getLastIdleTestFailureDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumBusyConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumBusyConnections(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumBusyConnectionsAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumBusyConnectionsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumConnections(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumConnectionsAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumConnectionsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumFailedCheckinsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumFailedCheckoutsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumFailedIdleTestsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumHelperThreads() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumIdleConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumIdleConnections(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumIdleConnectionsAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumIdleConnectionsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumThreadsAwaitingCheckout(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumThreadsAwaitingCheckoutDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUnclosedOrphanedConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUnclosedOrphanedConnections(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUnclosedOrphanedConnectionsDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUserPools() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getStartTimeMillisDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumCheckedOut(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumCheckedOutDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumCheckedOutStatementsAllUsers()
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumConnectionsWithCachedStatements(String arg0,
			String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumConnectionsWithCachedStatementsAllUsers()
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumConnectionsWithCachedStatementsDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumStatements(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumStatementsAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatementCacheNumStatementsDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getThreadPoolNumActiveThreads() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getThreadPoolNumIdleThreads() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getThreadPoolNumTasksPending() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getThreadPoolSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getUpTimeMillisDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void hardReset() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String sampleLastAcquisitionFailureStackTrace(String arg0,
			String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastAcquisitionFailureStackTraceDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastCheckinFailureStackTrace(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastCheckinFailureStackTraceDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastCheckoutFailureStackTrace(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastCheckoutFailureStackTraceDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastConnectionTestFailureStackTrace(String arg0,
			String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastConnectionTestFailureStackTraceDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastIdleTestFailureStackTrace(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleLastIdleTestFailureStackTraceDefaultUser()
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleStatementCacheStatus(String arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleStatementCacheStatusDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleThreadPoolStackTraces() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sampleThreadPoolStatus() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSourceName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void softReset(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void softResetAllUsers() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void softResetDefaultUser() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
