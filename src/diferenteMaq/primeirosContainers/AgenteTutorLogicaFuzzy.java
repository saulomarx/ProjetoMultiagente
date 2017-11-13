package diferenteMaq.primeirosContainers;

import java.util.Iterator;

public class AgenteTutorLogicaFuzzy extends AgenteTutor
{
    @Override
    protected void setup()
    {
       String servico = "", mensagem="";
       super.setup();

       //monta a mensagem de boas vindas e com informações gerais   
       mensagem = "Olá!!! Eu sou o Agente Tutor " + getAID().getLocalName();
       mensagem = mensagem + "\nMeu nome na plataforma é " + getAID().getName();
       
       mensagem = mensagem + "\nMeus endereços na plataforma são: ";
       Iterator it = getAID().getAllAddresses();
       while(it.hasNext()) 
       {
          mensagem = mensagem + "- " + it.next() + "\n";
       }
       mensagem = mensagem + "Meu estado atual é: " + getAgentState();
       
       //captura o serviço que o agente oferecerá à agência
       Object[] argumento = getArguments();
       if(argumento != null && argumento.length>0)
       {
          servico = (String) argumento[0];
          mensagem = mensagem + "\nPosso realizar o seguinte serviço: " + servico;
        }else 
        {
           //finaliza o agente
           mensagem = mensagem + "\nNão posso realizar nenhum serviço na sociedade";
           doDelete(); //invoca a execucao do metodo takeDown()
        } 
         System.out.println(mensagem);                

    }//Fim do método main()

    @Override
    protected void takeDown() 
    {
        System.out.println("Agente Tutor" + getAID().getName() + 
                   " está finalizado");
    }
}