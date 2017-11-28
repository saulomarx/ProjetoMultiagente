package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoCoCentroCirurgico extends SimpleBehaviour {

    private boolean fim = false;
    private boolean disponibilidade = false;

    public ComportamentoCoCentroCirurgico(Agent a) {
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
                System.out.println(myAgent.getLocalName() + ": Notificado");
            } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                disponibilidade = false;
                System.out.println(myAgent.getLocalName() + ": Reservado");
            } else if (codigoDaAcao.equalsIgnoreCase("C")) {
                String situacao = "F";
                if (disponibilidade) {
                    situacao = "T";
                }
                ACLMessage resposta = mensagemRecebida.createReply();
                resposta.setPerformative(ACLMessage.INFORM);
                resposta.setContent("00100;" + situacao);
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