package criarAgente;

import jade.core.Agent;
import jade.core.AID;
import jade.wrapper.ContainerController;
import jade.wrapper.AgentController;

public class CriarAgenteNoCodigo extends Agent 
{
    @Override
    protected void setup() 
    {
        String nomeAgente = "Joao";         
    	ContainerController c = getContainerController();        
    	try {
    	    AgentController a = c.createNewAgent( nomeAgente, Pong.class.getName(), null );
    	    a.start();                
            AID enderecos = new AID(nomeAgente, AID.ISLOCALNAME);                
    	    System.out.println("+++ Agente criado: " + enderecos);
            
    	    AgentController sniffer = c.createNewAgent( "Sniffer", jade.tools.sniffer.Sniffer.class.getName(), new Object[]{"CriandoAgente", ";", nomeAgente} );
    	    sniffer.start();            

            try
            {
              Thread.sleep(5000);
            }
            catch(Exception e)
            {
               System.out.println("Erro: " + e);
            }            
    	}
    	catch (Exception e){}        
        addBehaviour(new ComporEnviarMensagens(this, nomeAgente));
    }
}