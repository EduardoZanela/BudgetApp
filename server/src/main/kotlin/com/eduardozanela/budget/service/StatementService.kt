package com.eduardozanela.budget.service

import com.eduardozanela.budget.extractor.ATBChequingStatementExtractor
import com.eduardozanela.budget.extractor.ATBStatementExtractor
import com.eduardozanela.budget.extractor.CTFSStatementExtractor
import com.eduardozanela.budget.extractor.NeoStatementExtractor
import com.eduardozanela.budget.model.Bank
import com.eduardozanela.budget.utils.CSVUtil
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Service
class StatementService(
    @param:Value("\${tesseract.datapath}")
    private val tesseractDataPath: String
) {

    fun extract(file: MultipartFile, bank: Bank): ByteArray {
        val text = when (bank) {
            Bank.ATB, Bank.ATBCHEQUING -> extractTextWithOcr(file.inputStream)
            else -> extractTextEncrypted(file.inputStream)
        }

        val extractor = when (bank) {
            Bank.ATB -> ATBStatementExtractor()
            Bank.ATBCHEQUING -> ATBChequingStatementExtractor()
            Bank.CTFS -> CTFSStatementExtractor()
            Bank.NEO -> NeoStatementExtractor()
        }

        val records = extractor.extract(text)

        val output = ByteArrayOutputStream()
        CSVUtil.exportToCustomCsv(records, output, bank)

        return output.toByteArray()
    }

    private fun extractTextWithOcr(inputStream: InputStream): String {
        val fullText = StringBuilder()

        PDDocument.load(inputStream).use { document ->
            if (document.isEncrypted) {
                document.setAllSecurityToBeRemoved(true)
            }

            val renderer = PDFRenderer(document)
            val tesseract = Tesseract().apply {
                setDatapath(tesseractDataPath) // or your path
                setLanguage("eng")
            }

            for (page in 0 until document.numberOfPages) {
                val image: BufferedImage = renderer.renderImageWithDPI(page, 300f)
                fullText.append(tesseract.doOCR(image)).append("\n\n")
            }
        }

        return fullText.toString()
    }

    private fun extractTextEncrypted(inputStream: InputStream): String {
        PDDocument.load(inputStream).use { document ->
            if (document.isEncrypted) {
                document.setAllSecurityToBeRemoved(true)
            }

            return PDFTextStripper().getText(document)
        }
    }
}