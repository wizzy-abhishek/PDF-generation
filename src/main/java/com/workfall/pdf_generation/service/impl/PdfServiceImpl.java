package com.workfall.pdf_generation.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.workfall.pdf_generation.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfServiceImpl implements PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfServiceImpl.class);

    @Override
    public ByteArrayInputStream createPdf() {

        log.info("PDF creation started");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);

            document.open();
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font underlinedFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE); // Underlined font

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);

            float[] columnWidths = {1.25f, 5};
            headerTable.setWidths(columnWidths);

            PdfPCell titleCell = new PdfPCell(new Phrase("RECEIPT", titleFont));
            titleCell.setBackgroundColor(new BaseColor(200, 200, 200));
            titleCell.setPadding(5);
            titleCell.setBorder(PdfPCell.NO_BORDER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(titleCell);

            PdfPCell subtitleCell = new PdfPCell(new Phrase("This serves as proof for any payment made.", subtitleFont));
            subtitleCell.setBackgroundColor(new BaseColor(200, 200, 200));
            subtitleCell.setPadding(5);
            subtitleCell.setBorder(PdfPCell.NO_BORDER);
            subtitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(subtitleCell);

            PdfPCell lineCell = new PdfPCell();
            lineCell.setBorder(PdfPCell.NO_BORDER);
            lineCell.setMinimumHeight(5);
            lineCell.setColspan(2);
            lineCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerTable.addCell(lineCell);

            document.add(headerTable);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph dateParagraph = new Paragraph();
            dateParagraph.add(new Chunk("Date: ", contentFont));
            dateParagraph.add(new Chunk(LocalDate.now().format(dateFormatter), underlinedFont));
            dateParagraph.setSpacingBefore(20);
            dateParagraph.setSpacingAfter(10);
            document.add(dateParagraph);

            Paragraph vehicleInfoParagraph = new Paragraph();
            vehicleInfoParagraph.add(new Chunk("The below payment is made pertaining to the vehicle bearing registration number: ", contentFont));
            vehicleInfoParagraph.add(new Chunk("120345678", underlinedFont));
            vehicleInfoParagraph.setSpacingAfter(20);
            document.add(vehicleInfoParagraph);


            PdfPTable paymentTable = new PdfPTable(2);
            paymentTable.setWidths(new float[]{3,8});
            paymentTable.setWidthPercentage(100);
            paymentTable.setSpacingBefore(10);
            paymentTable.setSpacingAfter(10);

            addTableHeader(paymentTable, "PAYMENT DETAILS");
            addTableRow(paymentTable, "Amount", "120,145.0", contentFont);
            addTableRow(paymentTable, "Purpose", "dfghnjm", contentFont);
            addTableRow(paymentTable, "Mode of payment", "124563789", contentFont);
            addTableRow(paymentTable, "Payment reference No", "1254789630", contentFont);

            document.add(paymentTable);

            PdfPTable paidByTable = new PdfPTable(2);
            paidByTable.setSpacingBefore(10);
            paymentTable.setSpacingAfter(10);
            paidByTable.setWidths(new float[]{3,8});
            paidByTable.setWidthPercentage(100);

            addTableHeader(paidByTable, "PAID BY");
            addTableRow(paidByTable, "Name", "Abhishek", contentFont);
            addTableRow(paidByTable, "NRIC No.", "8527410", contentFont);
            addSignatureRowWithImage(paidByTable, "Signature", "src/main/resources/static/sign2.png");

            document.add(paidByTable);

            PdfPTable receivedByTable = new PdfPTable(2);
            receivedByTable.setWidths(new float[]{3,8});
            receivedByTable.setWidthPercentage(100);
            receivedByTable.setSpacingBefore(10);

            addTableHeader(receivedByTable, "RECEIVED BY");
            addTableRow(receivedByTable, "Name", "Anand", contentFont);
            addTableRow(receivedByTable, "NRIC No", "123456789", contentFont);
            addSignatureRowWithImage(receivedByTable, "Signature", "src/main/resources/static/sign1.png");

            document.add(receivedByTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void addTableHeader(PdfPTable table, String headerText) {
        PdfPCell header = new PdfPCell(new Phrase(headerText, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPadding(5);
        header.setHorizontalAlignment(Element.ALIGN_LEFT);
        header.setColspan(2);
        table.addCell(header);
    }

    private void addTableRow(PdfPTable table, String label, String value, Font font) {

        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(PdfPCell.BOX);
        labelCell.setPadding(5);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(PdfPCell.BOX);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
    }

    private void addSignatureRowWithImage(PdfPTable table, String label, String imagePath) {
        try {

            PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            labelCell.setPaddingBottom(10);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(labelCell);

            Image signature = Image.getInstance(imagePath);
            signature.scaleToFit(150, 60);
            signature.setAlignment(Image.ALIGN_LEFT);

            PdfPCell imageCell = new PdfPCell();
            imageCell.setFixedHeight(80);
            imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            imageCell.addElement(signature);
            table.addCell(imageCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
