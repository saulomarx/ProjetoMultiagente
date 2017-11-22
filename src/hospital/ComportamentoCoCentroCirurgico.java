package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoCoCentroCirurgico extends SimpleBehaviour {

    private boolean fim = false;
    private boolean disponibilidade = true;

    public ComportamentoCoCentroCirurgico(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();
        if (mensagemRecebida != null) {
            ACLMessage resposta = mensagemRecebida.createReply();
            resposta.setPerformative(ACLMessage.INFORM);
            resposta.setContent("00100;T");
            myAgent.send(resposta);

        } else {
            System.out.println("CentroCirurgico: Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    @Override
    public boolean done() {
        return fim;
    }
}
