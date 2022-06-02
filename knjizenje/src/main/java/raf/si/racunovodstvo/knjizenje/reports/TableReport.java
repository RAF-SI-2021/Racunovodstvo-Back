package raf.si.racunovodstvo.knjizenje.reports;

import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableReport extends Report {
    private List<String> columns;
    private List<List<String>> rows;

    public TableReport(String author, String title, String footer, List<String> columns, List<List<String>> rows) {
        super(title, author, footer);
        setColumns(columns);
        setRows(rows);
    }

    @Override
    public byte[] getReport() throws DocumentException {
        return ReportBuilder.generateTableReport(author, title, rows, columns, footer).toByteArray();
    }
}
