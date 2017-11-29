package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoCoAnestesista extends SimpleBehaviour {

    private boolean fim = false;
    private int disponibilidade = 0;

    public ComportamentoCoAnestesista(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();
        if (mensagemRecebida != null) {
            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1];
            if (codigoDaAcao.equalsIgnoreCase("N")) {
                disponibilidade = 0;
                System.out.println(myAgent.getLocalName() + ": Notificado");
            } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                disponibilidade = 1;
                System.out.println(myAgent.getLocalName() + ": Reservado");
            } else if (codigoDaAcao.equalsIgnoreCase("C")) {
                String situacao = "F";
                if (disponibilidade == 0) {
                    situacao = "T";
                    disponibilidade = -1;
                }
                ACLMessage resposta = mensagemRecebida.createReply();
                resposta.setPerformative(ACLMessage.INFORM);
                resposta.setContent("01001;" + situacao);
                myAgent.send(resposta);
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    @Override
    public boolean done() {
        return fim;
    }
}
