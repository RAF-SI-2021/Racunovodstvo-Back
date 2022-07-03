package raf.si.racunovodstvo.knjizenje.reports;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportBuilderTest {

    @Test
    public void testGenerateTableReportZeroRows() {
        assertThrows(IllegalArgumentException.class, () -> ReportBuilder.generateTableReport("", "", List.of(List.of()), List.of(), ""));
    }

    @Test
    public void testGenerateTableReport() throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = ReportBuilder.generateTableReport("", "", List.of(List.of()), List.of("column"), "");
        assertEquals(1003, byteArrayOutputStream.size());
    }
}