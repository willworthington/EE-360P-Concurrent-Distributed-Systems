// wjw692
// zbt86

public class FairUnifanBathroom {   
	private int utCnt;
	private int ouCnt;
	//Queue<>
	
	public FairUnifanBathroom() {
		utCnt = 0;
		ouCnt = 0;
	}
	
	public synchronized void enterBathroomUT() {
    // Called when a UT fan wants to enter bathroom	
		while(ouCnt>0 || utCnt>=4) {
			// Then at least one ou fan is in bathroom -> need to wait
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		utCnt++;
	}
	
	public synchronized void enterBathroomOU() {
    // Called when a OU fan wants to enter bathroom
		while(utCnt>0 || ouCnt>=4) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ouCnt++;
	}
	
	public synchronized void leaveBathroomUT() {
    // Called when a UT fan wants to leave bathroom
		utCnt--;
		notifyAll();
	}

	public synchronized void leaveBathroomOU() {
    // Called when a OU fan wants to leave bathroom
		ouCnt--;
		notifyAll();
	}
}
	
