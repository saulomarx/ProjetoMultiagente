package hospital;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class IniciarAgentes {

    static ContainerController containerController;
    static AgentController agentController;

    public static void main(String[] args) throws InterruptedException {
        //iniciando main container
        //startMainContainer(Profile.LOCAL_HOST, Profile.LOCAL_PORT, "UFABC");
        startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "UFABC");
        //adicionando agente
        //SINTAXE: addAgent(container, nome_do_agente, classe, parametros de inicializacao)
        addAgent(containerController, "CoTransplante", AgenteCoTransplante.class.getName(), null);
        addAgent(containerController, "CoHospital", AgenteCoHospital.class.getName(), null);
        addAgent(containerController, "CoCentroCirurgico", AgenteCoCentroCirurgico.class.getName(), null);

        addAgent(containerController, "MedicoChefe", AgenteMedicoChefe.class.getName(), null);

        addAgent(containerController, "CoAnestesita", AgenteCoAnestesita.class.getName(), null);
        addAgent(containerController, "CoEnfermagem", AgenteCoEnfermagem.class.getName(), null);
        addAgent(containerController, "CoTimeMedico", AgenteCoTimeMedico.class.getName(), null);
        // Thread.sleep(3000);        
        //adicionando agente RMA
        //addAgent(containerController, "rma", "jade.tools.rma.rma", null);
        //addAgent(containerController, "rma", jade.tools.rma.rma.class.getName(), null);        
        //Criando o agente Sniffer e definindo quais agentes ele irá controlar
        addAgent(containerController, "Sniffer", "jade.tools.sniffer.Sniffer",
                new Object[]{"CoTransplante", ";"
                        , "CoHospital", ";", 
                        "CoCentroCirurgico", ";", "MedicoChefe",";"
                        ,"CoAnestesita",";","CoEnfermagem",";","CoTimeMedico"
});
    }

    public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);

        containerController = runtime.createMainContainer(profile);
    }

    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) {
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
    }
}
