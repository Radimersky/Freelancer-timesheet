package domain;

import ui.I18N;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {

    private final I18N i18n = new I18N(getClass());
    private List<TimeEntry> timeEntries;
    private Client client;
    private Client issuer;
    private LocalDate createDate;
    private LocalDate dueDate;

    public Invoice(){}

    public Invoice(Client issuer, Client client, LocalDate createDate, LocalDate dueDate, List<TimeEntry> selectedTimeEntries) {
        this.client = client;
        this.issuer = issuer;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.timeEntries = selectedTimeEntries;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getIssuer() {
        return issuer;
    }

    public void setIssuer(Client issuer) {
        this.issuer = issuer;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public File createInvoice() throws IOException {
        StringBuilder fileName = new StringBuilder(String.format("%s_%s.txt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")), this.client.getLastName()));
        File invoiceFile = new File(fileName.toString());

        while (!invoiceFile.createNewFile()) {
            fileName.append("_");
            invoiceFile = new File(fileName.toString());
        }

        FileWriter myWriter = new FileWriter(fileName.toString());
        myWriter.write(buildInvoice());
        myWriter.close();

        return invoiceFile;
    }

    private String buildInvoice() {
        StringBuilder builder = new StringBuilder();
        final int width = 53;

        String issuerRow = String.format("%-12s %s %s", i18n.getString("issuer"), issuer.getFirstName(), issuer.getLastName());
        String clientRow = String.format("%-12s %s %s", i18n.getString("client"), client.getFirstName(), client.getLastName());
        String createDateRow = String.format("%-12s %s", i18n.getString("createDate"), createDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        String dueDateRow = String.format("%-12s %s", i18n.getString("dueDate"), dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        String workDoneHeaderRow = String.format("|%-25s|%-12s|%-12s|", i18n.getString("workType"), i18n.getString("hoursTotal"), i18n.getString("hourRate"));

        builder.append(issuerRow).append("\n");
        builder.append(clientRow).append("\n");
        builder.append(createDateRow).append("\n");
        builder.append(dueDateRow).append("\n\n");
        builder.append("-".repeat(width)).append("\n");
        builder.append(workDoneHeaderRow).append("\n");
        builder.append("-".repeat(width)).append("\n");

        BigDecimal sumTotal = new BigDecimal(0);
        for (TimeEntry timeEntry : this.timeEntries) {
            double hourDuration = timeEntry.getDuration().toMinutes() / 60D;
            int hourRate = timeEntry.getWorkType().getRate();
            String hourDurationString = String.format("%s:%02d", timeEntry.getDuration().toHours(), timeEntry.getDuration().toMinutes() % 60);

            builder.append(String.format("|%-25s", timeEntry.getWorkType().getName()));
            builder.append(String.format("|%12s|", hourDurationString));
            builder.append(String.format("%12s|", hourRate)).append("\n");

            sumTotal = sumTotal.add(new BigDecimal(hourRate * hourDuration));
        }

        String sumTotalRow = String.format("%s: %sKč", i18n.getString("sumTotal"), sumTotal.toString());

        builder.append("-".repeat(width)).append("\n");
        builder.append(sumTotalRow);

        return builder.toString();
    }
}
