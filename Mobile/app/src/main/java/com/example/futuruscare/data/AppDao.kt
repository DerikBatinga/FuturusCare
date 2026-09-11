package com.example.futuruscare.data

import androidx.room.*
import com.example.futuruscare.data.model.Payment
import com.example.futuruscare.data.model.Pet
import com.example.futuruscare.data.model.Petsitter
import com.example.futuruscare.data.model.Petsitting
import com.example.futuruscare.data.model.PlanEmployee
import com.example.futuruscare.data.model.Procedure
import com.example.futuruscare.data.model.User
import com.example.futuruscare.data.model.UserAddress

@Dao
interface UserDao {
    @Query("SELECT * FROM tb_user ORDER BY id_user DESC")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM tb_user WHERE id_user = :id")
    fun getUserById(id: Long): User?

    @Query("SELECT * FROM tb_user WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM tb_user WHERE cpf = :cpf")
    fun getUserByCpf(cpf: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Update
    fun updateUser(user: User): Int

    @Delete
    fun deleteUser(user: User): Int

    @Query("DELETE FROM tb_user WHERE id_user = :id")
    fun deleteUserById(id: Long): Int
}

@Dao
interface PetDao {
    @Query("SELECT * FROM tb_pet ORDER BY id_pet DESC")
    fun getAllPets(): List<Pet>

    @Query("SELECT * FROM tb_pet WHERE id_pet = :id")
    fun getPetById(id: Long): Pet?

    @Query("SELECT * FROM tb_pet WHERE user_id = :userId")
    fun getPetsByUserId(userId: Long): List<Pet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPet(pet: Pet): Long

    @Update
    fun updatePet(pet: Pet): Int

    @Delete
    fun deletePet(pet: Pet): Int

    @Query("DELETE FROM tb_pet WHERE id_pet = :id")
    fun deletePetById(id: Long): Int
}

@Dao
interface UserAddressDao {
    @Query("SELECT * FROM tb_user_address WHERE user_id = :userId")
    fun getAddressByUserId(userId: Long): UserAddress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: UserAddress): Long

    @Update
    fun updateAddress(address: UserAddress): Int

    @Delete
    fun deleteAddress(address: UserAddress): Int
}

@Dao
interface PetsitterDao {
    @Query("SELECT * FROM tb_petsitter ORDER BY id_petsitter DESC")
    fun getAllPetsitters(): List<Petsitter>

    @Query("SELECT * FROM tb_petsitter WHERE id_petsitter = :id")
    fun getPetsitterById(id: Long): Petsitter?

    @Query("SELECT * FROM tb_petsitter WHERE user_id = :userId")
    fun getPetsitterByUserId(userId: Long): Petsitter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPetsitter(petsitter: Petsitter): Long

    @Update
    fun updatePetsitter(petsitter: Petsitter): Int

    @Delete
    fun deletePetsitter(petsitter: Petsitter): Int
}

@Dao
interface PlanEmployeeDao {
    @Query("SELECT * FROM tb_plan_employees ORDER BY id_employee DESC")
    fun getAllPlanEmployees(): List<PlanEmployee>

    @Query("SELECT * FROM tb_plan_employees WHERE id_employee = :id")
    fun getPlanEmployeeById(id: Long): PlanEmployee?

    @Query("SELECT * FROM tb_plan_employees WHERE user_id = :userId")
    fun getPlanEmployeeByUserId(userId: Long): PlanEmployee?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlanEmployee(employee: PlanEmployee): Long

    @Update
    fun updatePlanEmployee(employee: PlanEmployee): Int

    @Delete
    fun deletePlanEmployee(employee: PlanEmployee): Int
}

@Dao
interface PetsittingDao {
    @Query("SELECT * FROM tb_petsitting ORDER BY id_petsitting DESC")
    fun getAllPetsittings(): List<Petsitting>

    @Query("SELECT * FROM tb_petsitting WHERE id_petsitting = :id")
    fun getPetsittingById(id: Long): Petsitting?

    @Query("SELECT * FROM tb_petsitting WHERE user_id = :userId")
    fun getPetsittingsByUserId(userId: Long): List<Petsitting>

    @Query("SELECT * FROM tb_petsitting WHERE pet_id = :petId")
    fun getPetsittingsByPetId(petId: Long): List<Petsitting>

    @Query("SELECT * FROM tb_petsitting WHERE petsitter_id = :petsitterId")
    fun getPetsittingsByPetsitterId(petsitterId: Long): List<Petsitting>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPetsitting(petsitting: Petsitting): Long

    @Update
    fun updatePetsitting(petsitting: Petsitting): Int

    @Delete
    fun deletePetsitting(petsitting: Petsitting): Int
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM tb_payments ORDER BY id_payment DESC")
    fun getAllPayments(): List<Payment>

    @Query("SELECT * FROM tb_payments WHERE id_payment = :id")
    fun getPaymentById(id: Long): Payment?

    @Query("SELECT * FROM tb_payments WHERE petsitting_id = :petsittingId")
    fun getPaymentByPetsittingId(petsittingId: Long): Payment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(payment: Payment): Long

    @Update
    fun updatePayment(payment: Payment): Int

    @Delete
    fun deletePayment(payment: Payment): Int
}

@Dao
interface ProcedureDao {
    @Query("SELECT * FROM tb_procedure ORDER BY id_procedure DESC")
    fun getAllProcedures(): List<Procedure>

    @Query("SELECT * FROM tb_procedure WHERE id_procedure = :id")
    fun getProcedureById(id: Long): Procedure?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProcedure(procedure: Procedure): Long

    @Update
    fun updateProcedure(procedure: Procedure): Int

    @Delete
    fun deleteProcedure(procedure: Procedure): Int
}