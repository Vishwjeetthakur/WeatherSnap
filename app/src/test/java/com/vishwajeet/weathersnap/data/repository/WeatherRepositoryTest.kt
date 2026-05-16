package com.vishwajeet.weathersnap.data.repository

import com.vishwajeet.weathersnap.data.local.WeatherDao
import com.vishwajeet.weathersnap.data.remote.WeatherApi
import com.vishwajeet.weathersnap.data.remote.dto.CityDto
import com.vishwajeet.weathersnap.data.remote.dto.CitySearchResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository
    private val api : WeatherApi = mockk()
    private val dao : WeatherDao = mockk()

    @Before
    fun setup() {
        repository = WeatherRepository(api, dao)
    }

    @Test
    fun `getCitySuggestions calls api and returns results` ()= runTest{

        val mockResponse = CitySearchResponse(
            results =
                listOf(CityDto(1, "Jabalpur", "India", "Madhya Pradesh", 23.16, 79.93,))
        )
        coEvery { api.getCitySuggestions("Jabalpur") } returns mockResponse

        val result = repository.getCitySuggestions("Jabalpur")

        assertEquals("Jabalpur",result.results?.first()?.name)
        coVerify(exactly = 1){ api.getCitySuggestions("Jabalpur") }
    }

}