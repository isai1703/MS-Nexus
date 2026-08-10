package com.multiservicios.msnexus.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.multiservicios.msnexus.data.local.OrdenEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object OrdenPdfGenerator {

    fun generar(
        context: Context,
        orden: OrdenEntity
    ): String? {

        return try {

            val document = PdfDocument()

            val pageInfo = PdfDocument.PageInfo.Builder(
                595,
                842,
                1
            ).create()

            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.BLACK
            }

            val titlePaint = Paint(paint).apply {
                textSize = 24f
                isFakeBoldText = true
            }

            val subtitlePaint = Paint(paint).apply {
                textSize = 14f
                isFakeBoldText = true
            }

            val textPaint = Paint(paint).apply {
                textSize = 12f
            }

            var y = 50f

            canvas.drawText(
                "MS NEXUS",
                40f,
                y,
                titlePaint
            )

            y += 25f

            canvas.drawText(
                "MULTISERVICIOS",
                40f,
                y,
                subtitlePaint
            )

            y += 35f

            canvas.drawText(
                "ORDEN DE TRABAJO",
                40f,
                y,
                subtitlePaint
            )

            y += 25f

            canvas.drawText(
                "Folio: ${orden.folio}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Fecha: ${orden.fecha}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Estado: ${orden.estado}",
                40f,
                y,
                textPaint
            )

            y += 35f

            canvas.drawText(
                "DATOS DEL CLIENTE",
                40f,
                y,
                subtitlePaint
            )

            y += 22f

            canvas.drawText(
                "Número de cliente: ${orden.numeroCliente}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Cliente: ${orden.nombreCliente}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Empresa: ${
                    orden.empresa.ifBlank {
                        "Particular"
                    }
                }",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Teléfono: ${
                    orden.telefono.ifBlank {
                        "No registrado"
                    }
                }",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Correo: ${
                    orden.correo.ifBlank {
                        "No registrado"
                    }
                }",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Dirección: ${
                    orden.direccion.ifBlank {
                        "No registrada"
                    }
                }",
                40f,
                y,
                textPaint
            )

            y += 35f

            canvas.drawText(
                "TRABAJO",
                40f,
                y,
                subtitlePaint
            )

            y += 22f

            canvas.drawText(
                "Tipo: ${orden.tipoTrabajo}",
                40f,
                y,
                textPaint
            )

            y += 22f

            canvas.drawText(
                "Descripción:",
                40f,
                y,
                textPaint
            )

            y += 20f

            val descripcion =
                orden.descripcionTrabajo.ifBlank {
                    "Sin descripción"
                }

            val lineas =
                descripcion.chunked(75)

            for (linea in lineas.take(6)) {

                canvas.drawText(
                    linea,
                    40f,
                    y,
                    textPaint
                )

                y += 18f
            }

            y += 25f

            canvas.drawText(
                "FECHAS",
                40f,
                y,
                subtitlePaint
            )

            y += 22f

            canvas.drawText(
                "Programada: ${
                    orden.fechaProgramada.ifBlank {
                        "No especificada"
                    }
                }",
                40f,
                y,
                textPaint
            )

            y += 35f

            canvas.drawText(
                "RESUMEN",
                40f,
                y,
                subtitlePaint
            )

            y += 22f

            canvas.drawText(
                "Subtotal: $${"%.2f".format(orden.subtotal)}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "Descuento: $${"%.2f".format(orden.descuento)}",
                40f,
                y,
                textPaint
            )

            y += 20f

            canvas.drawText(
                "IVA (${orden.ivaPorcentaje}%): $${
                    "%.2f".format(orden.ivaImporte)
                }",
                40f,
                y,
                textPaint
            )

            y += 25f

            canvas.drawText(
                "TOTAL: $${"%.2f".format(orden.total)}",
                40f,
                y,
                subtitlePaint
            )

            y += 60f

            canvas.drawText(
                "Documento generado por MS Nexus",
                40f,
                y,
                textPaint
            )

            y += 18f

            canvas.drawText(
                SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date()),
                40f,
                y,
                textPaint
            )

            document.finishPage(page)

            val fileName =
                "${orden.folio}.pdf"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val values = ContentValues().apply {
                    put(
                        MediaStore.Downloads.DISPLAY_NAME,
                        fileName
                    )

                    put(
                        MediaStore.Downloads.MIME_TYPE,
                        "application/pdf"
                    )

                    put(
                        MediaStore.Downloads.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS +
                            "/MS Nexus"
                    )

                    put(
                        MediaStore.Downloads.IS_PENDING,
                        1
                    )
                }

                val resolver = context.contentResolver

                val uri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                ) ?: return null

                resolver.openOutputStream(uri).use { output ->
                    document.writeTo(output!!)
                }

                values.clear()

                values.put(
                    MediaStore.Downloads.IS_PENDING,
                    0
                )

                resolver.update(
                    uri,
                    values,
                    null,
                    null
                )

                document.close()

                uri.toString()

            } else {

                val directory =
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                val folder =
                    java.io.File(
                        directory,
                        "MS Nexus"
                    )

                if (!folder.exists()) {
                    folder.mkdirs()
                }

                val file =
                    java.io.File(
                        folder,
                        fileName
                    )

                file.outputStream().use { output ->
                    document.writeTo(output)
                }

                document.close()

                file.absolutePath
            }

        } catch (_: Exception) {
            null
        }
    }
}
