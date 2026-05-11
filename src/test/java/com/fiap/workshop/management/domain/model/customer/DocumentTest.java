package com.fiap.workshop.management.domain.model.customer;

import com.fiap.workshop.management.domain.exception.InvalidDocumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Document")
class DocumentTest {

    @Test
    @DisplayName("should detect CPF type when document has 11 digits")
    void shouldDetectCpfTypeWhenDocumentHas11Digits() {
        Document doc = new Document("529.982.247-25");
        assertEquals(Document.DocumentType.CPF, doc.getType());
        assertEquals("52998224725", doc.getValue());
    }

    @Test
    @DisplayName("should accept CPF without formatting")
    void shouldAcceptCpfWithoutFormatting() {
        Document doc = new Document("52998224725");
        assertEquals(Document.DocumentType.CPF, doc.getType());
    }

    @Test
    @DisplayName("should detect CNPJ type when document has 14 digits")
    void shouldDetectCnpjTypeWhenDocumentHas14Digits() {
        Document doc = new Document("11.222.333/0001-81");
        assertEquals(Document.DocumentType.CNPJ, doc.getType());
        assertEquals("11222333000181", doc.getValue());
    }

    @Test
    @DisplayName("should accept CNPJ without formatting")
    void shouldAcceptCnpjWithoutFormatting() {
        Document doc = new Document("11222333000181");
        assertEquals(Document.DocumentType.CNPJ, doc.getType());
    }

    @Test
    @DisplayName("should throw InvalidDocumentException when CPF has all equal digits")
    void shouldThrowWhenCpfHasAllEqualDigits() {
        assertThrows(InvalidDocumentException.class, () -> new Document("111.111.111-11"));
    }

    @Test
    @DisplayName("should throw InvalidDocumentException when CPF checksum is invalid")
    void shouldThrowWhenCpfChecksumIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> new Document("529.982.247-26"));
    }

    @Test
    @DisplayName("should throw InvalidDocumentException when CNPJ has all equal digits")
    void shouldThrowWhenCnpjHasAllEqualDigits() {
        assertThrows(InvalidDocumentException.class, () -> new Document("11.111.111/1111-11"));
    }

    @Test
    @DisplayName("should throw InvalidDocumentException when CNPJ checksum is invalid")
    void shouldThrowWhenCnpjChecksumIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> new Document("11.222.333/0001-82"));
    }

    @Test
    @DisplayName("should throw InvalidDocumentException when document length is invalid")
    void shouldThrowWhenDocumentLengthIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> new Document("12345"));
    }

    @Test
    @DisplayName("should format CPF as 000.000.000-00")
    void shouldFormatCpfWithDotsAndDash() {
        Document doc = new Document("52998224725");
        assertEquals("529.982.247-25", doc.formatted());
    }

    @Test
    @DisplayName("should format CNPJ as 00.000.000/0000-00")
    void shouldFormatCnpjWithDotsSlashAndDash() {
        Document doc = new Document("11222333000181");
        assertEquals("11.222.333/0001-81", doc.formatted());
    }

    @Test
    @DisplayName("should be equal when documents have same digits regardless of formatting")
    void shouldBeEqualWhenDocumentsHaveSameDigits() {
        assertEquals(new Document("52998224725"), new Document("529.982.247-25"));
    }

    @Test
    @DisplayName("should have consistent hashCode with equals")
    void shouldHaveConsistentHashCodeWithEquals() {
        assertEquals(
                new Document("52998224725").hashCode(),
                new Document("529.982.247-25").hashCode());
    }
}
