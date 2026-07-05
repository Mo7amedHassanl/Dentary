package com.m7md7sn.dentary.domain.usecase.patient

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPatientsUseCaseTest {

    private lateinit var getPatientsUseCase: GetPatientsUseCase
    private val patientRepository: PatientRepository = mockk()

    private val testPatients = listOf(
        Patient(id = "1", name = "Zack", createdAt = "2023-01-01"),
        Patient(id = "2", name = "Alice", createdAt = "2023-01-02")
    )

    @Before
    fun setUp() {
        getPatientsUseCase = GetPatientsUseCase(patientRepository)
    }

    @Test
    fun `invoke with empty query should return all patients sorted by name`() = runTest {
        // Arrange
        coEvery { patientRepository.getAllPatients() } returns Result.Success(testPatients)

        // Act
        val result = getPatientsUseCase()

        // Assert
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals("Alice", data[0].name)
        assertEquals("Zack", data[1].name)
    }

    @Test
    fun `invoke with sortByRecent true should return patients sorted by date`() = runTest {
        // Arrange
        coEvery { patientRepository.getAllPatients() } returns Result.Success(testPatients)

        // Act
        val result = getPatientsUseCase(sortByRecent = true)

        // Assert
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals("2023-01-02", data[0].createdAt)
        assertEquals("2023-01-01", data[1].createdAt)
    }

    @Test
    fun `invoke with query should call searchPatients`() = runTest {
        // Arrange
        val query = "Alice"
        coEvery { patientRepository.searchPatients(query) } returns Result.Success(listOf(testPatients[1]))

        // Act
        val result = getPatientsUseCase(query = query)

        // Assert
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(1, data.size)
        assertEquals("Alice", data[0].name)
    }
}
