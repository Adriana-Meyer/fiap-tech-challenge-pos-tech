package com.fiap.workshop.management.domain.model.vehicle;

import com.fiap.workshop.management.domain.exception.InvalidLicensePlateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LicensePlate")
class LicensePlateTest {

    @Test
    @DisplayName("should accept legacy format ABC1234")
    void shouldAcceptLegacyFormat() {
        LicensePlate plate = new LicensePlate("ABC1234");
        assertEquals("ABC1234", plate.getValue());
    }

    @Test
    @DisplayName("should accept Mercosul format ABC1D23")
    void shouldAcceptMercosulFormat() {
        LicensePlate plate = new LicensePlate("ABC1D23");
        assertEquals("ABC1D23", plate.getValue());
    }

    @Test
    @DisplayName("should normalize plate when input has hyphen")
    void shouldNormalizePlateWhenInputHasHyphen() {
        LicensePlate plate = new LicensePlate("ABC-1234");
        assertEquals("ABC1234", plate.getValue());
    }

    @Test
    @DisplayName("should normalize plate when input is lowercase")
    void shouldNormalizePlateWhenInputIsLowercase() {
        LicensePlate plate = new LicensePlate("abc1234");
        assertEquals("ABC1234", plate.getValue());
    }

    @Test
    @DisplayName("should normalize Mercosul plate when input is lowercase with hyphen")
    void shouldNormalizeMercosulPlateWhenInputIsLowercaseWithHyphen() {
        LicensePlate plate = new LicensePlate("abc-1d23");
        assertEquals("ABC1D23", plate.getValue());
    }

    @Test
    @DisplayName("should throw InvalidLicensePlateException when format is invalid")
    void shouldThrowWhenFormatIsInvalid() {
        assertThrows(InvalidLicensePlateException.class, () -> new LicensePlate("INVALID99"));
    }

    @Test
    @DisplayName("should throw InvalidLicensePlateException when plate is too short")
    void shouldThrowWhenPlateIsTooShort() {
        assertThrows(InvalidLicensePlateException.class, () -> new LicensePlate("ABC123"));
    }

    @Test
    @DisplayName("should throw InvalidLicensePlateException when Mercosul pattern is invalid")
    void shouldThrowWhenMercosulPatternIsInvalid() {
        assertThrows(InvalidLicensePlateException.class, () -> new LicensePlate("ABC1123"));
    }

    @Test
    @DisplayName("should return plate with hyphen when calling formatted on legacy plate")
    void shouldReturnPlateWithHyphenWhenCallingFormattedOnLegacyPlate() {
        assertEquals("ABC-1234", new LicensePlate("ABC1234").formatted());
    }

    @Test
    @DisplayName("should return plate with hyphen when calling formatted on Mercosul plate")
    void shouldReturnPlateWithHyphenWhenCallingFormattedOnMercosulPlate() {
        assertEquals("ABC-1D23", new LicensePlate("ABC1D23").formatted());
    }

    @Test
    @DisplayName("should be equal when plates have same normalized value")
    void shouldBeEqualWhenPlatesHaveSameNormalizedValue() {
        assertEquals(new LicensePlate("ABC1234"), new LicensePlate("abc-1234"));
    }

    @Test
    @DisplayName("should have consistent hashCode with equals")
    void shouldHaveConsistentHashCodeWithEquals() {
        assertEquals(
                new LicensePlate("ABC1234").hashCode(),
                new LicensePlate("abc-1234").hashCode());
    }
}
