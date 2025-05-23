package com.gridgame

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class GridControllerTest {

    private var gridId: Long = 0

    @Test
    fun `should create grid and return 201`() {
        val response = given()
            .queryParam("rows", 10)
            .queryParam("columns", 10)
            .`when`()
            .post("/grid")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("rows", `is`(10))
            .body("columns", `is`(10))
            .body("cells.size()", `is`(100))
            .extract()
            .response()

        gridId = response.path("id")
    }

}
