package com.fiap.workshop.management.domain.model.vehicle;

import com.fiap.workshop.management.domain.exception.InvalidLicensePlateException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class LicensePlate {

    private static final Pattern LEGACY   = Pattern.compile("^[A-Z]{3}[0-9]{4}$");
    private static final Pattern MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    private final String value;

    public LicensePlate(String raw) {
        String normalized = raw.toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (!LEGACY.matcher(normalized).matches() && !MERCOSUL.matcher(normalized).matches()) {
            throw new InvalidLicensePlateException(raw);
        }
        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    public String formatted() {
        return value.substring(0, 3) + "-" + value.substring(3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicensePlate that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return formatted();
    }
}
