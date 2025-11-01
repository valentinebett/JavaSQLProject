package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.FileOutputStream;

public class ChartPDFExporter {

    public static void exportToPDF(JFreeChart chart, String fileName, String interpretation) {
        try {
            // Save chart as an image first
            File imageFile = new File(fileName.replace(".pdf", ".png"));
            ChartUtils.saveChartAsPNG(imageFile, chart, 800, 500);

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Hospital Data Visualization", titleFont));
            document.add(new Paragraph("\n"));

            Image chartImage = Image.getInstance(imageFile.getAbsolutePath());
            chartImage.scaleToFit(750, 400);
            chartImage.setAlignment(Element.ALIGN_CENTER);
            document.add(chartImage);

            Font textFont = new Font(Font.FontFamily.HELVETICA, 12);
            document.add(new Paragraph("\nInterpretation:", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
            document.add(new Paragraph(interpretation, textFont));

            document.close();
            System.out.println("✅ Exported PDF: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
