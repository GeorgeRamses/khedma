package com.georgeramsis.khedma.khedma.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ─── Profile ───────────────────────────────────────────────────────────────

@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name") val fullName: String,
    val role: String? = "junior",
    val phone: String? = null,
    val email: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true,
    val address: String? = null,
    val birthdate: String? = null,
    val job: String? = null,
    @SerialName("confession_father") val confessionFather: String? = null,
    val church: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Student ───────────────────────────────────────────────────────────────

@Serializable
data class Student(
    val id: String? = null,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("date_of_birth") val dateOfBirth: String,
    @SerialName("current_stage_id") val currentStageId: String? = null,
    val phone: String? = null,
    @SerialName("parent_email") val parentEmail: String? = null,
    val address: String? = null,
    val notes: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true,
    @SerialName("created_by") val createdBy: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Stage ─────────────────────────────────────────────────────────────────

@Serializable
data class Stage(
    val id: String? = null,
    val name: String,
    @SerialName("display_order") val displayOrder: Int,
    @SerialName("min_age") val minAge: Int? = null,
    @SerialName("max_age") val maxAge: Int? = null,
    val description: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Class ─────────────────────────────────────────────────────────────────

@Serializable
data class SchoolClass(
    val id: String? = null,
    @SerialName("stage_id") val stageId: String? = null,
    val name: String,
    val description: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Student Class ─────────────────────────────────────────────────────────

@Serializable
data class StudentClass(
    val id: String? = null,
    @SerialName("student_id") val studentId: String? = null,
    @SerialName("class_id") val classId: String? = null,
    @SerialName("enrolled_date") val enrolledDate: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Attendance ────────────────────────────────────────────────────────────

@Serializable
data class Attendance(
    val id: String? = null,
    @SerialName("student_id") val studentId: String? = null,
    @SerialName("class_id") val classId: String? = null,
    val date: String,
    val status: String,
    val notes: String? = null,
    @SerialName("recorded_by") val recordedBy: String? = null,
    @SerialName("academic_year_id") val academicYearId: String? = null,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Visitation ────────────────────────────────────────────────────────────

@Serializable
data class Visitation(
    val id: String? = null,
    @SerialName("visit_date") val visitDate: String,
    @SerialName("visit_type") val visitType: String,
    @SerialName("visit_reason") val visitReason: String,
    @SerialName("visit_to") val visitTo: String,
    @SerialName("visit_by") val visitBy: String,
    val notes: String? = null
)

// ─── Activity ──────────────────────────────────────────────────────────────

@Serializable
data class Activity(
    val id: String? = null,
    val title: String,
    val date: String,
    val description: String? = null,
    val type: String,
    @SerialName("stage_id") val stageId: String? = null,
    @SerialName("created_by") val createdBy: String,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Activity Servant ──────────────────────────────────────────────────────

@Serializable
data class ActivityServant(
    val id: String? = null,
    @SerialName("activity_id") val activityId: String? = null,
    @SerialName("servant_id") val servantId: String? = null,
    @SerialName("assigned_at") val assignedAt: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

// ─── Parent ────────────────────────────────────────────────────────────────

@Serializable
data class Parent(
    val id: String? = null,
    @SerialName("full_name") val fullName: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val notes: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Student Parent ────────────────────────────────────────────────────────

@Serializable
data class StudentParent(
    val id: String? = null,
    @SerialName("student_id") val studentId: String,
    @SerialName("parent_id") val parentId: String,
    val relationship: String? = "guardian",
    @SerialName("is_primary_contact") val isPrimaryContact: Boolean? = false,
    val phone: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Academic Year ─────────────────────────────────────────────────────────

@Serializable
data class AcademicYear(
    val id: String? = null,
    val name: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    @SerialName("is_current") val isCurrent: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Lookup Value ──────────────────────────────────────────────────────────

@Serializable
data class LookupValue(
    val id: String? = null,
    val category: String,
    val value: String,
    @SerialName("is_active") val isActive: Boolean = true
)

// ─── Servant Permission ────────────────────────────────────────────────────

@Serializable
data class ServantPermission(
    val id: String? = null,
    @SerialName("servant_id") val servantId: String? = null,
    @SerialName("class_id") val classId: String? = null,
    @SerialName("stage_id") val stageId: String? = null,
    @SerialName("service_type") val serviceType: String? = null,
    @SerialName("granted_by") val grantedBy: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Student Stage History ─────────────────────────────────────────────────

@Serializable
data class StudentStageHistory(
    val id: String? = null,
    @SerialName("student_id") val studentId: String? = null,
    @SerialName("stage_id") val stageId: String? = null,
    @SerialName("academic_year") val academicYear: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String? = null,
    val notes: String? = null,
    @SerialName("transitioned_by") val transitionedBy: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

// ─── Notification Settings ─────────────────────────────────────────────────

@Serializable
data class NotificationSettings(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("absence_reminder_enabled") val absenceReminderEnabled: Boolean? = true,
    @SerialName("absence_threshold") val absenceThreshold: Int? = 2,
    @SerialName("birthday_reminder_enabled") val birthdayReminderEnabled: Boolean? = true,
    @SerialName("birthday_reminder_days") val birthdayReminderDays: Int? = 3,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

// ─── Views ─────────────────────────────────────────────────────────────────

@Serializable
data class StudentAttendanceSummary(
    @SerialName("student_id") val studentId: String? = null,
    @SerialName("student_name") val studentName: String? = null,
    @SerialName("class_id") val classId: String? = null,
    @SerialName("class_name") val className: String? = null,
    @SerialName("total_recorded") val totalRecorded: Long? = null,
    @SerialName("total_present") val totalPresent: Long? = null,
    @SerialName("total_absent") val totalAbsent: Long? = null,
    @SerialName("total_excused") val totalExcused: Long? = null,
    @SerialName("total_late") val totalLate: Long? = null,
    @SerialName("attendance_rate") val attendanceRate: Double? = null
)

@Serializable
data class ClassAttendanceByDate(
    @SerialName("class_id") val classId: String? = null,
    @SerialName("class_name") val className: String? = null,
    val date: String? = null,
    @SerialName("present_count") val presentCount: Long? = null,
    @SerialName("absent_count") val absentCount: Long? = null,
    @SerialName("excused_count") val excusedCount: Long? = null,
    @SerialName("late_count") val lateCount: Long? = null,
    @SerialName("total_count") val totalCount: Long? = null
)

@Serializable
data class AbsenceRecord(
    @SerialName("student_id") val studentId: String,
    @SerialName("class_id") val classId: String,
    val date: String
)

data class AbsenceWithName(val studentName: String, val date: String)