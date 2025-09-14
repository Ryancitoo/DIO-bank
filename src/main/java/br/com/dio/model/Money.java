package br.com.dio.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode
@ToString
@Getter
public class Money {

    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(final MoneyAudit audit) {
        history.add(audit);
    }

    public void addHistory(final MoneyAudit audit) {
        history.add(audit);
    }

    public List<MoneyAudit> getHistory() {
        return history;
    }
}
