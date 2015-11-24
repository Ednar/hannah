package coffeeMachine.story.chapters;

import jade.core.AID;

import java.util.Scanner;

public abstract class Chapter {

    protected Scanner scanner = new Scanner(System.in);
    protected AID machine = new AID("Dave", AID.ISLOCALNAME);

    public abstract void play();
}
