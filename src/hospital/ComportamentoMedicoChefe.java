package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;
import java.util.HashMap;
import java.util.Map;

public class ComportamentoMedicoChefe extends SimpleBehaviour {

    private boolean fim = false;
    private ACLMessage mensagemCoordenadorHospital;
    //private int situacaoCoTimeMedico = 0, situacaoCoEnfermaria = 0, situacaoCoAnestesista = 0;
    private Map<Integer,ACLMessage> bancoMenssagens = new HashMap<Integer,ACLMessage>();
    private Map<Integer,Integer> situacaoCoTimeMedico1 = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> situacaoCoEnfermaria1 = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> situacaoCoAnestesista1 = new HashMap<Integer,Integer>();


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
            int idAtual = Integer.parseInt(mensagemRecebida.getConversationId());
            if (!bancoMenssagens.containsKey(idAtual)){
                bancoMenssagens.put(idAtual,mensagemRecebida);
                situacaoCoTimeMedico1.put(idAtual,0);
                situacaoCoEnfermaria1.put(idAtual,0);
                situacaoCoAnestesista1.put(idAtual,0);
            }
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
                    mensagemParaEnvio.setConversationId(mensagemRecebida.getConversationId());

                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                } else if (codigoDaAcao.equalsIgnoreCase("N")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");
                    //Criando e preenchendo menssagem
                    ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("CoAnestesita", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoEnfermagem", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoTimeMedico", AID.ISLOCALNAME));

                    mensagemParaEnvio.addReplyTo(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent("00010;N");
                    mensagemParaEnvio.setConversationId(mensagemRecebida.getConversationId());
                    

                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
                }

            } else if (veioDoAgente.equals("01100")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Medico disponivel");
                    situacaoCoTimeMedico1.put(idAtual,1);
                    
                } else {
                    situacaoCoTimeMedico1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual));

            } else if (veioDoAgente.equals("01010")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time de Enfermagem Disponivel");
                    situacaoCoEnfermaria1.put(idAtual,1);
                } else {
                    situacaoCoEnfermaria1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual));

            } else if (veioDoAgente.equals("01001")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Anestesista Desponivel");
                    situacaoCoAnestesista1.put(idAtual,1);
                } else {
                    situacaoCoAnestesista1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual));
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    private void verificarDisponibilidade(int situacaoCoTimeMedico, int situacaoCoEnfermaria, int situacaoCoAnestesista) {
        if (situacaoCoTimeMedico == 1 && situacaoCoEnfermaria == 1 && situacaoCoAnestesista == 1) {
            sendResponse(mensagemCoordenadorHospital, "T");
        } else if (situacaoCoTimeMedico != 0 && situacaoCoEnfermaria != 0 && situacaoCoAnestesista != 0) {
            if (situacaoCoTimeMedico == -1 || situacaoCoEnfermaria == -1 || situacaoCoAnestesista == -1) {
                sendResponse(mensagemCoordenadorHospital, "F");
            }
        }
    }

    private void sendResponse(ACLMessage mensagemRecebida, String resultado) {
        ACLMessage resposta = mensagemRecebida.createReply();
        resposta.setPerformative(ACLMessage.INFORM);
        resposta.setContent("01000;" + resultado);
                    System.out.println(myAgent.getLocalName() + ": =++++++++++="+resposta.toString());

        myAgent.send(resposta);
    }

    @Override
    public boolean done() {
        return fim;
    }
}
