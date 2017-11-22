package emissorReceptorBlock;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;
import jade.lang.acl.*;

public class ComportamentoEmissor extends SimpleBehaviour
{
   boolean fim = false;
   public ComportamentoEmissor (Agent a)
   {
      super(a);
   }   
   
   @Override
   public void action()
   {
      System.out.println(myAgent.getLocalName() +": Preparando para enviar uma mensagem ao Receptor");
      // Criação do objeto ACLMessage
      ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
      //Preencher os campos necesários da mensagem
      mensagem.setSender(myAgent.getAID());
      mensagem.addReceiver(new AID("Receptor",AID.ISLOCALNAME));
      mensagem.setLanguage("Portugues");
      mensagem.setContent("Olá, como você vai Receptor?");
      //Envia a mensagem aos destinatarios
      System.out.println(myAgent.getLocalName() +": Enviando olá ao Receptor");
      System.out.println(myAgent.getLocalName() + "\n" + mensagem.toString());
      myAgent.send(mensagem);

      fim = true;
   }
   
   @Override
   public boolean done()
   {
      return fim;
   }
}