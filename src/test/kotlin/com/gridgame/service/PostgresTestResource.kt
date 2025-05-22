package com.gridgame.service

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresTestResource : QuarkusTestResourceLifecycleManager {
    private val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))

    override fun start(): Map<String, String> {
        postgres.start()
        return mapOf(
            "quarkus.datasource.jdbc.url" to postgres.jdbcUrl,
            "quarkus.datasource.username" to postgres.username,
            "quarkus.datasource.password" to postgres.password
        )
    }

    override fun stop() {
        postgres.stop()
    }
}
