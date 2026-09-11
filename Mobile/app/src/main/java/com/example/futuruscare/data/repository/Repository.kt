package com.example.futuruscare.data.repository

import com.example.futuruscare.data.model.Payment
import com.example.futuruscare.data.PaymentDao
import com.example.futuruscare.data.model.Pet
import com.example.futuruscare.data.PetDao
import com.example.futuruscare.data.model.Petsitter
import com.example.futuruscare.data.PetsitterDao
import com.example.futuruscare.data.model.Petsitting
import com.example.futuruscare.data.PetsittingDao
import com.example.futuruscare.data.model.PlanEmployee
import com.example.futuruscare.data.PlanEmployeeDao
import com.example.futuruscare.data.model.Procedure
import com.example.futuruscare.data.ProcedureDao
import com.example.futuruscare.data.model.User
import com.example.futuruscare.data.model.UserAddress
import com.example.futuruscare.data.UserAddressDao
import com.example.futuruscare.data.UserDao

class Repository(
    private val userDao: UserDao,
    private val petDao: PetDao,
    private val userAddressDao: UserAddressDao,
    private val petsitterDao: PetsitterDao,
    private val planEmployeeDao: PlanEmployeeDao,
    private val petsittingDao: PetsittingDao,
    private val paymentDao: PaymentDao,
    private val procedureDao: ProcedureDao
) {

    // User CRUD
    fun getAllUsers(): List<User> = userDao.getAllUsers()
    fun getUserById(id: Long): User? = userDao.getUserById(id)
    fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    fun getUserByCpf(cpf: String): User? = userDao.getUserByCpf(cpf)
    fun insertUser(user: User): Long = userDao.insertUser(user)
    fun updateUser(user: User): Int = userDao.updateUser(user)
    fun deleteUser(user: User): Int = userDao.deleteUser(user)
    fun deleteUserById(id: Long): Int = userDao.deleteUserById(id)

    // Pet CRUD
    fun getAllPets(): List<Pet> = petDao.getAllPets()
    fun getPetById(id: Long): Pet? = petDao.getPetById(id)
    fun getPetsByUserId(userId: Long): List<Pet> = petDao.getPetsByUserId(userId)
    fun insertPet(pet: Pet): Long = petDao.insertPet(pet)
    fun updatePet(pet: Pet): Int = petDao.updatePet(pet)
    fun deletePet(pet: Pet): Int = petDao.deletePet(pet)
    fun deletePetById(id: Long): Int = petDao.deletePetById(id)

    // UserAddress CRUD
    fun getAddressByUserId(userId: Long): UserAddress? = userAddressDao.getAddressByUserId(userId)
    fun insertAddress(address: UserAddress): Long = userAddressDao.insertAddress(address)
    fun updateAddress(address: UserAddress): Int = userAddressDao.updateAddress(address)
    fun deleteAddress(address: UserAddress): Int = userAddressDao.deleteAddress(address)

    // Petsitter CRUD
    fun getAllPetsitters(): List<Petsitter> = petsitterDao.getAllPetsitters()
    fun getPetsitterById(id: Long): Petsitter? = petsitterDao.getPetsitterById(id)
    fun getPetsitterByUserId(userId: Long): Petsitter? = petsitterDao.getPetsitterByUserId(userId)
    fun insertPetsitter(petsitter: Petsitter): Long = petsitterDao.insertPetsitter(petsitter)
    fun updatePetsitter(petsitter: Petsitter): Int = petsitterDao.updatePetsitter(petsitter)
    fun deletePetsitter(petsitter: Petsitter): Int = petsitterDao.deletePetsitter(petsitter)

    // PlanEmployee CRUD
    fun getAllPlanEmployees(): List<PlanEmployee> = planEmployeeDao.getAllPlanEmployees()
    fun getPlanEmployeeById(id: Long): PlanEmployee? = planEmployeeDao.getPlanEmployeeById(id)
    fun getPlanEmployeeByUserId(userId: Long): PlanEmployee? = planEmployeeDao.getPlanEmployeeByUserId(userId)
    fun insertPlanEmployee(employee: PlanEmployee): Long = planEmployeeDao.insertPlanEmployee(employee)
    fun updatePlanEmployee(employee: PlanEmployee): Int = planEmployeeDao.updatePlanEmployee(employee)
    fun deletePlanEmployee(employee: PlanEmployee): Int = planEmployeeDao.deletePlanEmployee(employee)

    // Petsitting CRUD
    fun getAllPetsittings(): List<Petsitting> = petsittingDao.getAllPetsittings()
    fun getPetsittingById(id: Long): Petsitting? = petsittingDao.getPetsittingById(id)
    fun getPetsittingsByUserId(userId: Long): List<Petsitting> = petsittingDao.getPetsittingsByUserId(userId)
    fun getPetsittingsByPetId(petId: Long): List<Petsitting> = petsittingDao.getPetsittingsByPetId(petId)
    fun getPetsittingsByPetsitterId(petsitterId: Long): List<Petsitting> = petsittingDao.getPetsittingsByPetsitterId(petsitterId)
    fun insertPetsitting(petsitting: Petsitting): Long = petsittingDao.insertPetsitting(petsitting)
    fun updatePetsitting(petsitting: Petsitting): Int = petsittingDao.updatePetsitting(petsitting)
    fun deletePetsitting(petsitting: Petsitting): Int = petsittingDao.deletePetsitting(petsitting)

    // Payment CRUD
    fun getAllPayments(): List<Payment> = paymentDao.getAllPayments()
    fun getPaymentById(id: Long): Payment? = paymentDao.getPaymentById(id)
    fun getPaymentByPetsittingId(petsittingId: Long): Payment? = paymentDao.getPaymentByPetsittingId(petsittingId)
    fun insertPayment(payment: Payment): Long = paymentDao.insertPayment(payment)
    fun updatePayment(payment: Payment): Int = paymentDao.updatePayment(payment)
    fun deletePayment(payment: Payment): Int = paymentDao.deletePayment(payment)

    // Procedure CRUD
    fun getAllProcedures(): List<Procedure> = procedureDao.getAllProcedures()
    fun getProcedureById(id: Long): Procedure? = procedureDao.getProcedureById(id)
    fun insertProcedure(procedure: Procedure): Long = procedureDao.insertProcedure(procedure)
    fun updateProcedure(procedure: Procedure): Int = procedureDao.updateProcedure(procedure)
    fun deleteProcedure(procedure: Procedure): Int = procedureDao.deleteProcedure(procedure)
}