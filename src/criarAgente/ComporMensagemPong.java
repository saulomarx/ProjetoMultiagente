package criarAgente;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class ComporMensagemPong extends CyclicBehaviour
{
    public ComporMensagemPong (Agent a)
    {
       super(a);
    }      
        
    @Override
    public void action() 
    {
        ACLMessage msg = myAgent.receive();
        if (msg!=null) {
            System.out.println( " - " +
            myAgent.getLocalName() + " recebeu: " +
            msg.getContent() );

            ACLMessage reply = msg.createReply();
            reply.setPerformative( ACLMessage.INFORM );
            reply.setContent("Pong" );
            myAgent.send(reply);
        }
        else{            
           block();
        }
    }
}