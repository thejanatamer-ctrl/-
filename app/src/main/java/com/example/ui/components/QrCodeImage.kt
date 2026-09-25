package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

@Composable
fun QrCodeImage(
    content: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 220.dp,
    pixelSize: Int = 512,
    darkColor: Int = AndroidColor.parseColor("#0A2E23"),
    lightColor: Int = AndroidColor.parseColor("#FFFFFF")
) {
    val bitmap = remember(content, pixelSize, darkColor, lightColor) {
        generateQrBitmap(content, pixelSize, darkColor, lightColor)
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "رمز الاستجابة السريعة QR",
            modifier = modifier.size(sizeDp)
        )
    }
}

private fun generateQrBitmap(
    content: String,
    size: Int,
    darkColor: Int,
    lightColor: Int
): Bitmap? {
    return try {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 1
        )
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) darkColor else lightColor
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap
    } catch (_: Exception) {
        null
    }
}
