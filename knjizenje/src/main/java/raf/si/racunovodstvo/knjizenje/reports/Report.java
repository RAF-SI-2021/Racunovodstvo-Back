package raf.si.racunovodstvo.knjizenje.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Report implements Reports {

    protected String title;
    protected String author;
    protected String footer;
}
