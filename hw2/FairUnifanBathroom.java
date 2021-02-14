// wjw692
// zbt86


public class FairUnifanBathroom {
	private int utCnt;
	private int ouCnt;
	int ticketsGiven;
	int nextTicket;
	
	public FairUnifanBathroom() {
		utCnt = 0;
		ouCnt = 0;
		ticketsGiven = 0;
		nextTicket = 0;
	}
	
	public synchronized void enterBathroomUT() {
	// Called when a UT fan wants to enter bathroom
		//System.out.println("UT Fan " + Thread.currentThread().getId() + " joins line");
		int myTicket = ticketsGiven++;

		while(ouCnt>0 || utCnt>=4 || nextTicket != myTicket) {
			// Then at least one ou fan is in bathroom -> need to wait
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		utCnt++;
		nextTicket++;
		//System.out.println("UT Fan " + Thread.currentThread().getId() + " entered bathroom");
		notifyAll();
	}
	
	public synchronized void enterBathroomOU() {
    // Called when a OU fan wants to enter bathroom
		//System.out.println("OU Fan " + Thread.currentThread().getId() + " joins line");

		int myTicket = ticketsGiven++;

		while(utCnt>0 || ouCnt>=4 || nextTicket != myTicket) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ouCnt++;
		nextTicket++;
		//System.out.println("OU Fan " + Thread.currentThread().getId() + " entered bathroom");
		notifyAll();
	}
	
	public synchronized void leaveBathroomUT() {
    // Called when a UT fan wants to leave bathroom
		utCnt--;
		//System.out.println("UT Fan " + Thread.currentThread().getId() + " left bathroom");
		notifyAll();

	}

	public synchronized void leaveBathroomOU() {
    // Called when a OU fan wants to leave bathroom
		ouCnt--;
		//System.out.println("OU Fan " + Thread.currentThread().getId() + " left bathroom");
		notifyAll();
	}

	public void printCounts(){
		System.out.println("UT cnt " + utCnt + ", OU cnt " + ouCnt);
	}
}
	
