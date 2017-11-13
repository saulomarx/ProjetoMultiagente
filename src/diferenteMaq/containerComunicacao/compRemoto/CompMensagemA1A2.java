package diferenteMaq.containerComunicacao.compRemoto;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CompMensagemA1A2 extends CyclicBehaviour 
{   
    public CompMensagemA1A2(Agent a) 
    {
       super(a);
    }

    @Override
    public void action() 
    {
        ACLMessage msg = myAgent.receive();
        if (msg!=null) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative( ACLMessage.INFORM );
            reply.setContent("Vamos conversar sobre INFORM" );
            myAgent.send(reply);

            reply.setPerformative( ACLMessage.PROPOSE );
            reply.setContent("Vamos conversar sobre PROPOSE" );
            myAgent.send(reply);
        }
        else 
            block();
    }   
}