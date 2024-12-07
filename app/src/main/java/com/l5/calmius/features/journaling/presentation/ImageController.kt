package com.l5.calmius.feature.journaling.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.l5.calmius.R
import com.l5.calmius.features.journaling.presentation.MyModalBottomSheet
import java.io.File

@Composable
fun MyImageArea(
    uri: Uri? = null, //target url to preview
    directory: File? = null, // stored directory
    onSetUri : (Uri) -> Unit = {}, // selected / taken uri
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    // for takePhotoLauncher used
    fun getTempUri(): Uri? {
        directory?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
        return null
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onSetUri.invoke(it)
            }
        }
    )

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {isSaved ->
            tempUri.value?.let {
                onSetUri.invoke(it)
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch takePhotoLauncher
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            takePhotoLauncher.launch(tempUri.value!!)
        } else {
            // Permission is denied, handle it accordingly
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet){
        MyModalBottomSheet(
            onDismiss = {
                showBottomSheet = false
            },
            onTakePhotoClick = {
                showBottomSheet = false

                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is already granted, proceed to step 2
                    val tmpUri = getTempUri()
                    tempUri.value = tmpUri
                    takePhotoLauncher.launch(tempUri.value!!)
                } else {
                    // Permission is not granted, request it
                    cameraPermissionLauncher.launch(permission)
                }
            },
            onPhotoGalleryClick = {
                showBottomSheet = false
                imagePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
        )
    }

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Text(text = "Select / Take")
            }
        }

        //preview selfie
        uri?.let {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = it,
                    modifier = Modifier.size(
                        160.dp
                    ),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}