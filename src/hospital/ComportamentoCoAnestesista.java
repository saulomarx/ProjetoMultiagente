package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;
import java.util.HashMap;
import java.util.Map;

public class ComportamentoCoAnestesista extends SimpleBehaviour {

    private boolean fim = false;
    private int[][] horarios = new int[24][3];
    private Map<Integer, Boolean> bancoProtocolos = new HashMap<Integer, Boolean>();

    public ComportamentoCoAnestesista(Agent a) {
        super(a);
        for (int i = 0, l = horarios.length; i < l; i++) {
            for (int j = 0, m = horarios[i].length; j < m; j++) {
                horarios[i][j] = 0;
            }
        }
    }

    @Override
    public void action() {

        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();
        if (mensagemRecebida != null) {

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }

            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1], horario = aux[2];

            int idMenssagem = Integer.parseInt(mensagemRecebida.getConversationId());
            int hora = Integer.parseInt(horario);

            if (codigoDaAcao.equalsIgnoreCase("N")) {
                cancelaHorario(hora, idMenssagem);
                System.out.println(myAgent.getLocalName() + ": Notificado");

            } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                confirmaHorario(hora, idMenssagem);
                System.out.println(myAgent.getLocalName() + ": Reservado");

            } else if (codigoDaAcao.equalsIgnoreCase("C")) {
                String situacao = "F";
                if (getDisponibilidade(hora, idMenssagem)) {
                    situacao = "T";
                    reservaHorario(hora, idMenssagem);
                }
                ACLMessage resposta = mensagemRecebida.createReply();
                resposta.setPerformative(ACLMessage.INFORM);
                resposta.setContent("01001;" + situacao + ";" + horario);
                myAgent.send(resposta);
            }
            imprimirHorarios();

        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    private boolean getDisponibilidade(int hora, int idMessaegem) {
        for (int i = 0, l = horarios[hora].length; i < l; i++) {
            if (horarios[hora][i] == 0) {
                return true;
            }
        }
        return false;
    }

    private void reservaHorario(int hora, int idMessaegem) {
        for (int i = 0, l = horarios[hora].length; i < l; i++) {
            if (horarios[hora][i] == 0) {
                horarios[hora][i] = -1;
                return;
            }
        }

    }

    private void cancelaHorario(int hora, int idMessaegem) {
        for (int i = 0, l = horarios[hora].length; i < l; i++) {
            if (horarios[hora][i] == -1) {
                horarios[hora][i] = 0;
                return;
            }
            return;
        }

    }

    private void confirmaHorario(int hora, int idMessaegem) {
        for (int i = 0, l = horarios[hora].length; i < l; i++) {
            if (horarios[hora][i] == -1) {
                horarios[hora][i] = 1;
                return;
            }
        }

    }

    private void imprimirHorarios() {
        String saida = "\n\t\t" + myAgent.getLocalName() + "\t\t\n0-> livre\t-1-> Reservado\t1-> Indsponivel\n"+"--------------------------------------"+"\n----\t\tF1\tF2\tF3\n";
        for (int i = 0, l = horarios.length; i < l; i++) {
            saida += "Horario" + Integer.toString(i) + ":\t";
            for (int j = 0, m = horarios[i].length; j < m; j++) {
                saida += Integer.toString(horarios[i][j]) + "\t";
            }
            saida += "\n";
        }
        System.out.println(saida);

    }

    @Override
    public boolean done() {
        return fim;
    }
}
