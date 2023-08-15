package com.mycompany.arredondamentocomabnt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Arredondamentocomabnt {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite a faixa de arredondamento:");
        String faixaInput = scanner.nextLine();
        BigDecimal faixaArredondamento = new BigDecimal(faixaInput);

        BigDecimal NumInicio = new BigDecimal("-9999.9999");
        BigDecimal NumFinal = new BigDecimal("9999.9999");

        BigDecimal incrementar = new BigDecimal("0.0001");
        BigDecimal numero = NumInicio;
        BigDecimal numeroAnterior = null;

        try {
            FileWriter fileWriter = new FileWriter("resultado.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            while (numero.compareTo(NumFinal) <= 0) {
                BigDecimal parteDecimal = numero.remainder(BigDecimal.ONE).setScale(4, RoundingMode.HALF_UP);
                if ((parteDecimal.compareTo(new BigDecimal("0.4400")) >= 0 && parteDecimal.compareTo(new BigDecimal("0.5600")) <= 0) ||
                        (parteDecimal.compareTo(new BigDecimal("-0.5600")) >= 0 && parteDecimal.compareTo(new BigDecimal("-0.4400")) <= 0)) {
                    BigDecimal numeroArredondado = arredondar(numero, faixaArredondamento);
                    if (numeroAnterior == null || !parteDecimal.equals(numeroAnterior.remainder(BigDecimal.ONE).setScale(4, RoundingMode.HALF_UP))) {
                        String line = numero + "|" + numeroArredondado;
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                    numeroAnterior = numero;
                }
                numero = numero.add(incrementar);
            }

            bufferedWriter.close();
            fileWriter.close();

            System.out.println("O resultado foi salvo no arquivo 'resultado.csv'.");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo.");
        }

        scanner.close();
    }

    /**
     * Realiza o arredondamento de um número de acordo com a norma ABNT NBR ISO 80000-1:2011.
     *
     * @param numero: O número a ser arredondado.
     * @param faixaArredondamento: A faixa de arredondamento a ser usada.
     * @return: O número arredondado.
     */
    public static BigDecimal arredondar(BigDecimal numero, BigDecimal faixaArredondamento) {
        BigDecimal multiploInteiroAnterior = numero.divide(faixaArredondamento, 0, RoundingMode.DOWN);
        BigDecimal multiploInteiroSeguinte = multiploInteiroAnterior.add(BigDecimal.ONE);

        BigDecimal diferencaAnterior = numero.subtract(multiploInteiroAnterior.multiply(faixaArredondamento));
        BigDecimal diferencaSeguinte = multiploInteiroSeguinte.multiply(faixaArredondamento).subtract(numero);

        if (diferencaAnterior.compareTo(diferencaSeguinte) < 0) {
            return multiploInteiroAnterior.multiply(faixaArredondamento);
        } else if (diferencaAnterior.compareTo(diferencaSeguinte) > 0) {
            return multiploInteiroSeguinte.multiply(faixaArredondamento);
        } else {
            if (multiploInteiroAnterior.remainder(new BigDecimal("2")).equals(BigDecimal.ZERO)) {
                return multiploInteiroAnterior.multiply(faixaArredondamento);
            } else {
                return multiploInteiroSeguinte.multiply(faixaArredondamento);
            }
        }
    }
}
