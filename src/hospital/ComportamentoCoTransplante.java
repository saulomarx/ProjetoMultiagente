package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;
import jade.lang.acl.*;

public class ComportamentoCoTransplante extends SimpleBehaviour {

    boolean fim = false;
    boolean enviouMensagen = false;

    public ComportamentoCoTransplante(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage mensagemRecebida = myAgent.receive();
        if (!enviouMensagen) {
            System.out.println(myAgent.getLocalName() + ": Preparando para enviar una mensagem ao receptor");
            // Criação do objeto ACLMessage
            ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
            //Preencher os campos necesários da mensagem
            mensagemParaEnvio.setSender(myAgent.getAID());
            mensagemParaEnvio.addReceiver(new AID("CoHospital", AID.ISLOCALNAME));
            mensagemParaEnvio.setContent("00001;C");
            mensagemParaEnvio.addReplyTo(new AID("CoTransplante", AID.ISLOCALNAME));
            //Envia a mensagem aos destinatarios
            myAgent.send(mensagemParaEnvio);
            System.out.println(myAgent.getLocalName() + ": Enviando olá ao receptor");
            System.out.println(myAgent.getLocalName() + "\n" + mensagemParaEnvio.toString());
            enviouMensagen = !enviouMensagen;
        } else if (mensagemRecebida != null) {
            System.out.println(myAgent.getLocalName() + ": Acaba de receber a seguinte mensagem: ");
            System.out.println(mensagemRecebida.getContent());
            fim = true;
        } else {
            System.out.println("Aguardando resposta");
            block();
        }

    }

    @Override
    public boolean done() {
        return fim;
    }
}
