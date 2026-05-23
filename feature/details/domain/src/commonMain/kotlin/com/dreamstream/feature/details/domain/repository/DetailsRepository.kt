package com.dreamstream.feature.details.domain.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.model.DetailContent

interface DetailsRepository {
    suspend fun getContentDetail(contentId: String): Result<DetailContent, DetailsError>
}
