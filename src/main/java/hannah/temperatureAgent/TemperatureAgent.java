package hannah.temperatureAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TemperatureAgent extends Agent {

    String w1DirPath = "/sys/bus/w1/devices/28-021562c60fff/w1_slave";
    double lastMeasuredTemperature;

    @Override
    protected void setup() {
        System.out.println("Temperaturagent är igång");
        String filePath = w1DirPath;
        File file = new File(filePath);

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                System.out.println("Läser temperatur");
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String output;
                    br.readLine();
                    if ((output = br.readLine()) != null) {
                        int idx = output.indexOf("t=");
                        if (idx > -1) {
                            // Temp data (multiplied by 1000) in 5 chars after t=
                            double tempC = Double.parseDouble(
                                    output.substring(output.indexOf("t=") + 2));
                            // Divide by 1000 to get degrees Celsius
                            tempC /= 1000;
                            lastMeasuredTemperature = tempC;


                        }

                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println("Kontrollerar temperaturerequest");
                ACLMessage receive = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
                if (receive != null) {
                    ACLMessage reply = receive.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(String.valueOf(lastMeasuredTemperature));
                    send(reply);
                }

            }
        });
    }
}
