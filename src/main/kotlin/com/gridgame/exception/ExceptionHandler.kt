package com.gridgame.exception

import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.jboss.logging.Logger

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
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {

    private val logger = Logger.getLogger(IllegalArgumentExceptionMapper::class.java)
    override fun toResponse(exception: IllegalArgumentException): Response {
        logger.error("Illegal argument exception occurred", exception)
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(mapOf("message" to exception.message))
            .build()
    }
}

@Provider
class NotFoundExceptionMapper : ExceptionMapper<NotFoundException> {

    override fun toResponse(exception: NotFoundException): Response {
        return Response.status(Response.Status.NOT_FOUND)
            .entity(mapOf("message" to exception.message))
            .build()
    }
}

@Provider
class GeneralExceptionMapper : ExceptionMapper<Exception> {

    private val logger = Logger.getLogger(GeneralExceptionMapper::class.java)
    override fun toResponse(exception: Exception): Response {
        logger.error("Unexpected error occurred", exception)
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(mapOf("message" to "An unexpected error occurred"))
            .build()
    }
}
