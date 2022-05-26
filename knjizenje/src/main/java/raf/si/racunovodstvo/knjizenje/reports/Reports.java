package raf.si.racunovodstvo.knjizenje.reports;

import com.itextpdf.text.DocumentException;

public interface Reports {

    byte[] getReport() throws DocumentException;
}
