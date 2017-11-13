package diferenteMaq.containerComunicacao;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.*;

public class AgenteDoisComportamentos extends Agent 
{
    @Override
    protected void setup() 
    {
        try
        {
           Thread.sleep(12000);
        }
        catch(Exception e)
        {
           System.out.println("Erro: " + e);
        }
        
        // Envio das mensagens para os agentes "a1" e "a2"    
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent( "Ping" );
        for (int i = 1; i<=2; i++)
            msg.addReceiver( new AID( "a" + i, AID.ISLOCALNAME) );

        send(msg);
        
        addBehaviour(new CompFiltroINFORMa1(this));
        addBehaviour(new CompFiltroPROPOSEa2(this));
    }
}