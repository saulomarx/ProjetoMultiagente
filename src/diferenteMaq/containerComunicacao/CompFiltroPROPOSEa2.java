package diferenteMaq.containerComunicacao;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CompFiltroPROPOSEa2 extends CyclicBehaviour 
{   
    public CompFiltroPROPOSEa2(Agent a) 
    {
       super(a);
    }

    @Override
    public void action() 
    {
        MessageTemplate mt1 = 
            MessageTemplate.and(  
            MessageTemplate.MatchPerformative( ACLMessage.PROPOSE ),
            MessageTemplate.MatchSender( new AID( "a2", AID.ISLOCALNAME))) ;
        
        System.out.print("\nComportamento que filtra mensagens PROPOSE do agente a2: \n");
        ACLMessage msg= myAgent.receive( mt1 );
        if (msg!=null)
            System.out.println( "Recebi mensagem do agente "
                +  msg.getSender().getLocalName() + " = "
                +  msg.getContent() );
        else
            System.out.println( "Mensagem NULL" );
        block();
    }   
}