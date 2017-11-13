package diferenteMaq.primeirosContainers;

import jade.core.Agent;
import java.util.Iterator;

public class AgenteAluno extends Agent
{    
   @Override
   protected void setup()
   {
       String mensagem="";
       super.setup();
       
       //monta a mensagem de boas vindas e com informações gerais   
       mensagem = "Olá!!! Eu sou o Agente Aluno " + getAID().getLocalName();
       mensagem = mensagem + "\nMeu nome na plataforma é " + getAID().getName();
       
       mensagem = mensagem + "\nMeus endereços na plataforma são: ";
       Iterator it = getAID().getAllAddresses();
       while(it.hasNext()) 
       {
          mensagem = mensagem + "- " + it.next() + "\n";
       }
       mensagem = mensagem + "Meu estado atual é: " + getAgentState();
       
       System.out.println(mensagem);                

    }//Fim do método main()

   @Override
    protected void takeDown() 
    {
        System.out.println("Agente Aluno" + getAID().getName() + 
                   " está finalizado");
    }
}