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
            String veioDoAgente = aux[0], codigoDaAcao = aux[1], horario = aux[2];
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
                    sendMessage("00010;C;"+horario, mensagemRecebida.getConversationId());
                    
                } else if (codigoDaAcao.equalsIgnoreCase("N")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");
                    sendMessage("00010;N;"+horario, mensagemRecebida.getConversationId());
                    
                } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");
                    sendMessage("00010;R;"+horario, mensagemRecebida.getConversationId());
                }

            } else if (veioDoAgente.equals("01100")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Medico disponivel");
                    situacaoCoTimeMedico1.put(idAtual,1);
                    
                } else {
                    situacaoCoTimeMedico1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual), horario);

            } else if (veioDoAgente.equals("01010")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time de Enfermagem Disponivel");
                    situacaoCoEnfermaria1.put(idAtual,1);
                } else {
                    situacaoCoEnfermaria1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual), horario);

            } else if (veioDoAgente.equals("01001")) {
                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Time Anestesista Desponivel");
                    situacaoCoAnestesista1.put(idAtual,1);
                } else {
                    situacaoCoAnestesista1.put(idAtual,-1);
                }
                verificarDisponibilidade(situacaoCoTimeMedico1.get(idAtual), situacaoCoEnfermaria1.get(idAtual), situacaoCoAnestesista1.get(idAtual), horario);
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()
    
    private void sendMessage(String message, String idConversa){
        ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
                    mensagemParaEnvio.setSender(myAgent.getAID());
                    mensagemParaEnvio.addReceiver(new AID("CoAnestesita", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoEnfermagem", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReceiver(new AID("CoTimeMedico", AID.ISLOCALNAME));
                    mensagemParaEnvio.addReplyTo(new AID("MedicoChefe", AID.ISLOCALNAME));
                    mensagemParaEnvio.setContent(message);
                    mensagemParaEnvio.setConversationId(idConversa);

                    //Envia a mensagem aos destinatarios
                    myAgent.send(mensagemParaEnvio);
    }

    private void verificarDisponibilidade(int situacaoCoTimeMedico, int situacaoCoEnfermaria, int situacaoCoAnestesista, String horario) {
        if (situacaoCoTimeMedico == 1 && situacaoCoEnfermaria == 1 && situacaoCoAnestesista == 1) {
            sendResponse(mensagemCoordenadorHospital, "T", horario);
        } else if (situacaoCoTimeMedico != 0 && situacaoCoEnfermaria != 0 && situacaoCoAnestesista != 0) {
            if (situacaoCoTimeMedico == -1 || situacaoCoEnfermaria == -1 || situacaoCoAnestesista == -1) {
                sendResponse(mensagemCoordenadorHospital, "F", horario);
            }
        }
    }

    private void sendResponse(ACLMessage mensagemRecebida, String resultado, String horario) {
        ACLMessage resposta = mensagemRecebida.createReply();
        resposta.setPerformative(ACLMessage.INFORM);
        resposta.setContent("01000;" + resultado + ";" + horario);
                    System.out.println(myAgent.getLocalName() + ": =++++++++++="+resposta.toString());

        myAgent.send(resposta);
    }

    @Override
    public boolean done() {
        return fim;
    }
}
