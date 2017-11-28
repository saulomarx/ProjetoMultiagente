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
            System.out.println(myAgent.getLocalName() + ": Há disponibilidade para o exame");

            enviouMensagen = !enviouMensagen;
        } else if (mensagemRecebida != null) {
            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1];
            if (codigoDaAcao.equalsIgnoreCase("T")) {
                System.out.println(myAgent.getLocalName() + ": Trasnplante aprovado, solicitando reserva: ");
                //mandar menssagem com a reserva '''R'''
                fim = true;
            } else if (codigoDaAcao.equalsIgnoreCase("F")) {
                System.out.println(myAgent.getLocalName() + ": Trasnplante não aprovado, somente notificando");
                //nmandar menssagem com a sobre nao aprovacao '''N'''
                ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                //Preencher os campos necesários da mensagem
                mensagemParaEnvio.setSender(myAgent.getAID());
                mensagemParaEnvio.addReceiver(new AID("CoHospital", AID.ISLOCALNAME));
                mensagemParaEnvio.setContent("00001;N");
                mensagemParaEnvio.addReplyTo(new AID("CoTransplante", AID.ISLOCALNAME));
                //Envia a mensagem aos destinatarios
                myAgent.send(mensagemParaEnvio);
                fim = true;
            }

            System.out.println(mensagemRecebida.getContent());
        } else {
            System.out.println(myAgent.getLocalName() + ": Aguardando resposta");
            block();
        }

    }

    @Override
    public boolean done() {
        return fim;
    }
}
