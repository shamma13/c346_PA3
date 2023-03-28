public class Philosopher extends BaseThread implements Runnable {
    private final Object leftFork;
    private final Object rightFork;

    public Philosopher(Object leftFork, Object rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    public void run() {
        try {
            while (true) {
                // thinking
                doAction(": Thinking");
                synchronized (leftFork) {
                    doAction(": Picked up left fork");
                    synchronized (rightFork) {
                        //eating
                        doAction(": Picked up right fork - eating");

                        doAction(": Put down right fork");
                    }

                    doAction(": Put down left fork. Back to thinking");
                }
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}