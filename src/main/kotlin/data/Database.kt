package data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource
import kotlin.concurrent.thread


fun createHikariDataSource(): DataSource {
    val databaseUrl = System.getenv("JDBC_DATABASE_URL") ?: error("DATABASE_URL environment variable is not set.")
    val databasePass = System.getenv("DATABASE_PASS") ?: error("DATABASE_PASS environment variable is not set.")
    val databaseUser = System.getenv("DATABASE_USER") ?: error("DATABASE_USER environment variable is not set.")
    val config = HikariConfig().apply {
        jdbcUrl = databaseUrl
        username = databaseUser
        password = databasePass
    }
    val ds = HikariDataSource(config)

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        ds.close()
    })

    // Migrate database
    Flyway.configure().dataSource(ds).load().apply {
        migrate()
    }

    return ds
}
