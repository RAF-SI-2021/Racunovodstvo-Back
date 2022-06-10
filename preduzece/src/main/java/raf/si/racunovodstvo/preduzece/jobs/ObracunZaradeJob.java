package raf.si.racunovodstvo.preduzece.jobs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.preduzece.services.impl.ObracunZaposleniService;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ObracunZaradeJob {
    final ObracunZaposleniService obracunZaposleniService;
    private ZonedDateTime nextDate;

    /**
     * dayOfMonth from 1 to 31
     */
    @Getter
    private int dayOfMonth = JobConstants.DEFAULT_DAY_OF_MONTH;

    public void setDayOfMonth(int dayOfMonth) throws DateTimeException {
        YearMonth yearMonth = YearMonth.of(nextDate.getYear(), nextDate.getMonthValue());
        if (yearMonth.lengthOfMonth() >= dayOfMonth)
            this.dayOfMonth = dayOfMonth;
        else throw new DateTimeException(
                "Izabrani dan [" + dayOfMonth + "]"
                        + " nije validan za mesec koji ima ["
                        + yearMonth.lengthOfMonth()
                        + "] u sledecoj iteraciji job-a.");
    }

    @Autowired
    public ObracunZaradeJob(ObracunZaposleniService obracunZaposleniService) {
        this.obracunZaposleniService = obracunZaposleniService;
        start();
    }

    public void start() {
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                ZonedDateTime now = ZonedDateTime.now();
                nextDate = now.plusMonths(1).withDayOfMonth(dayOfMonth);
                long delay = now.until(nextDate, ChronoUnit.MILLIS);
                try {
                    obracunZaposleniService.makeObracun(Date.from(now.toInstant()));
                } finally {
                    executor.schedule(this, delay, TimeUnit.MILLISECONDS);
                }
            }
        };

        ZonedDateTime dateTime = ZonedDateTime.now();
        if (dateTime.getDayOfMonth() >= dayOfMonth) {
            dateTime = dateTime.plusMonths(1);
        }
        dateTime = dateTime.withDayOfMonth(dayOfMonth);
        // TEST
        // Za test staviti dateTime = dateTime.withMonth(6).plusMinutes(1); ovo i zakomentarisati if iznad
        executor.schedule(task,
                ZonedDateTime.now().until(dateTime, ChronoUnit.MILLIS),
                TimeUnit.MILLISECONDS);
    }
}
