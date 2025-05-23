package com.gridgame

import com.gridgame.service.GridService
import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/grid")
@Produces(MediaType.APPLICATION_JSON)
class GridController {

    @Inject
    lateinit var gridService: GridService

    @POST
    fun createGrid(
        @QueryParam("rows") rows: Int? = null,
        @QueryParam("columns") columns: Int? = null
    ): Response {
        val createdGrid = gridService.initializeGrid(rows ?: DEFAULT_SIZE, columns ?: DEFAULT_SIZE)
        return Response.status(Response.Status.CREATED).entity(createdGrid).build()
    }

    companion object {

        private const val DEFAULT_SIZE = 50
    }

}
