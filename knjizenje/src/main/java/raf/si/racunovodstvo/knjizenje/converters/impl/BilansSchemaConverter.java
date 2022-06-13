package raf.si.racunovodstvo.knjizenje.converters.impl;

import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converters.IConverter;
import raf.si.racunovodstvo.knjizenje.reports.schema.BilansSchema;
import raf.si.racunovodstvo.knjizenje.responses.BilansResponse;

@Service
public class BilansSchemaConverter implements IConverter<BilansResponse, BilansSchema> {

    public BilansSchema convert(BilansResponse source) {
        return new BilansSchema(source.getBrojKonta(),
                                String.valueOf(source.getBrojStavki()),
                                source.getNazivKonta(),
                                String.valueOf(source.getDuguje()),
                                String.valueOf(source.getPotrazuje()),
                                String.valueOf(source.getDuguje() - source.getPotrazuje()));
    }
}
