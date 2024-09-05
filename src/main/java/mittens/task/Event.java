package mittens.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public LocalDate getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " ("
                + this.from.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) + " -- "
                + this.to.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) + ")";
    }
}
