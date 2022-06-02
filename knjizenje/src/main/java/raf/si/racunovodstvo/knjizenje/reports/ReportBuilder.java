package raf.si.racunovodstvo.knjizenje.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ReportBuilder {

    private static final String CREATOR = "RAF-RACUNOVODSTVO-SI";

    private ReportBuilder() {
    }

    /**
     * Generise izvestaj tabelarnog prikaza za odredjeni tip.
     *
     * @param author Korisnik koji generise izvestaj.
     * @param title  Naziv dokumenta.
     * @param rows   Sadrzaj dokumenta.
     * @param footer Zakljucni donji red dokumenta.
     * @return pdf u bajt streamu u koji su upisani bajtovi.
     * @throws DocumentException izuzetak biblioteke za pravljenje pdf-a kod citanja i pisanja.
     */
    public static ByteArrayOutputStream generateTableReport(String author,
                                                            String title,
                                                            List<List<String>> rows,
                                                            List<String> columns,
                                                            String footer) throws DocumentException {
        Document document = new Document(PageSize.LETTER, 0.75F, 0.75F, 0.75F, 0.75F);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, bytes);
        document.open();

        Paragraph header = new Paragraph(title);
        header.setSpacingAfter(10);
        document.add(header);

        PdfPTable table = new PdfPTable(columns.size());
        table.setWidthPercentage(80);
        columns.forEach(c -> table.addCell(new PdfPCell(new Phrase(c))));
        for (List<String> row : rows) {
            for (String val : row) {
                table.addCell(new PdfPCell(new Phrase(val)));
            }
        }
        document.add(table);

        document.add(new Paragraph(footer));

        document.addAuthor(author);
        document.addTitle(title);
        document.addCreator(CREATOR);

        document.close();
        return bytes;
    }
}
