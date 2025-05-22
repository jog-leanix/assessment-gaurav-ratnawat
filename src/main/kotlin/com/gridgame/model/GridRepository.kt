package com.gridgame.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository for managing Grid entities.
 *
 * This class extends the PanacheRepository interface, which provides basic CRUD operations
 * for the Grid entity.
 */
@ApplicationScoped
class GridRepository : PanacheRepository<Grid> {
    // Repository methods will be available here
}
