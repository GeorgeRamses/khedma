package com.georgeramsis.khedma.khedma.data.repository

import com.georgeramsis.khedma.khedma.data.model.Activity
import com.georgeramsis.khedma.khedma.data.model.Attendance
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import com.georgeramsis.khedma.khedma.data.model.Student
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
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

    suspend fun getServantClass(): ServantPermission? {
        return client.postgrest["servant_permissions"].select() {
            filter {
                eq("servant_id", client.auth.currentUserOrNull()?.id ?: "")
            }
        }.decodeSingleOrNull<ServantPermission>()
    }

    suspend fun getUpcomingBirthdays(classId: String): List<Student> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val students = getStudentsByClass(classId).filter { student ->
            runCatching { LocalDate.parse(student.dateOfBirth) }.getOrNull()?.let { birthday ->
                birthday.month == today.month && birthday.day >= today.day
            } ?: false
        }
        return students
    }

    suspend fun getStudentsNeedingVisit(classId: String): List<StudentVisitNeed> {
        val students = getStudentsByClass(classId)
        val attendanceRecords = client.postgrest["attendance"].select {
            filter {
                eq("class_id", classId)
            }
        }.decodeList<Attendance>()

        val needVisit = attendanceRecords.groupBy { it.studentId }
            .mapValues { (_, records) ->
                records.sortedByDescending { it.date }
                    .takeWhile { it.status == "absent" }.count()
            }
            .filter { (_, count) -> count > 0 }

        return students.filter { it.id in needVisit.keys }
            .map { StudentVisitNeed(it, needVisit[it.id] ?: 0) }
    }

    suspend fun getUpcomingActivities(): List<Activity> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return client.postgrest["activities"].select {
            filter { gte("date", today) }
        }.decodeList<Activity>()
    }
}

data class StudentVisitNeed(val student: Student, val consecutiveAbsences: Int)

