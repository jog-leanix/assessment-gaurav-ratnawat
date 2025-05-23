package com.gridgame

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers
import org.hamcrest.Matchers.everyItem
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@QuarkusTest
class GridControllerTest {

    @Nested
    inner class CreateGrid {

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

        @Test
        fun `should create grid with default values when no parameters provided`() {
            given()
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("rows", `is`(50))
                .body("columns", `is`(50))
                .body("cells.size()", `is`(2500))
        }

        @Test
        fun `should create grid with custom rows only`() {
            given()
                .queryParam("rows", 5)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .body("rows", `is`(5))
                .body("columns", `is`(50))
                .body("cells.size()", `is`(250))
        }

        @Test
        fun `should create grid with custom columns only`() {
            given()
                .queryParam("columns", 5)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .body("rows", `is`(50))
                .body("columns", `is`(5))
                .body("cells.size()", `is`(250))
        }

        @Test
        fun `should verify all cells are initialized with zero`() {
            given()
                .queryParam("rows", 2)
                .queryParam("columns", 2)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .body("cells.value", `is`(listOf(0, 0, 0, 0)))
        }
    }

    @Nested
    inner class GetAllGrids {

        @Test
        fun `should return all grids`() {
            createTestGrid(3, 3)
            createTestGrid(4, 4)
            given()
                .`when`()
                .get("/grid")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.greaterThan(0))
        }

        @Test
        fun `should return correct response structure`() {
            createTestGrid(3, 3)
            createTestGrid(4, 4)
            given()
                .`when`()
                .get("/grid")
                .then()
                .statusCode(200)
                .statusCode(200)
                .body("rows", everyItem(Matchers.greaterThan(0)))
                .body("columns", everyItem(Matchers.greaterThan(0)))
                .body("cells", everyItem(hasSize<Int>(Matchers.greaterThan(0))))
        }

        private fun createTestGrid(rows: Int, columns: Int) {
            given()
                .queryParam("rows", rows)
                .queryParam("columns", columns)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
        }

    }

    @Nested
    inner class GetGridById {

        private var existingGridId: Long = 0

        @Test
        fun `should return grid when exists`() {
            // Given - create a grid first
            val response = given()
                .queryParam("rows", 3)
                .queryParam("columns", 3)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .extract()
                .response()

            existingGridId = response.path("id")

            // When - get the created grid
            given()
                .`when`()
                .get("/grid/${existingGridId}")
                .then()
                .statusCode(200)
                .body("id", `is`(existingGridId.toInt()))
                .body("rows", `is`(3))
                .body("columns", `is`(3))
                .body("cells.size()", `is`(9))
                .body("cells.value", everyItem(`is`(0)))
        }

        @Test
        fun `should return 404 when grid does not exist`() {
            given()
                .`when`()
                .get("/grid/999999")
                .then()
                .statusCode(404)
        }
    }

    @Nested
    inner class ClickCell {
        private var gridId: Long = 0

        @Test
        fun `should increment values in row and column on valid click`() {
            // Create a test grid first
            val response = given()
                .queryParam("rows", 3)
                .queryParam("columns", 3)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .extract()
                .response()

            gridId = response.path("id")

            // Perform click
            given()
                .queryParam("row", 1)
                .queryParam("column", 1)
                .`when`()
                .put("/grid/$gridId/click")
                .then()
                .log().all()
                .statusCode(200)
                .body("cells.find { it.row == 1 && it.column == 1 }.value", `is`(1)) // Intersection gets incremented
                .body("cells.findAll { it.row == 1 && it.column != 1 }.value", everyItem(`is`(1))) // Same row
                .body("cells.findAll { it.column == 1 && it.row != 1 }.value", everyItem(`is`(1))) // Same column
                .body("cells.findAll { it.row != 1 && it.column != 1 }.value", everyItem(`is`(0))) // Other cells
        }

        @Test
        fun `should return 400 when row parameter is missing`() {
            given()
                .queryParam("column", 1)
                .`when`()
                .put("/grid/$gridId/click")
                .then()
                .statusCode(400)
                .body("message", equalTo("Row and column parameters are required"))
        }

        @Test
        fun `should return 400 when column parameter is missing`() {
            given()
                .queryParam("row", 1)
                .`when`()
                .put("/grid/$gridId/click")
                .then()
                .statusCode(400)
                .body("message", equalTo("Row and column parameters are required"))
        }

        @Test
        fun `should handle grid not found exception`() {
            given()
                .queryParam("row", 1)
                .queryParam("column", 1)
                .`when`()
                .put("/grid/99999/click")
                .then()
                .statusCode(404)
                .body("message", containsString("Grid not found"))
        }

        @Test
        fun `should handle invalid cell click with out of bounds coordinates`() {
            val response = given()
                .queryParam("rows", 3)
                .queryParam("columns", 3)
                .`when`()
                .post("/grid")
                .then()
                .statusCode(201)
                .extract()
                .response()

            gridId = response.path("id")
            given()
                .queryParam("row", 10)
                .queryParam("column", 10)
                .`when`()
                .put("/grid/$gridId/click")
                .then()
                .statusCode(400)
                .body("message", containsString("Client clicked an invalid cell position: row=10, column=10 for grid " +
                    " = $gridId"))
        }
    }
}
