package com.fiap.workshop.management.domain.model.customer;

import com.fiap.workshop.management.domain.exception.InvalidDocumentException;
import com.fiap.workshop.management.domain.model.shared.CpfValidator;

import java.util.Objects;

public final class Document {

    public enum DocumentType {
        CPF, CNPJ
    }

    private final String value;
    private final DocumentType type;

    public Document(String raw) {
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() == 11) {
            validateCpf(digits);
            this.type = DocumentType.CPF;
        } else if (digits.length() == 14) {
            validateCnpj(digits);
            this.type = DocumentType.CNPJ;
        } else {
            throw new InvalidDocumentException(raw);
        }
        this.value = digits;
    }

    public String getValue() {
        return value;
    }

    public DocumentType getType() {
        return type;
    }

    public String formatted() {
        if (type == DocumentType.CPF) {
            return value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return value.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private static void validateCpf(String digits) {
        if (!CpfValidator.isValid(digits)) throw new InvalidDocumentException(digits);
    }

    private static void validateCnpj(String digits) {
        if (digits.chars().distinct().count() == 1) throw new InvalidDocumentException(digits);

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) sum += (digits.charAt(i) - '0') * weights1[i];
        int first = 11 - (sum % 11);
        if (first >= 10) first = 0;

        sum = 0;
        for (int i = 0; i < 13; i++) sum += (digits.charAt(i) - '0') * weights2[i];
        int second = 11 - (sum % 11);
        if (second >= 10) second = 0;

        if (first != (digits.charAt(12) - '0') || second != (digits.charAt(13) - '0')) {
            throw new InvalidDocumentException(digits);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document document)) return false;
        return Objects.equals(value, document.value);
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
