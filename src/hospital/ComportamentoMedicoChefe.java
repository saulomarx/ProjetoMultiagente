package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoMedicoChefe extends SimpleBehaviour {

    private boolean fim = false;

    public ComportamentoMedicoChefe(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();
        if (mensagemRecebida != null) {
            ACLMessage resposta =  mensagemRecebida.createReply();
            resposta.setPerformative(ACLMessage.INFORM);
            resposta.setContent("01000;T");
            myAgent.send(resposta);
     
            
//            System.out.println(myAgent.getLocalName() + ": Acaba de receber a seguinte mensagem: ");
//            
//            System.out.println("OIEEEEEEEEEEEEEEEE "+mensagemRecebida.getSender());
//            System.out.println(mensagemRecebida.toString());
//            System.out.println("PreparandoResposta");
//            ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
//            //Preencher os campos necesários da mensagem
//            mensagemParaEnvio.setSender(myAgent.getAID());
//            mensagemParaEnvio.addReceiver(new AID("CoTransplante", AID.ISLOCALNAME));
//            mensagemParaEnvio.setContent("1");
//            //Envia a mensagem aos destinatarios
//            myAgent.send(mensagemParaEnvio);
//            System.out.println(myAgent.getLocalName() + ": Enviando olá ao receptor");
//            System.out.println(myAgent.getLocalName() + "\n" + mensagemParaEnvio.toString());
//            fim = true;
        } else {
            System.out.println("MedicoChefe: Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    @Override
    public boolean done() {
        return fim;
    }
}
