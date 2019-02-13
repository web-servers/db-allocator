package org.jboss.qa.db;

import org.apache.commons.lang.StringUtils;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
//import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class AllocatorTest {

    @Test
    public void myTest() {
        try {
            Allocator dba = Allocator.getInstance();
            DB db = dba.allocateDB();

            final File[] dbDriver = resolveJdbcDriverPath(dba, db);
            String dbDriverAbsolutePath = dbDriver[0].getAbsolutePath();

//            try {
//                System.out.println("sleeping");
//                TimeUnit.SECONDS.sleep(20);
//                System.out.println("woke up");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            Allocator.executeTestStatement(db, dbDriverAbsolutePath);
            executeTestStatement(dbDriverAbsolutePath, db, dba);

            dba.deallocateDB(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File[] resolveJdbcDriverPath(Allocator dba, DB db) {
        if (dba instanceof DBAllocator || dba instanceof ExternalDBAllocator) {
            return new File[]{new File(db.dbDriverArtifact)};
        } else {
            return Maven.resolver().resolve(db.dbDriverArtifact).withTransitivity().asFile();
        }
    }

    private static void executeTestStatement(final String dbDriverAbsolutePath, final DB db, final Allocator dba) {
        if (dba instanceof PostgreContainerAllocator && StringUtils.isNotBlank(db.heartBeatStatement)) {
            try {
                if (!Allocator.executeTestStatement(db, dbDriverAbsolutePath)) {
                    dba.deallocateDB(db);
                    fail("The database system is not ready to execute statements. Check DB logs, please.");
                }
            } catch (ClassNotFoundException e) {
                dba.deallocateDB(db);
                fail("The class %s cannot be loaded. " + e.getMessage());
            } catch (IllegalAccessException e) {
                dba.deallocateDB(db);
                fail("Dynamic loading of Driver class is probably not possible with this JVM setup. " + e.getMessage());
            } catch (InstantiationException e) {
                dba.deallocateDB(db);
                fail("Dynamic Driver class instantiation failed. " + e.getMessage());
            } catch (SQLException e) {
                dba.deallocateDB(db);
                fail("Driver cannot be used. " + e.getMessage());
            } catch (MalformedURLException e) {
                fail("Driver jar path seems invalid. " + e.getMessage());
            }
        }
    }

}