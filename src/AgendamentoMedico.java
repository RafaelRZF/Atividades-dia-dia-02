import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Exercício 3 — Sistema de Agendamento Médico
 * Versão básica de console (dados em memória).
 */
public class AgendamentoMedico {

    // ---------- "Banco de dados" em memória ----------
    static class Paciente {
        String cpf;
        String nome;

        Paciente(String cpf, String nome) {
            this.cpf = cpf;
            this.nome = nome;
        }
    }

    static class Horario {
        String especialidade;
        String hora;
        boolean livre;

        Horario(String especialidade, String hora, boolean livre) {
            this.especialidade = especialidade;
            this.hora = hora;
            this.livre = livre;
        }
    }

    static class Consulta {
        String cpf;
        String especialidade;
        String hora;

        Consulta(String cpf, String especialidade, String hora) {
            this.cpf = cpf;
            this.especialidade = especialidade;
            this.hora = hora;
        }
    }

    static List<Paciente> pacientes = new ArrayList<>();
    static List<Horario> horarios = new ArrayList<>();
    static List<Consulta> consultas = new ArrayList<>();

    static void carregarDadosDeExemplo() {
        pacientes.add(new Paciente("12345678900", "Maria"));
        pacientes.add(new Paciente("11122233344", "João"));

        horarios.add(new Horario("Cardiologia", "09:00", true));
        horarios.add(new Horario("Cardiologia", "10:00", true));
        horarios.add(new Horario("Cardiologia", "11:00", false)); // já ocupado
        horarios.add(new Horario("Dermatologia", "14:00", true));
        horarios.add(new Horario("Ortopedia", "15:00", false));   // sem horários livres
    }

    static Paciente buscarPaciente(String cpf) {
        for (Paciente p : pacientes) {
            if (p.cpf.equals(cpf)) return p;
        }
        return null;
    }

    static List<Horario> horariosLivres(String especialidade) {
        List<Horario> livres = new ArrayList<>();
        for (Horario h : horarios) {
            if (h.especialidade.equalsIgnoreCase(especialidade) && h.livre) livres.add(h);
        }
        return livres;
    }

    static Horario buscarHorario(String especialidade, String hora) {
        for (Horario h : horarios) {
            if (h.especialidade.equalsIgnoreCase(especialidade) && h.hora.equals(hora)) return h;
        }
        return null;
    }

    // ---------- Fluxo do sistema ----------
    public static void main(String[] args) {
        carregarDadosDeExemplo();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== AGENDAMENTO MÉDICO ===");
        System.out.println("(CPFs de teste: 12345678900 = Maria | 11122233344 = João)");
        System.out.println("(Especialidades: Cardiologia, Dermatologia, Ortopedia)");

        // ENTRADAS
        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Especialidade desejada: ");
        String especialidade = sc.nextLine().trim();

        // DECISÃO 1: paciente possui cadastro?
        Paciente paciente = buscarPaciente(cpf);
        if (paciente == null) {
            System.out.println("Paciente não cadastrado.");
            System.out.println("Sistema encerrado.");
            return;
        }

        // DECISÃO 2: existem horários disponíveis para a especialidade?
        List<Horario> livres = horariosLivres(especialidade);
        if (livres.isEmpty()) {
            System.out.println("Não há horários disponíveis.");
            System.out.println("Sistema encerrado.");
            return;
        }

        // SAÍDA: apresenta os horários livres
        System.out.println("Horários livres:");
        for (Horario h : livres) {
            System.out.println(" - " + h.hora);
        }

        // ENTRADA: paciente escolhe um horário
        System.out.print("Escolha um horário (ex: 09:00): ");
        String horaEscolhida = sc.nextLine().trim();

        // DECISÃO 3 (verificação): o horário ainda está disponível?
        Horario escolhido = buscarHorario(especialidade, horaEscolhida);
        if (escolhido == null || !escolhido.livre) {
            System.out.println("Horário indisponível.");
            System.out.println("Sistema encerrado.");
            return;
        }

        // PROCESSAMENTOS: registra consulta e ocupa o horário
        escolhido.livre = false;
        consultas.add(new Consulta(paciente.cpf, escolhido.especialidade, escolhido.hora));
        System.out.println("Enviando confirmação para " + paciente.nome + "...");

        System.out.println("Consulta agendada com sucesso.");
        System.out.println("Sistema encerrado.");
    }
}