package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoCoHospital extends SimpleBehaviour {

    private boolean fim = false;
    private ACLMessage mensagemCoordenadorTransplante, mensagemMedicoChefe, mensagemCoordenadorCentroCirurgico;
    private int situacaoCoCentroCirurgico = 0 , situacaoMedicoChefe = 0;

    public ComportamentoCoHospital(Agent a) {
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
            if (veioDoAgente.equals("00001")) {
                mensagemCoordenadorTransplante = mensagemRecebida;
                if (codigoDaAcao.equalsIgnoreCase("C")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");
                    //Criando e preenchendo menssagem
                    ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoCentroCirurgico", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReplyTo(new AID("CoHospital", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent("00010;C");
                    
                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                    System.out.println(myAgent.getLocalName() + ": Transplante aprovado, reservar na agenda");
                    //Criando e preenchendo menssagem
                    ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoCentroCirurgico", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReplyTo(new AID("CoHospital", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent("00010;R");
                    
                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                } else if (codigoDaAcao.equalsIgnoreCase("N")) {
                    System.out.println(myAgent.getLocalName() + ": Transplante nao aprovado, Somente notificando");
                    //Criando e preenchendo menssagem
                    ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoCentroCirurgico", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReplyTo(new AID("CoHospital", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent("00010;N");
                    
                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                }
                
            } else if (veioDoAgente.equals("00100")) {
                mensagemCoordenadorCentroCirurgico = mensagemRecebida;
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Centro Cirurgico disponivel");
                    situacaoCoCentroCirurgico = 1;
                } else situacaoCoCentroCirurgico = -1;
                verificarDisponibilidade(situacaoMedicoChefe, situacaoCoCentroCirurgico);
            } else if (veioDoAgente.equals("01000")) {
                mensagemMedicoChefe = mensagemRecebida;
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Madico chefe aprovou o procedimento");
                    situacaoMedicoChefe = 1;
                } else situacaoMedicoChefe = -1;
                verificarDisponibilidade(situacaoMedicoChefe, situacaoCoCentroCirurgico);
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()
    
    private void verificarDisponibilidade(int situacaoCoCentroCirurgico, int situacaoMedicoChefe){
        if (situacaoCoCentroCirurgico == 1 && situacaoMedicoChefe == 1) sendResponse(mensagemCoordenadorTransplante, "T");
        else if(situacaoCoCentroCirurgico != 0 && situacaoMedicoChefe != 0) 
            if (situacaoCoCentroCirurgico == -1 || situacaoMedicoChefe == -1) sendResponse(mensagemCoordenadorTransplante, "F");
    }
    private void sendResponse(ACLMessage mensagemRecebida, String resultado){
            ACLMessage resposta =  mensagemRecebida.createReply();
            resposta.setPerformative(ACLMessage.INFORM);
            resposta.setContent("00010;"+resultado);
            myAgent.send(resposta);
    }
    @Override
    public boolean done() {
        return fim;
    }
}
