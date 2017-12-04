package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;
import jade.lang.acl.*;
import java.util.HashMap;
import java.util.Map;

public class ComportamentoCoTransplante extends SimpleBehaviour {

    boolean fim = false;
    boolean enviouMensagen = false;
    int protocolo = 1;
    String s = "";
    Map<Integer, ACLMessage> bancoMenssagens = new HashMap<Integer, ACLMessage>();

    public ComportamentoCoTransplante(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage mensagemRecebida = myAgent.receive();
        if (!enviouMensagen) {
            for (protocolo = 1; protocolo < 4; protocolo++) {
                System.out.println(myAgent.getLocalName() + ": Preparando para enviar una mensagem ao receptor");
                sendMessage("00001;C", Integer.toString(protocolo));
                System.out.println(myAgent.getLocalName() + ": Há disponibilidade para o exame");

            }

            enviouMensagen = !enviouMensagen;
        } else if (mensagemRecebida != null) {
            int idAtual = Integer.parseInt(mensagemRecebida.getConversationId());
            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1];
            int idMensagem = Integer.parseInt(mensagemRecebida.getConversationId());
            //System.out.println(myAgent.getLocalName() + ": :" + bancoMenssagens.get(idMensagem).toString());
            if (codigoDaAcao.equalsIgnoreCase("T")) {
                System.out.println(myAgent.getLocalName() + ": Trasnplante aprovado, solicitando reserva: ");
                //mandar menssagem com a reserva '''R'''
                sendMessage("00001;R", mensagemRecebida.getConversationId());

                bancoMenssagens.remove(idAtual);
            } else if (codigoDaAcao.equalsIgnoreCase("F")) {
                System.out.println(myAgent.getLocalName() + ": Trasnplante não aprovado, somente notificando");
                //nmandar menssagem com a sobre nao aprovacao '''N'''
                sendMessage("00001;N", mensagemRecebida.getConversationId());
                bancoMenssagens.remove(idAtual);

            }

            System.out.println(mensagemRecebida.getContent());
        } else {
            System.out.println(myAgent.getLocalName() + ": Aguardando resposta");
            block();
        }

    }

    private void sendMessage(String message, String idMenssagem) {
        ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
        mensagemParaEnvio.setSender(myAgent.getAID());
        mensagemParaEnvio.addReceiver(new AID("CoHospital", AID.ISLOCALNAME));
        mensagemParaEnvio.addReplyTo(new AID("CoTransplante", AID.ISLOCALNAME));
        mensagemParaEnvio.setContent(message);
        mensagemParaEnvio.setConversationId(idMenssagem);
        myAgent.send(mensagemParaEnvio);

        //Preencher os campos necesários da mensagem
        bancoMenssagens.put(protocolo, mensagemParaEnvio);

    }

    @Override
    public boolean done() {
        return fim;
    }
}
