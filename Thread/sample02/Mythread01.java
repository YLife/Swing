package sample02;

public class Mythread01 extends Thread {
	@Override
	public void run() {
		for (int i = 1; i < 101; i++) {
			if (i%2==0) {
				System.out.println("ż����:"+i);
			}
		}
	}
}
