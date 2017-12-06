package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;
import java.util.HashMap;
import java.util.Map;

public class ComportamentoCoHospital extends SimpleBehaviour {

    private boolean fim = false;
    private Map<Integer, ACLMessage> bancoMenssagens = new HashMap<Integer, ACLMessage>();
    private Map<Integer, Integer> situacaoCoCentroCirurgico1 = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> situacaoMedicoChefe1 = new HashMap<Integer, Integer>();

    public ComportamentoCoHospital(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();

        if (mensagemRecebida != null) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Erro: " + e);
            }
            int idAtual = Integer.parseInt(mensagemRecebida.getConversationId());
            if (!bancoMenssagens.containsKey(idAtual)) {
                bancoMenssagens.put(idAtual, mensagemRecebida);
                situacaoCoCentroCirurgico1.put(idAtual, 0);
                situacaoMedicoChefe1.put(idAtual, 0);
            }

            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1], horario = aux[2];

            if (veioDoAgente.equals("00001")) {
                if (codigoDaAcao.equalsIgnoreCase("C")) {
                    System.out.println(myAgent.getLocalName() + ": Requisitado Disponibilidade");
                    System.out.println(myAgent.getLocalName() + ": Perguntando Sobre Disponibilidade ao Medico Chefe e ao Coordenador do centro cirurgico");

                    //Envia a mensagem aos destinatarios
                    sendMessage("00010;C;" + horario, mensagemRecebida.getConversationId());

                } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                    System.out.println(myAgent.getLocalName() + ": Transplante aprovado, reservar na agenda");
                    //Criando e preenchendo menssagem
                    sendMessage("00010;R;" + horario, mensagemRecebida.getConversationId());
                    bancoMenssagens.remove(idAtual);

                    //Envia a mensagem aos destinatarios
                } else if (codigoDaAcao.equalsIgnoreCase("N")) {
                    System.out.println(myAgent.getLocalName() + ": Transplante nao aprovado, Somente notificando");
                    //Criando e preenchendo menssagem
                    sendMessage("00010;N;" + horario, mensagemRecebida.getConversationId());

                    bancoMenssagens.remove(idAtual);
                    //Envia a mensagem aos destinatarios
                }

            } else if (veioDoAgente.equals("00100")) {

                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Centro Cirurgico disponivel");
                    situacaoCoCentroCirurgico1.put(idAtual, 1);
                } else {
                    situacaoCoCentroCirurgico1.put(idAtual, -1);
                }
                verificarDisponibilidade(situacaoMedicoChefe1.get(idAtual), situacaoCoCentroCirurgico1.get(idAtual), idAtual, horario);

            } else if (veioDoAgente.equals("01000")) {

                if (codigoDaAcao.equalsIgnoreCase("T")) {
                    System.out.println(myAgent.getLocalName() + ": Madico chefe aprovou o procedimento");
                    situacaoMedicoChefe1.put(idAtual, 1);
                } else {
                    situacaoMedicoChefe1.put(idAtual, -1);
                }
                verificarDisponibilidade(situacaoMedicoChefe1.get(idAtual), situacaoCoCentroCirurgico1.get(idAtual), idAtual, horario);
            }
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

    private void sendMessage(String message, String idMenssagem) {
        ACLMessage mensagemParaEnvio = new ACLMessage(ACLMessage.INFORM);
        mensagemParaEnvio.setSender(myAgent.getAID());
        mensagemParaEnvio.addReceiver(new AID("MedicoChefe", AID.ISLOCALNAME));
        mensagemParaEnvio.addReceiver(new AID("CoCentroCirurgico", AID.ISLOCALNAME));
        mensagemParaEnvio.addReplyTo(new AID("CoHospital", AID.ISLOCALNAME));
        mensagemParaEnvio.setContent(message);
        mensagemParaEnvio.setConversationId(idMenssagem);
        myAgent.send(mensagemParaEnvio);
    }

    private void verificarDisponibilidade(int situacaoCoCentroCirurgico, int situacaoMedicoChefe, int idAtual, String horario) {
        if (situacaoCoCentroCirurgico == 1 && situacaoMedicoChefe == 1) {
            sendResponse(bancoMenssagens.get(idAtual), "T", idAtual, horario);
        } else if (situacaoCoCentroCirurgico != 0 && situacaoMedicoChefe != 0) {
            if (situacaoCoCentroCirurgico == -1 || situacaoMedicoChefe == -1) {
                sendResponse(bancoMenssagens.get(idAtual), "F", idAtual, horario);
            }
        }
    }

    private void sendResponse(ACLMessage mensagemRecebida, String resultado, int idAtual, String horario) {
        ACLMessage resposta = mensagemRecebida.createReply();
        resposta.setPerformative(ACLMessage.INFORM);
        resposta.setContent("00010;" + resultado + ";" + horario);
        myAgent.send(resposta);
        System.out.println(myAgent.getLocalName() + ":::" + bancoMenssagens);
    }

    @Override
    public boolean done() {
        return fim;
    }
}
