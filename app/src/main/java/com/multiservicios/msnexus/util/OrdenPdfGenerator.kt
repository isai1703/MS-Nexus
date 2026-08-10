package com.multiservicios.msnexus.util

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.multiservicios.msnexus.R
import com.multiservicios.msnexus.data.local.OrdenEntity
import java.io.File
import java.util.Locale

object OrdenPdfGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2)

    fun generar(
        context: Context,
        orden: OrdenEntity
    ): String? {

        return try {

            val document = PdfDocument()

            val pageInfo = PdfDocument.PageInfo.Builder(
                PAGE_WIDTH,
                PAGE_HEIGHT,
                1
            ).create()

            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            val black = android.graphics.Color.rgb(25, 25, 25)
            val gray = android.graphics.Color.rgb(90, 90, 90)
            val lightGray = android.graphics.Color.rgb(235, 235, 235)
            val darkGray = android.graphics.Color.rgb(55, 55, 55)
            val white = android.graphics.Color.WHITE

            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = black
                strokeWidth = 1f
            }

            val titlePaint = Paint(paint).apply {
                textSize = 18f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            val sectionPaint = Paint(paint).apply {
                textSize = 11f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            val textPaint = Paint(paint).apply {
                textSize = 9.5f
            }

            val smallPaint = Paint(paint).apply {
                textSize = 8f
                color = gray
            }

            val boldPaint = Paint(paint).apply {
                textSize = 9.5f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = white
                textSize = 10f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            /*
             * ENCABEZADO CON LOGO
             */

            val logo = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ms_nexus_logo
            )

            if (logo != null) {

                val logoMaxWidth = 260f
                val logoMaxHeight = 72f

                val ratio = minOf(
                    logoMaxWidth / logo.width.toFloat(),
                    logoMaxHeight / logo.height.toFloat()
                )

                val logoWidth = logo.width * ratio
                val logoHeight = logo.height * ratio

                val logoRect = RectF(
                    MARGIN,
                    25f,
                    MARGIN + logoWidth,
                    25f + logoHeight
                )

                canvas.drawBitmap(
                    logo,
                    null,
                    logoRect,
                    null
                )

            } else {

                canvas.drawText(
                    "MS NEXUS",
                    MARGIN,
                    55f,
                    titlePaint
                )

                canvas.drawText(
                    "MULTISERVICIOS",
                    MARGIN,
                    75f,
                    sectionPaint
                )
            }

            /*
             * FOLIO
             */

            val folioBox = RectF(
                405f,
                30f,
                PAGE_WIDTH - MARGIN,
                78f
            )

            paint.color = darkGray

            canvas.drawRoundRect(
                folioBox,
                6f,
                6f,
                paint
            )

            canvas.drawText(
                "ORDEN DE TRABAJO",
                420f,
                48f,
                whitePaint
            )

            canvas.drawText(
                orden.folio,
                420f,
                66f,
                Paint(whitePaint).apply {
                    textSize = 14f
                }
            )

            paint.color = black
            paint.strokeWidth = 2f

            canvas.drawLine(
                MARGIN,
                94f,
                PAGE_WIDTH - MARGIN,
                94f,
                paint
            )

            paint.strokeWidth = 1f

            var y = 115f

            /*
             * INFORMACIÓN DE LA ORDEN
             */

            drawSectionTitle(
                canvas,
                "INFORMACIÓN DE LA ORDEN",
                y,
                sectionPaint,
                lightGray
            )

            y += 28f

            drawLabelValue(
                canvas,
                "Fecha:",
                orden.fecha,
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            drawLabelValue(
                canvas,
                "Estado:",
                orden.estado,
                300f,
                y,
                boldPaint,
                textPaint
            )

            y += 22f

            drawLabelValue(
                canvas,
                "Fecha programada:",
                orden.fechaProgramada.ifBlank {
                    "No especificada"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 30f

            /*
             * DATOS DEL CLIENTE
             */

            drawSectionTitle(
                canvas,
                "DATOS DEL CLIENTE",
                y,
                sectionPaint,
                lightGray
            )

            y += 28f

            drawLabelValue(
                canvas,
                "Número de cliente:",
                orden.numeroCliente,
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 20f

            drawLabelValue(
                canvas,
                "Cliente:",
                orden.nombreCliente,
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 20f

            drawLabelValue(
                canvas,
                "Empresa:",
                orden.empresa.ifBlank {
                    "Particular"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 20f

            drawLabelValue(
                canvas,
                "Teléfono:",
                orden.telefono.ifBlank {
                    "No registrado"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 20f

            drawLabelValue(
                canvas,
                "Correo:",
                orden.correo.ifBlank {
                    "No registrado"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 20f

            drawLabelValue(
                canvas,
                "Dirección:",
                orden.direccion.ifBlank {
                    "No registrada"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 30f

            /*
             * DETALLE DEL SERVICIO
             */

            drawSectionTitle(
                canvas,
                "DETALLE DEL SERVICIO",
                y,
                sectionPaint,
                lightGray
            )

            y += 28f

            drawLabelValue(
                canvas,
                "Tipo de trabajo:",
                orden.tipoTrabajo.ifBlank {
                    "No especificado"
                },
                MARGIN,
                y,
                boldPaint,
                textPaint
            )

            y += 22f

            canvas.drawText(
                "Descripción del trabajo:",
                MARGIN,
                y,
                boldPaint
            )

            y += 17f

            val descripcion = orden.descripcionTrabajo.ifBlank {
                "Sin descripción"
            }

            val descripcionLineas = dividirTexto(
                descripcion,
                textPaint,
                CONTENT_WIDTH
            )

            for (linea in descripcionLineas.take(5)) {

                canvas.drawText(
                    linea,
                    MARGIN,
                    y,
                    textPaint
                )

                y += 15f
            }

            y += 15f

            /*
             * RESUMEN ECONÓMICO
             */

            drawSectionTitle(
                canvas,
                "RESUMEN ECONÓMICO",
                y,
                sectionPaint,
                lightGray
            )

            y += 28f

            val tableTop = y
            val rowHeight = 22f

            paint.color = lightGray

            canvas.drawRect(
                MARGIN,
                tableTop,
                PAGE_WIDTH - MARGIN,
                tableTop + rowHeight,
                paint
            )

            paint.color = black

            canvas.drawText(
                "CONCEPTO",
                MARGIN + 10f,
                tableTop + 15f,
                boldPaint
            )

            canvas.drawText(
                "IMPORTE",
                455f,
                tableTop + 15f,
                boldPaint
            )

            y += rowHeight

            drawTableRow(
                canvas,
                "Subtotal",
                orden.subtotal,
                y,
                rowHeight,
                textPaint,
                boldPaint
            )

            y += rowHeight

            drawTableRow(
                canvas,
                "Descuento",
                orden.descuento,
                y,
                rowHeight,
                textPaint,
                boldPaint
            )

            y += rowHeight

            drawTableRow(
                canvas,
                "IVA (${orden.ivaPorcentaje}%)",
                orden.ivaImporte,
                y,
                rowHeight,
                textPaint,
                boldPaint
            )

            y += rowHeight

            paint.color = darkGray

            canvas.drawRect(
                MARGIN,
                y,
                PAGE_WIDTH - MARGIN,
                y + 30f,
                paint
            )

            canvas.drawText(
                "TOTAL",
                MARGIN + 10f,
                y + 20f,
                whitePaint
            )

            canvas.drawText(
                "$${formatMoney(orden.total)}",
                445f,
                y + 20f,
                Paint(whitePaint).apply {
                    textSize = 12f
                }
            )

            paint.color = black

            y += 50f

            /*
             * OBSERVACIONES
             */

            drawSectionTitle(
                canvas,
                "OBSERVACIONES",
                y,
                sectionPaint,
                lightGray
            )

            y += 24f

            paint.style = Paint.Style.STROKE
            paint.color = android.graphics.Color.LTGRAY

            canvas.drawRect(
                MARGIN,
                y,
                PAGE_WIDTH - MARGIN,
                y + 52f,
                paint
            )

            paint.style = Paint.Style.FILL
            paint.color = black

            y += 65f

            /*
             * CONFORMIDAD
             */

            drawSectionTitle(
                canvas,
                "CONFORMIDAD DEL CLIENTE",
                y,
                sectionPaint,
                lightGray
            )

            y += 24f

            val conformidad = Paint(paint).apply {
                textSize = 8.5f
                color = gray
            }

            canvas.drawText(
                "El cliente manifiesta haber recibido el servicio indicado",
                MARGIN,
                y,
                conformidad
            )

            y += 13f

            canvas.drawText(
                "y expresa su conformidad con los trabajos realizados.",
                MARGIN,
                y,
                conformidad
            )

            y += 28f

            /*
             * FIRMAS
             */

            val signatureTop = y

            paint.color = android.graphics.Color.GRAY
            paint.strokeWidth = 1f

            canvas.drawLine(
                MARGIN,
                signatureTop + 55f,
                250f,
                signatureTop + 55f,
                paint
            )

            canvas.drawLine(
                315f,
                signatureTop + 55f,
                PAGE_WIDTH - MARGIN,
                signatureTop + 55f,
                paint
            )

            canvas.drawText(
                "Firma del cliente",
                MARGIN,
                signatureTop + 70f,
                smallPaint
            )

            canvas.drawText(
                "Firma del responsable",
                315f,
                signatureTop + 70f,
                smallPaint
            )

            y = signatureTop + 92f

            canvas.drawText(
                "Nombre del cliente:",
                MARGIN,
                y,
                boldPaint
            )

            canvas.drawLine(
                135f,
                y + 2f,
                280f,
                y + 2f,
                paint
            )

            canvas.drawText(
                "Sello de la empresa:",
                315f,
                y,
                boldPaint
            )

            /*
             * PIE DE PÁGINA
             */

            paint.color = lightGray

            canvas.drawRect(
                0f,
                PAGE_HEIGHT - 48f,
                PAGE_WIDTH.toFloat(),
                PAGE_HEIGHT.toFloat(),
                paint
            )

            canvas.drawText(
                "Documento generado por MS Nexus",
                MARGIN,
                PAGE_HEIGHT - 28f,
                smallPaint
            )

            canvas.drawText(
                "Folio: ${orden.folio}",
                455f,
                PAGE_HEIGHT - 28f,
                smallPaint
            )

            /*
             * FINALIZAR
             */

            document.finishPage(page)

            val fileName = "${orden.folio}.pdf"

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

                    if (output == null) {
                        resolver.delete(uri, null, null)
                        return null
                    }

                    document.writeTo(output)
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

                val folder = File(
                    directory,
                    "MS Nexus"
                )

                if (!folder.exists()) {
                    folder.mkdirs()
                }

                val file = File(
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

    private fun drawSectionTitle(
        canvas: Canvas,
        title: String,
        y: Float,
        paint: Paint,
        backgroundColor: Int
    ) {

        val background = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
        }

        canvas.drawRect(
            MARGIN,
            y - 13f,
            PAGE_WIDTH - MARGIN,
            y + 5f,
            background
        )

        canvas.drawText(
            title,
            MARGIN + 7f,
            y,
            paint
        )
    }

    private fun drawLabelValue(
        canvas: Canvas,
        label: String,
        value: String,
        x: Float,
        y: Float,
        labelPaint: Paint,
        valuePaint: Paint
    ) {

        canvas.drawText(
            label,
            x,
            y,
            labelPaint
        )

        val labelWidth = labelPaint.measureText(label)

        canvas.drawText(
            value,
            x + labelWidth + 5f,
            y,
            valuePaint
        )
    }

    private fun drawTableRow(
        canvas: Canvas,
        concept: String,
        amount: Double,
        y: Float,
        rowHeight: Float,
        textPaint: Paint,
        boldPaint: Paint
    ) {

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 0.7f
        }

        canvas.drawRect(
            MARGIN,
            y,
            PAGE_WIDTH - MARGIN,
            y + rowHeight,
            borderPaint
        )

        canvas.drawText(
            concept,
            MARGIN + 10f,
            y + 15f,
            textPaint
        )

        canvas.drawText(
            "$${formatMoney(amount)}",
            445f,
            y + 15f,
            boldPaint
        )
    }

    private fun dividirTexto(
        texto: String,
        paint: Paint,
        maxWidth: Float
    ): List<String> {

        val palabras = texto.trim().split(
            Regex("\\s+")
        )

        val lineas = mutableListOf<String>()
        var lineaActual = ""

        for (palabra in palabras) {

            val candidata =
                if (lineaActual.isEmpty()) {
                    palabra
                } else {
                    "$lineaActual $palabra"
                }

            if (paint.measureText(candidata) <= maxWidth) {

                lineaActual = candidata

            } else {

                if (lineaActual.isNotEmpty()) {
                    lineas.add(lineaActual)
                }

                lineaActual = palabra
            }
        }

        if (lineaActual.isNotEmpty()) {
            lineas.add(lineaActual)
        }

        return lineas
    }

    private fun formatMoney(
        value: Double
    ): String {

        return String.format(
            Locale.US,
            "%.2f",
            value
        )
    }
}
