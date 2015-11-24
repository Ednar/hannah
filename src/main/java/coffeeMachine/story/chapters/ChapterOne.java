package coffeeMachine.story.chapters;

import coffeeMachine.story.StoryBoard;
import coffeeMachine.story.StoryTellingUtils;
import jade.lang.acl.ACLMessage;

public class ChapterOne extends Chapter {

    @Override
    public void play() {
        StoryTellingUtils.fastWrite("\n" +
                "\n" +
                " _____        __  __            _____                 _   \n" +
                "/  __ \\      / _|/ _|          |  _  |               | |  \n" +
                "| /  \\/ ___ | |_| |_ ___  ___  | | | |_   _  ___  ___| |_ \n" +
                "| |    / _ \\|  _|  _/ _ \\/ _ \\ | | | | | | |/ _ \\/ __| __|\n" +
                "| \\__/\\ (_) | | | ||  __/  __/ \\ \\/' / |_| |  __/\\__ \\ |_ \n" +
                " \\____/\\___/|_| |_| \\___|\\___|  \\_/\\_\\\\__,_|\\___||___/\\__|\n" +
                "                                                          \n" +
                "*********************************************************\n" +
                "\n");
        System.out.println("It's time.");
        StoryTellingUtils.dramaticPause();
        System.out.println("In the distance you can see it...");
        StoryTellingUtils.dramaticPause();
        StoryTellingUtils.slowWrite("The coffee machine whispers to you, calling you.");
        System.out.print("What do you do? ");
        String choice = scanner.nextLine();

        if (choice.equals("go north")) {
            System.out.println("The quest begins");
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(machine);
            request.setSender(StoryBoard.buyer.getAID());
            StoryBoard.step = StoryBoard.Step.WAIT_FOR_TURN_ON_ACCEPT;
        } else {
            System.out.println("You are forever lost, wandering the catacombs of the office space.");
            System.exit(0);
        }
    }
}
