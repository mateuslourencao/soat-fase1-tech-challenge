package com.oficina.common.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        // Remove non-digits
        String digits = value.replaceAll("\\D", "");

        if (digits.length() == 11) {
            return isValidCPF(digits);
        } else if (digits.length() == 14) {
            return isValidCNPJ(digits);
        }

        return false;
    }

    private boolean isValidCPF(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) return false;

        int[] weight = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        
        int d1 = calculateDigit(cpf.substring(0, 9), weight);
        int d2 = calculateDigit(cpf.substring(0, 9) + d1, weight);
        
        return cpf.equals(cpf.substring(0, 9) + d1 + d2);
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        
        int d1 = calculateDigit(cnpj.substring(0, 12), weight1);
        int d2 = calculateDigit(cnpj.substring(0, 12) + d1, weight2);
        
        return cnpj.equals(cnpj.substring(0, 12) + d1 + d2);
    }

    private int calculateDigit(String str, int[] weight) {
        int sum = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(str.substring(i, i + 1));
            sum += digit * weight[weight.length - str.length() + i];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }
}
