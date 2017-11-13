package mesmaMaq.containerComunicacao;

import java.net.InetAddress;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CriarAgente 
{
   static ContainerController containerController;
   static AgentController agentController;
   static AgentContainer container[] = new AgentContainer [2];
   
   static jade.core.Runtime runtime;

   public static void main(String[] args) throws InterruptedException 
   {
      String ipMainContainer = "";
       
      try {
         ipMainContainer = InetAddress.getLocalHost().getHostAddress();
         System.out.println("IP: " + ipMainContainer);
      } catch (Exception e) { } 
              
      //iniciando main container
      //startMainContainer(Profile.LOCAL_HOST, Profile.LOCAL_PORT, "UFABC");
      startMainContainer(ipMainContainer, Profile.LOCAL_PORT, "UFABC_Containers");
      
      container[0] = null;
      container[1] = null;
      container[0]=startContainer(ipMainContainer, Profile.LOCAL_PORT, "ContainerEnvioMensagens", runtime, container[0] );
      container[1]=startContainer(ipMainContainer, Profile.LOCAL_PORT, "Container-a1a2", runtime, container[1] );
        
      //adicionando agente
      addAgent(container[0], "Emissor", AgenteDoisComportamentos.class.getName(), null);
      addAgent(container[1], "a1", Agentea1a2.class.getName(), null);
      addAgent(container[1], "a2", Agentea1a2.class.getName(), null);

      //adicionando agente RMA
      //addAgent(containerController, "rma", "jade.tools.rma.rma", null);
      addAgent(containerController, "rma", jade.tools.rma.rma.class.getName(), null);
      
      addAgent(containerController, "Sniffer", "jade.tools.sniffer.Sniffer", 
                                     new Object[]{"Emissor", ";", "a1", ";", "a2"}); 
   }
   
   public static void startMainContainer(String host, String port, String name) {
        runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);
        containerController = runtime.createMainContainer(profile);
    }

   public static AgentContainer startContainer(String host, String port, String nomeContainer, 
           jade.core.Runtime runtime, AgentContainer container) {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.CONTAINER_NAME, nomeContainer);
        container = runtime.createAgentContainer(profile);
        try
        {
           container.start();
        }
        catch(Exception e)
        {
            System.out.println("Erro: " + e);
        }
        return container;
    }
   
    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) { 
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
        catch(Exception e)
        {
            System.out.println("Erro: " + e);
        }        
    }
}