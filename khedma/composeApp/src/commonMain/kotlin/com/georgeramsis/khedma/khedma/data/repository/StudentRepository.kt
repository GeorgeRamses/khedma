package com.georgeramsis.khedma.khedma.data.repository

import com.georgeramsis.khedma.khedma.data.model.AbsenceRecord
import com.georgeramsis.khedma.khedma.data.model.Activity
import com.georgeramsis.khedma.khedma.data.model.ClassAndStageName
import com.georgeramsis.khedma.khedma.data.model.ClassIdParam
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import com.georgeramsis.khedma.khedma.data.model.StageNameResult
import com.georgeramsis.khedma.khedma.data.model.Student
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class StudentRepository(private val client: SupabaseClient) {
    suspend fun getStudentsByClass(classId: String): List<Student> {

        return client.postgrest["students"].select(
            Columns.raw("*,student_classes!inner(class_id)")
        ) {
            filter {
                eq("student_classes.class_id", classId)
            }
        }.decodeList<Student>()
    }

    suspend fun getClassAndStageName(classId: String): ClassAndStageName {
        // This function can be implemented to fetch stage and class names based on IDs
        // For example, you can create a mapping of stageId to stageName and classId to className
        return client.postgrest.rpc("get_class_and_stage_name", ClassIdParam(classId))
            .decodeSingle<ClassAndStageName>()
    }

    suspend fun getStageName(stageId: String): ClassAndStageName {
        val result = client.postgrest["stages"].select(Columns.list("name")) {
            filter {
                eq("id", stageId)
            }
        }.decodeSingle<StageNameResult>()
        return ClassAndStageName(stageName = result.name, className = "")
    }

    suspend fun getStudentsByStage(stageId: String): List<Student> {

        return client.postgrest["students"].select() {
            filter {
                eq("current_stage_id", stageId)
            }
        }.decodeList<Student>()
    }

    fun getUpcomingBirthdays(students: List<Student>): List<Student> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return students.filter { student ->
            runCatching { LocalDate.parse(student.dateOfBirth) }.getOrNull()?.let { birthday ->
                birthday.month == today.month && birthday.day >= today.day
            } ?: false
        }
    }


    suspend fun getStageStudentsNeedingVisit(permission: ServantPermission): List<AbsenceRecord> {
        val allAbsence = client.postgrest.rpc("get_last_session_absences").decodeList<AbsenceRecord>()
        val studentsStage = permission.stageId?.let { stageId ->
            getStudentsByStage(stageId).mapNotNull { it.id }.toSet()
        }

        val myStageAbsent = allAbsence.filter { absence ->
            absence.studentId in studentsStage.orEmpty()
        }
        return myStageAbsent
    }

    suspend fun getClassStudentsNeedingVisit(permission: ServantPermission): List<AbsenceRecord> {
        val allAbsence = client.postgrest.rpc("get_last_session_absences").decodeList<AbsenceRecord>()
        val myClassAbsent = allAbsence.filter { it.classId == permission.classId }
        return myClassAbsent
    }

    suspend fun getUpcomingActivities(): List<Activity> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return client.postgrest["activities"].select {
            filter { gte("date", today) }
        }.decodeList<Activity>()
    }
}



