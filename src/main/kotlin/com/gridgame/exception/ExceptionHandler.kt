package com.gridgame.exception

import com.gridgame.service.GridNotFoundException
import com.gridgame.service.InvalidCellClickException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class GridNotFoundExceptionMapper : ExceptionMapper<GridNotFoundException> {

    override fun toResponse(exception: GridNotFoundException): Response {
        return Response.status(Response.Status.NOT_FOUND)
            .entity(mapOf("message" to exception.message))
            .build()
    }
}

@Provider
class InvalidCellClickExceptionMapper : ExceptionMapper<InvalidCellClickException> {

    override fun toResponse(exception: InvalidCellClickException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(mapOf("message" to exception.message))
            .build()
    }
}

@Provider
class GeneralExceptionMapper : ExceptionMapper<Exception> {

    override fun toResponse(exception: Exception): Response {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(mapOf("message" to "An unexpected error occurred"))
            .build()
    }
}
