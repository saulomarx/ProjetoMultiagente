package hospital;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;
import jade.core.AID;

public class ComportamentoCoCentroCirurgico extends SimpleBehaviour {

    private boolean fim = false;
    private boolean disponibilidade = false;
    private int [][] horarios = new int [24][3]; 


    public ComportamentoCoCentroCirurgico(Agent a) {
        super(a);
        for(int i = 0, l = horarios.length;i<l;i++){
            for (int j = 0, m = horarios[i].length;j<m;j++){
                horarios[i][j] = 0;
            }
        }
    }

    @Override
    public void action() {
        System.out.println(myAgent.getLocalName() + ": Preparando para receber mensagens");
        //Obtem a primeira mensagem da fila de mensagens
        ACLMessage mensagemRecebida = myAgent.receive();
        if (mensagemRecebida != null) {
            int hora = 4;
            String aux[] = mensagemRecebida.getContent().split(";");
            String veioDoAgente = aux[0], codigoDaAcao = aux[1];
            if (codigoDaAcao.equalsIgnoreCase("N")) {
                System.out.println(myAgent.getLocalName() + ": Notificado");
            } else if (codigoDaAcao.equalsIgnoreCase("R")) {
                disponibilidade = false;
                System.out.println(myAgent.getLocalName() + ": Reservado");
            } else if (codigoDaAcao.equalsIgnoreCase("C")) {
                String situacao = "F";
                if (getDisponibilidade(hora)) {
                    situacao = "T";
                    reservaHorario(hora);
                }
                ACLMessage resposta = mensagemRecebida.createReply();
                resposta.setPerformative(ACLMessage.INFORM);
                resposta.setContent("00100;" + situacao);
                resposta.setConversationId(mensagemRecebida.getConversationId());
                myAgent.send(resposta);
            }
            imprimirHorarios();
        } else {
            System.out.println(myAgent.getLocalName() + ": Bloqueado para esperar receber mensagem.....");
            block();
        }
    } // Fim do método action()

        private boolean getDisponibilidade(int hora){
        for(int i = 0, l = horarios[hora].length;i<l;i++){
            if(horarios[hora][i] == 0) return true;
        }
        return false;
    }
    
    private void reservaHorario(int hora){
        for(int i = 0, l = horarios[hora].length;i<l;i++){
            if(horarios[hora][i] == 0){
                horarios[hora][i] = -1;
                return;
            }
        }
        
    }
    
    private void cancelaHorario(int hora){
        for(int i = 0, l = horarios[hora].length;i<l;i++){
            if(horarios[hora][i] == -1){
                horarios[hora][i] = 0;
                return;
            }
        }
        
    }
    
    private void confirmaHorario(int hora){
        for(int i = 0, l = horarios[hora].length;i<l;i++){
            if(horarios[hora][i] == -1){
                horarios[hora][i] = 1;
                return;
            }
        }
        
    }
    
    private void imprimirHorarios(){
        String saida = "\n\t\t"+myAgent.getLocalName()+"\t\t\n";
        for(int i = 0, l = horarios.length;i<l;i++){
            saida+="Horario"+Integer.toString(i)+":\t";
            for (int j = 0, m = horarios[i].length;j<m;j++){
                saida+=Integer.toString(horarios[i][j])+"\t";
            }
            saida+="\n";
        }
        System.out.println(saida);
        
    }
    
    @Override
    public boolean done() {
        return fim;
    }
}
