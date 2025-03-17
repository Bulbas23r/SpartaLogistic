package common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(nullable = false)
    private String roadAddress;
    @Column(nullable = false)
    private String jibunAddress;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String town;
    @Column(nullable = false)
    private String postalCode;

    @Builder
    public Address(String roadAddress, String jibunAddress, String city, String district,
        String town, String postalCode) {
        validateNotEmpty(roadAddress, "도로명 주소(roadAddress)");
        validateNotEmpty(city, "도/시(city)");
        validateNotEmpty(district, "구/군(district)");
        validateNotEmpty(town, "읍/면/동(state)");
        validateNotEmpty(postalCode, "우편번호(postalCode)");

        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.city = city;
        this.district = district;
        this.town = town;
        this.postalCode = postalCode;
    }

    private void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "는 비워둘 수 없습니다.");
        }
    }

}
