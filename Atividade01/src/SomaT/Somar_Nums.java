package SomaT;

import java.util.*;

class Somar_Nums {
    // O Scanner deve ficar dentro da classe para ser acessível pelos métodos
    public static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) {
        // Declaração de variáveis
        int num1, num2, soma;

        // Leituras
        System.out.println("Digite um número:");
        num1 = sc.nextInt();

        System.out.println("Digite outro número:");
        num2 = sc.nextInt();

        // Somar
        soma = num1 + num2;

        // Mostrar na tela
        System.out.println("Soma: " + soma);
        
        // Boa prática: fechar o scanner ao finalizar
        sc.close();
    }
}