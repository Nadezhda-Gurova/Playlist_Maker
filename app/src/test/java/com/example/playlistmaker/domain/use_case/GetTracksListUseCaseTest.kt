package com.example.playlistmaker.domain.use_case

import androidx.core.util.Consumer
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.search.domain.interactor.GetTracksListInteractor
import com.example.playlistmaker.util.LoadingState

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class GetTracksListUseCaseTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun execute() {
        val givenResult = LoadingState.Success(emptyList<Track>())
        val tracksRepositoryStub = object : TracksRepository {
            override fun getTracks(track: String) = givenResult
        }


        val case = GetTracksListInteractor(
            tracksRepository = tracksRepositoryStub,
            executorService = ExecutorServiceStub()
        )

        class ConsumerStub : Consumer<LoadingState<List<Track>>> {
            lateinit var accepted: LoadingState<List<Track>>
            override fun accept(t: LoadingState<List<Track>>) {
               accepted = t
                
            }
        }

        val consumer = ConsumerStub()

        case.execute("Sample Name", consumer)
        assert(givenResult === consumer.accepted)
    }

    class ExecutorServiceStub: ExecutorService{
        override fun execute(command: Runnable) {
            command.run()
        }

        override fun shutdown() {
            TODO("Not yet implemented")
        }

        override fun shutdownNow(): MutableList<Runnable> {
            TODO("Not yet implemented")
        }

        override fun isShutdown(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isTerminated(): Boolean {
            TODO("Not yet implemented")
        }

        override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> submit(task: Callable<T>?): Future<T> {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> submit(task: Runnable?, result: T): Future<T> {
            TODO("Not yet implemented")
        }

        override fun submit(task: Runnable?): Future<*> {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> invokeAll(tasks: MutableCollection<out Callable<T>>?): MutableList<Future<T>> {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> invokeAll(
            tasks: MutableCollection<out Callable<T>>?,
            timeout: Long,
            unit: TimeUnit?
        ): MutableList<Future<T>> {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> invokeAny(tasks: MutableCollection<out Callable<T>>?): T {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> invokeAny(
            tasks: MutableCollection<out Callable<T>>?,
            timeout: Long,
            unit: TimeUnit?
        ): T {
            TODO("Not yet implemented")
        }
    }
}