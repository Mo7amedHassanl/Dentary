package com.m7md7sn.dentary.presentation.ui.patient.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.data.model.Attachment
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaExtraBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.AlexandriaSemiBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightGray
import java.io.File

@Composable
fun MedicalHistoryCard(
    medicalHistory: MedicalHistory,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    onToggleSelection: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .pointerInput(onLongClick, isSelectionMode) {
                detectTapGestures(
                    onTap = {
                        if (isSelectionMode) onToggleSelection()
                        else isExpanded = !isExpanded
                    },
                    onLongPress = { onLongClick() }
                )
            },
        shape = RoundedCornerShape(21.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) DentaryBlue else Color(0xFFE3E7F2)
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F4FF) else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelectionMode) {
                    Icon(
                        imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = null,
                        tint = if (isSelected) DentaryBlue else Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_medical_history),
                    contentDescription = null,
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(DentaryBlue)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.medical_history),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaExtraBold,
                        fontWeight = FontWeight.ExtraBold,
                        color = DentaryDarkBlue,
                    )
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = DentaryBlueGray,
                    modifier = Modifier
                        .height(12.dp)
                        .rotate(if (isExpanded) 90f else 0f)
                )
            }

            if (isExpanded) {
                ExpandedMedicalHistoryContent(
                    medicalHistory = medicalHistory,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun ExpandedMedicalHistoryContent(
    medicalHistory: MedicalHistory,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (!medicalHistory.description.isNullOrBlank()) {
            Text(
                text = medicalHistory.description,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                    color = DentaryDarkBlue,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = DentaryLighterBlue
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_attachments),
                contentDescription = null
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.attachments),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = DentaryBlue,
                ),
            )
        }

        if (medicalHistory.attachments.isEmpty()) {
            Text(
                text = stringResource(R.string.no_attachments),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                    color = DentaryBlueGray,
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        } else {
            medicalHistory.attachments.forEach { attachment ->
                AttachmentRow(
                    attachment = attachment,
                    context = context
                )
            }
        }

        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun AttachmentRow(
    attachment: Attachment,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val isImage = attachment.fileType.startsWith("image/")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DentaryLightGray)
            .clickable { openAttachment(context, attachment) }
            .padding(vertical = 8.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isImage) {
            AsyncImage(
                model = attachment.fileUrl,
                contentDescription = attachment.fileName,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_attachments),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = attachment.fileName,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = AlexandriaSemiBold,
                fontWeight = FontWeight.SemiBold,
                color = DentaryDarkBlue,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun openAttachment(context: android.content.Context, attachment: Attachment) {
    val uri = Uri.parse(attachment.fileUrl)
    when (uri.scheme) {
        "http", "https" -> {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, attachment.fileType)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(Intent.createChooser(intent, null))
        }
        "file" -> {
            val file = File(uri.path!!)
            if (file.exists()) {
                val contentUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(contentUri, attachment.fileType)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(Intent.createChooser(intent, null))
            } else {
                Toast.makeText(context, R.string.file_not_found, Toast.LENGTH_SHORT).show()
            }
        }
        "content" -> {
            try {
                val tempFile = File(context.cacheDir, "view_${attachment.fileName}")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                if (tempFile.exists()) {
                    val contentUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        tempFile
                    )
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(contentUri, attachment.fileType)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                } else {
                    Toast.makeText(context, R.string.file_not_found, Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                Toast.makeText(context, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
