package com.dreamstream.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) {
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    fun teardown() {
        Dispatchers.resetMain()
    }
}

