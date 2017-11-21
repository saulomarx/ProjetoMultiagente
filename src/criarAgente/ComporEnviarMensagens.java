package criarAgente;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ComporEnviarMensagens extends SimpleBehaviour
{
    int n = 1;
    String agenteReceptor = "";
   
    public ComporEnviarMensagens (Agent a, String agenteReceptor)
    {
       super(a);
       this.agenteReceptor = agenteReceptor;
    }      
   			 
    @Override
    public void action() 
    {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Mensagem #" + n );
        msg.addReceiver(new AID(agenteReceptor, AID.ISLOCALNAME));
        System.out.println("+++ Enviando: " + n);
        myAgent.send(msg);
        n++;
    }

    @Override
    public  boolean done() 
    {  
       return n > 3;  
    }
}