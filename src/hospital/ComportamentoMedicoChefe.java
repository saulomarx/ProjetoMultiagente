package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoMedicoChefe extends SimpleBehaviour {

    private boolean fim = false;
    private ACLMessage mensagemCoordenadorHospital;
    private int situacaoCoTimeMedico = 0, situacaoCoEnfermaria = 0, situacaoCoAnestesista = 0;

    public ComportamentoMedicoChefe(Agent a) {
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
            if (veioDoAgente.equals("00010")) {
                mensagemCoordenadorHospital = mensagemRecebida;
                if (codigoDaAcao.equalsIgnoreCase("C")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");
                    //Criando e preenchendo menssagem
                    ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("CoAnestesita", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoEnfermagem", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoTimeMedico", AID.ISLOCALNAME));

                    mensagemParaEnvio.addReplyTo(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent("00010;C");

                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                }
            
            } else if (veioDoAgente.equals("01100")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Medico disponivel");
                    situacaoCoTimeMedico = 1;
                } else {
                    situacaoCoTimeMedico = -1;
                }
                verificarDisponibilidade(situacaoCoTimeMedico, situacaoCoEnfermaria, situacaoCoAnestesista);
            
            } else if (veioDoAgente.equals("01010")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time de Enfermagem Disponivel");
                    situacaoCoEnfermaria = 1;
                } else {
                    situacaoCoEnfermaria = -1;
                }
                verificarDisponibilidade(situacaoCoTimeMedico, situacaoCoEnfermaria, situacaoCoAnestesista);
            
            } else if (veioDoAgente.equals("01001")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Anestesista Desponivel");
                    situacaoCoAnestesista = 1;
                } else {
                    situacaoCoAnestesista = -1;
                }
                verificarDisponibilidade(situacaoCoTimeMedico, situacaoCoEnfermaria, situacaoCoAnestesista);
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    private void verificarDisponibilidade(int situacaoCoTimeMedico, int situacaoCoEnfermaria, int situacaoCoAnestesista) {
        if (situacaoCoTimeMedico == 1 && situacaoCoEnfermaria == 1 && situacaoCoAnestesista == 1) {
            sendResponse(mensagemCoordenadorHospital, "T");
        } else if (situacaoCoTimeMedico == -1 || situacaoCoEnfermaria == -1 || situacaoCoAnestesista == -1) {
            sendResponse(mensagemCoordenadorHospital, "F");
        }
    }

    private void sendResponse(ACLMessage mensagemRecebida, String resultado) {
        ACLMessage resposta = mensagemRecebida.createReply();
        resposta.setPerformative(ACLMessage.INFORM);
        resposta.setContent("01000;" + resultado);
        
        myAgent.send(resposta);
    }

    @Override
    public boolean done() {
        return fim;
    }
}
