package diferenteMaq.containerComunicacao;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CompFiltroINFORMa1 extends CyclicBehaviour 
{   
    public CompFiltroINFORMa1(Agent a) 
    {
       super(a);
    }

    @Override
    public void action() 
    {
        MessageTemplate mt1 = 
            MessageTemplate.and(  
            MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
            MessageTemplate.MatchSender( new AID( "a1", AID.ISLOCALNAME))) ;
        
        System.out.print("\nComportamento que filtra mensagens INFORM do agente a1: \n");
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