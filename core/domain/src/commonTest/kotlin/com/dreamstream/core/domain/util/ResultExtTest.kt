package com.dreamstream.core.domain.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import com.dreamstream.core.domain.extensions.asEmptyResult
import com.dreamstream.core.domain.extensions.map
import com.dreamstream.core.domain.extensions.onFailure
import com.dreamstream.core.domain.extensions.onSuccess
import kotlin.test.Test

private enum class TestError : Error { BOOM }

class ResultExtTest {

    @Test
    fun map_transformsSuccessPayload() {
        val result: Result<Int, TestError> = Result.Success(2)

        val mapped = result.map { it * 5 }

        assertThat(mapped).isEqualTo(Result.Success(10))
    }

    @Test
    fun map_propagatesErrorUnchanged() {
        val result: Result<Int, TestError> = Result.Error(TestError.BOOM)

        val mapped = result.map { it * 5 }

        assertThat(mapped).isInstanceOf(Result.Error::class)
        assertThat((mapped as Result.Error).error).isEqualTo(TestError.BOOM)
    }

    @Test
    fun onSuccess_runsActionOnlyOnSuccessAndReturnsSelf() {
        val success: Result<Int, TestError> = Result.Success(1)
        val failure: Result<Int, TestError> = Result.Error(TestError.BOOM)
        var hits = 0

        val chained = success.onSuccess { hits++ }
        failure.onSuccess { hits++ }

        assertThat(hits).isEqualTo(1)
        assertThat(chained).isSameInstanceAs(success)
    }

    @Test
    fun onFailure_runsActionOnlyOnFailureAndReturnsSelf() {
        val success: Result<Int, TestError> = Result.Success(1)
        val failure: Result<Int, TestError> = Result.Error(TestError.BOOM)
        var captured: TestError? = null

        success.onFailure { captured = it }
        val chained = failure.onFailure { captured = it }

        assertThat(captured).isEqualTo(TestError.BOOM)
        assertThat(chained).isSameInstanceAs(failure)
    }

    @Test
    fun asEmptyResult_discardsSuccessPayload() {
        val result: Result<Int, TestError> = Result.Success(42)

        val empty: EmptyResult<TestError> = result.asEmptyResult()

        assertThat(empty is Result.Success).isTrue()
    }
}
