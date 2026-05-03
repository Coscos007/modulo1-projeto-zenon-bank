package br.com.zenon.report;

import java.util.Locale;

public enum Language {
    PORTUGUESE(Locale.of("pt", "BR")),
    ENGLISH(Locale.US),
    ;

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
