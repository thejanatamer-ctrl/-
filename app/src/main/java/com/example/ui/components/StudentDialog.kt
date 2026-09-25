package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entities.StudentEntity
import com.example.ui.theme.IslamicGoldBright
import com.example.ui.theme.IslamicGoldPrimary
import com.example.ui.theme.IslamicGreenDark
import com.example.ui.theme.IslamicGreenPrimary

private val avatarColors = listOf(
    "#114B3A", // Emerald
    "#D4AF37", // Gold
    "#1E6B53", // Jade
    "#8C6B1C", // Bronze
    "#2E7D32", // Forest
    "#5D4037"  // Warm Brown
)

@Composable
fun StudentDialog(
    currentStudent: StudentEntity?,
    existingStudents: List<StudentEntity>,
    onDismiss: () -> Unit,
    onStudentSelected: (name: String, avatarColorHex: String) -> Unit
) {
    var nameInput by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(avatarColors.first()) }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, IslamicGoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(IslamicGreenPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "ملف الطالب",
                            tint = IslamicGreenPrimary
                        )
                    }

                    Text(
                        text = "تسجيل وانضمام الطالب",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreenDark
                        )
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_student_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Input
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                        if (it.isNotBlank()) isError = false
                    },
                    label = { Text("اكتب اسمك للانضمام") },
                    placeholder = { Text("مثال: عبد الرحمن، عائشة...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "إضافة طالب",
                            tint = IslamicGreenPrimary
                        )
                    },
                    isError = isError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_name_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicGreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                if (isError) {
                    Text(
                        text = "يرجى كتابة اسم الطالب أولاً",
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp, start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar color picker
                Text(
                    text = "اختر لون شارتك:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    avatarColors.forEach { colorHex ->
                        val color = Color(android.graphics.Color.parseColor(colorHex))
                        val isSelected = selectedColor == colorHex

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) IslamicGoldBright else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = colorHex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "محدد",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Join button
                Button(
                    onClick = {
                        if (nameInput.trim().isNotBlank()) {
                            onStudentSelected(nameInput.trim(), selectedColor)
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("join_student_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IslamicGreenPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "دخول وبدء التسبيح",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                if (existingStudents.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "أو اختر من طلاب الحلقة الحاليين:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        items(existingStudents) { student ->
                            val isCurrent = currentStudent?.id == student.id
                            val avatarColor = try {
                                Color(android.graphics.Color.parseColor(student.avatarColorHex))
                            } catch (_: Exception) {
                                IslamicGreenPrimary
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isCurrent) IslamicGoldPrimary.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .clickable {
                                        onStudentSelected(student.name, student.avatarColorHex)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(avatarColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = student.name.take(1),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = student.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                            color = IslamicGreenDark
                                        )
                                    )
                                    Text(
                                        text = "مجموع التسبيحات: ${student.totalCount}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }

                                if (isCurrent) {
                                    Text(
                                        text = "أنت الآن",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = IslamicGreenPrimary,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(IslamicGreenPrimary.copy(alpha = 0.12f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
