/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
import java.util.concurrent.locks.Condition;

public class Monitor
{
    /*
     * ------------
     * Data members
     * ------------
     */
    private enum status{thinking,hungry,eating};
    private int numofchop;
    private boolean talking;
    private status state[];

    private Condition self[]; //synchronization primitive that provides a queue for threads waiting for resource

    /**
     * Constructor
     */
    public Monitor(int piNumberOfPhilosophers)
    {
        // TODO: set appropriate number of chopsticks based on the # of philosophers
        this.numofchop = piNumberOfPhilosophers;
        state = new status[piNumberOfPhilosophers];
        Condition self[] = new Condition[piNumberOfPhilosophers];

        //initializing chopstick as condition
//        for (int j = 0; j < piNumberOfPhilosophers; i++)
//            self [i] = lock.newCondition(); //lock object will coordinate access to shared state and ensure that condition is checked atomically

        for(int i = 0; i < piNumberOfPhilosophers; i++)
            state[i] = status.thinking;

        talking = false;
    }


    /*
     * -------------------------------
     * User-defined monitor procedures
     * -------------------------------
     */

    /**
     * Grants request (returns) to eat when both chopsticks/forks are available.
     * Else forces the philosopher to wait()
     */
    public synchronized void pickUp(final int piTID)
    {
        try {
            state[piTID] = status.hungry;
            test(piTID);
            if (state[piTID] != status.eating)
                self[piTID].wait(); //wait for signal;
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to pick up chopstick for philosopher " + piTID);
        }
    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int piTID)
    {
        state[piTID] = status.thinking;
        test((piTID + numofchop-1) % numofchop);
        test((piTID + 1) % numofchop);
    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk()
    {
        while (talking) { //many philosophers can want to talk but only one should be allowed to speak
            try {
                wait();
//                requestTalk();
            }
            catch(InterruptedException e) {
                System.out.println("Philosopher can't talk someone else is speaking");
            }
            talking = true;
        }

    }

    /**
     * When one philosopher is done talking stuff, others
     * can feel free to start talking.
     */
    public synchronized void endTalk()
    {
        talking = false;
        notifyAll();
    }

    public synchronized void test(final int piTID)
    {
        if (state[(piTID + numofchop-1) % numofchop] != status.eating && state[piTID] == status.hungry && state[(piTID + 1) % numofchop] != status.eating) {
            state[piTID] = status.eating;
            self[piTID].notifyAll();
        }
    }
}

// EOF
