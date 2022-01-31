package it.vodafone.test.enumeration;

public enum Gender {
    MALE, FEMALE;

    public static Gender getGenderFromTaxCode(String genderDay) {
        int gender = Integer.parseInt(genderDay);
        if (gender >= 1 && gender <= 31) {
            return Gender.MALE;
        }

        if (gender >= 41 && gender <= 71) {
            return Gender.FEMALE;
        }

        return null;
    }
}
