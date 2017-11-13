package diferenteMaq.containerComunicacao;

import java.net.InetAddress;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import test.common.remote.RemoteManager;
import test.common.JadeController;
import test.common.TestUtility;
import test.common.remote.TSDaemon;

public class CriarAgentes
{
   static ContainerController containerController;
   static AgentController agentController;
   static AgentContainer container[] = new AgentContainer [2];
   
   static jade.core.Runtime runtime;

   public static void main(String[] args) throws InterruptedException 
   {
      JadeController jc=null; 
      String ipMainContainer = "", jadeArgs="";
      int num=0;
       
      try {
         ipMainContainer = InetAddress.getLocalHost().getHostAddress();
         System.out.println("IP: " + ipMainContainer);
      } catch (Exception e) { } 
       
      //iniciando main container
      //startMainContainer(Profile.LOCAL_HOST, Profile.LOCAL_PORT, "UFABC");
      startMainContainer(ipMainContainer, "1099", "UFABC_EAD");
      //startMainContainer("127.0.0.1", "1099", "UFABC_EAD");
    
      container[0] = null;
      container[0]=startContainer(ipMainContainer, Profile.LOCAL_PORT, "CEnvioMensagemSA", runtime, container[0] );
      container[1]=startContainer(ipMainContainer, Profile.LOCAL_PORT, "CEnvioMensagemSBC", runtime, container[0] );
      
      //adicionando agentes
      addAgent(container[0], "AgEnvioSA", AgenteDoisComportamentos.class.getName(), null);
      addAgent(container[1], "AgEnvioSBC", AgenteDoisComportamentos.class.getName(), null);

      //Criando containers remotos
      try {
      String remoteHost = "192.168.15.15"; // O host/IP do container remoto
      RemoteManager rm = TestUtility.createRemoteManager(remoteHost, TSDaemon.DEFAULT_PORT, 
              TSDaemon.DEFAULT_NAME);
      num++;
      jadeArgs = "-container -container-name CRecebeMensagemSA -host " + Profile.getDefaultNetworkName() + 
                      " -port 1099 -name UFABC_EAD -agents a1:diferenteMaq.containerComunicacao.compRemoto.Agentea1a2";
      jc = TestUtility.launchJadeInstance(rm, "Instancia"+num+"JADE", 
              null, jadeArgs, null);

      num++;
      jadeArgs = "-container -container-name CRecebeMensagemSBC -host " + Profile.getDefaultNetworkName() + 
                      " -port 1099 -name UFABC_EAD -agents a2:diferenteMaq.containerComunicacao.compRemoto.Agentea1a2";
      jc = TestUtility.launchJadeInstance(rm, "Instancia"+num+"JADE", 
              null, jadeArgs, null);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      //adicionando agente RMA
      //addAgent(containerController, "rma", "jade.tools.rma.rma", null);
      addAgent(containerController, "rma", jade.tools.rma.rma.class.getName(), null);
      
      addAgent(containerController, "Sniffer", "jade.tools.sniffer.Sniffer", 
                                     new Object[]{"AgEnvioSA", ";", "AgEnvioSBC", ";", "a1", ";", "a2"}); 
   }
   
   public static void startMainContainer(String host, String port, String name) {
       //Inicia um nova instância de runtime da plataforma JADE 
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