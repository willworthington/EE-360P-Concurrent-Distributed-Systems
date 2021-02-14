public class testFairUnifanBathroom implements Runnable {
	final static int SIZE = 10;

	boolean isUT;
	FairUnifanBathroom bathroom;

	public testFairUnifanBathroom(double d, FairUnifanBathroom bathroom){
		if(d < 0.5){
			this.isUT = true;
		}
		else{
			this.isUT = false;
		}

		this.bathroom = bathroom;
	}

	public void run() {
		long id = Thread.currentThread().getId();

		if(isUT) {
			bathroom.enterBathroomUT();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bathroom.leaveBathroomUT();
		}
		else{
			bathroom.enterBathroomOU();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bathroom.leaveBathroomOU();
		}
	}
	
	public static void main(String[] args) {
		FairUnifanBathroom br = new FairUnifanBathroom();

		Thread[] t = new Thread[SIZE];
		
		for (int i = 0; i < SIZE; ++i) {
			double random = Math.random();
			t[i] = new Thread(new testFairUnifanBathroom(random, br));
		}
		
		for (int i = 0; i < SIZE; ++i) {
			t[i].start();
		}
    }
}
