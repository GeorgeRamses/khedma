package com.georgeramsis.khedma.khedma.data.repository

import com.georgeramsis.khedma.khedma.data.model.Student
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

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
}