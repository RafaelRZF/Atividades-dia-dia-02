import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Exercício 4 — Sistema de Controle de Estacionamento
 * Versão básica de console (dados em memória).
 *
 * Cobrança: até 1 hora = R$ 10,00
 *           acima de 1 hora = R$ 10,00 + R$ 5,00 por hora adicional
 *           (hora adicional iniciada conta como hora cheia)
 */
public class Estacionamento {

    static final int TOTAL_VAGAS = 5;
    static final double VALOR_PRIMEIRA_HORA = 10.00;
    static final double VALOR_HORA_ADICIONAL = 5.00;

    static class Veiculo {
        String placa;
        String tipo; // "carro" ou "moto"
        LocalDateTime entrada;

        Veiculo(String placa, String tipo, LocalDateTime entrada) {
            this.placa = placa;
            this.tipo = tipo;
            this.entrada = entrada;
        }
    }

    static List<Veiculo> estacionados = new ArrayList<>();
    static List<String> pagamentos = new ArrayList<>();
    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    static void carregarDadosDeExemplo() {
        // Veículos que "já estavam" no estacionamento, para facilitar o teste da cobrança
        estacionados.add(new Veiculo("ABC1234", "carro", LocalDateTime.now().minusHours(3).minusMinutes(30)));
        estacionados.add(new Veiculo("MOT0001", "moto", LocalDateTime.now().minusMinutes(40)));
    }

    static Veiculo buscarVeiculo(String placa) {
        for (Veiculo v : estacionados) {
            if (v.placa.equalsIgnoreCase(placa)) return v;
        }
        return null;
    }

    // CÁLCULO do valor total
    static double calcularValor(long minutos) {
        if (minutos <= 60) {
            return VALOR_PRIMEIRA_HORA;
        }
        long minutosAdicionais = minutos - 60;
        long horasAdicionais = (minutosAdicionais + 59) / 60; // arredonda para cima
        return VALOR_PRIMEIRA_HORA + horasAdicionais * VALOR_HORA_ADICIONAL;
    }

    static void registrarEntrada(Scanner sc) {
        System.out.print("Placa do veículo: ");
        String placa = sc.nextLine().trim().toUpperCase();
        System.out.print("Tipo do veículo (carro/moto): ");
        String tipo = sc.nextLine().trim().toLowerCase();

        // DECISÃO: existem vagas?
        if (estacionados.size() >= TOTAL_VAGAS) {
            System.out.println("Estacionamento lotado.");
            return;
        }

        // PROCESSAMENTO: registra horário de entrada, placa e tipo
        Veiculo v = new Veiculo(placa, tipo, LocalDateTime.now());
        estacionados.add(v);
        System.out.println("Entrada registrada: " + v.placa + " (" + v.tipo + ") às " + v.entrada.format(fmt));
        System.out.println("Vagas livres: " + (TOTAL_VAGAS - estacionados.size()));
    }

    static void registrarSaida(Scanner sc) {
        System.out.print("Placa do veículo: ");
        String placa = sc.nextLine().trim();

        Veiculo v = buscarVeiculo(placa);
        if (v == null) {
            System.out.println("Veículo não encontrado.");
            return;
        }

        // CÁLCULOS: tempo estacionado e valor total
        long minutos = Duration.between(v.entrada, LocalDateTime.now()).toMinutes();
        double valor = calcularValor(minutos);
        System.out.println("Tempo estacionado: " + (minutos / 60) + "h" + (minutos % 60) + "min");
        System.out.printf("Valor a pagar: R$ %.2f%n", valor);

        // ENTRADA: forma de pagamento
        System.out.print("Forma de pagamento (cartão/pix/dinheiro): ");
        String forma = sc.nextLine().trim();

        // DECISÃO: pagamento aprovado? (simulado)
        System.out.print("Pagamento aprovado? (s/n): ");
        String resposta = sc.nextLine().trim().toLowerCase();

        if (!resposta.equals("s")) {
            System.out.println("Pagamento não autorizado.");
            return;
        }

        // PROCESSAMENTOS AUTOMÁTICOS: registra pagamento e libera a saída
        pagamentos.add(v.placa + " | " + forma + " | R$ " + String.format("%.2f", valor));
        estacionados.remove(v);
        System.out.println("Saída liberada.");
    }

    // ---------- Fluxo do sistema ----------
    public static void main(String[] args) {
        carregarDadosDeExemplo();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== ESTACIONAMENTO ===");
        System.out.println("(placas de teste já estacionadas: ABC1234 = 3h30 | MOT0001 = 40min)");

        int opcao;
        do {
            System.out.println("\n1 - Registrar entrada");
            System.out.println("2 - Registrar saída");
            System.out.println("0 - Encerrar");
            System.out.print("Escolha: ");
            String linha = sc.nextLine().trim();
            opcao = linha.isEmpty() ? -1 : Integer.parseInt(linha);

            if (opcao == 1) {
                registrarEntrada(sc);
            } else if (opcao == 2) {
                registrarSaida(sc);
            } else if (opcao != 0) {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        System.out.println("Sistema encerrado.");
    }
}