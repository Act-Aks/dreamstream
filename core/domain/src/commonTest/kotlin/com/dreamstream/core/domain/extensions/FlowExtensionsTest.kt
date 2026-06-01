package com.dreamstream.core.domain.extensions

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.dreamstream.core.domain.util.Error
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

private enum class TestError : Error { MAPPED, OTHER }

class FlowExtensionsTest {

    // -------------------------------------------------------------------------
    // asResult
    // -------------------------------------------------------------------------

    @Test
    fun asResult_emitsSuccessForEachValue() = runTest {
        flowOf(1, 2, 3)
            .asResult { TestError.MAPPED }
            .test {
                assertThat(awaitItem()).isEqualTo(Result.Success(1))
                assertThat(awaitItem()).isEqualTo(Result.Success(2))
                assertThat(awaitItem()).isEqualTo(Result.Success(3))
                awaitComplete()
            }
    }

    @Test
    fun asResult_mapsExceptionToErrorViaOnError() = runTest {
        flow<Int> { throw RuntimeException("boom") }
            .asResult { TestError.MAPPED }
            .test {
                assertThat(awaitItem()).isEqualTo(Result.Error(TestError.MAPPED))
                awaitComplete()
            }
    }

    @Test
    fun asResult_onErrorLambdaReceivesOriginalThrowable() = runTest {
        val cause = IllegalStateException("original")
        var captured: Throwable? = null

        flow<Int> { throw cause }
            .asResult { e -> captured = e; TestError.MAPPED }
            .test { awaitItem(); awaitComplete() }

        assertThat(captured).isEqualTo(cause)
    }

    // -------------------------------------------------------------------------
    // onSuccess
    // -------------------------------------------------------------------------

    @Test
    fun onSuccess_runsActionOnlyForSuccessEmissions() = runTest {
        var hits = 0
        flowOf(
            Result.Success(1),
            Result.Error(TestError.OTHER),
            Result.Success(2),
        )
            .onSuccess { hits++ }
            .test {
                awaitItem(); awaitItem(); awaitItem()
                awaitComplete()
            }
        assertThat(hits).isEqualTo(2)
    }

    @Test
    fun onSuccess_passesOriginalResultDownstream() = runTest {
        val success: Result<Int, TestError> = Result.Success(42)
        flowOf(success)
            .onSuccess { }
            .test {
                assertThat(awaitItem()).isEqualTo(success)
                awaitComplete()
            }
    }

    // -------------------------------------------------------------------------
    // onFailure
    // -------------------------------------------------------------------------

    @Test
    fun onFailure_runsActionOnlyForErrorEmissions() = runTest {
        var hits = 0
        flowOf(
            Result.Success(1),
            Result.Error(TestError.OTHER),
            Result.Success(2),
        )
            .onFailure { hits++ }
            .test {
                awaitItem(); awaitItem(); awaitItem()
                awaitComplete()
            }
        assertThat(hits).isEqualTo(1)
    }

    @Test
    fun onFailure_capturesTypedError() = runTest {
        var captured: TestError? = null
        flowOf(Result.Error(TestError.MAPPED))
            .onFailure { captured = it }
            .test { awaitItem(); awaitComplete() }
        assertThat(captured).isEqualTo(TestError.MAPPED)
    }

    @Test
    fun onFailure_passesOriginalResultDownstream() = runTest {
        val error: Result<Int, TestError> = Result.Error(TestError.OTHER)
        flowOf(error)
            .onFailure { }
            .test {
                assertThat(awaitItem()).isEqualTo(error)
                awaitComplete()
            }
    }

    // -------------------------------------------------------------------------
    // mapResult
    // -------------------------------------------------------------------------

    @Test
    fun mapResult_transformsSuccessPayload() = runTest {
        flowOf(Result.Success(2))
            .mapResult<Int, String, TestError> { it.toString() }
            .test {
                assertThat(awaitItem()).isEqualTo(Result.Success("2"))
                awaitComplete()
            }
    }

    @Test
    fun mapResult_propagatesErrorUnchanged() = runTest {
        flowOf(Result.Error(TestError.MAPPED))
            .mapResult<Int, String, TestError> { it.toString() }
            .test {
                val item = awaitItem()
                assertThat(item).isInstanceOf(Result.Error::class)
                assertThat((item as Result.Error).error).isEqualTo(TestError.MAPPED)
                awaitComplete()
            }
    }

    @Test
    fun mapResult_handlesInterleavedSuccessAndError() = runTest {
        flowOf(
            Result.Success(10),
            Result.Error(TestError.OTHER),
            Result.Success(20),
        )
            .mapResult<Int, Int, TestError> { it * 2 }
            .test {
                assertThat(awaitItem()).isEqualTo(Result.Success(20))
                assertThat(awaitItem()).isEqualTo(Result.Error(TestError.OTHER))
                assertThat(awaitItem()).isEqualTo(Result.Success(40))
                awaitComplete()
            }
    }
}
