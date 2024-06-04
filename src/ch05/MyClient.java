package ch05;

import java.io.IOException;
import java.net.Socket;

// 2 - 1 상속을 활용한 구현 클래스 설계 하기
public class MyClient extends AbstractClient {

	@Override
	protected void setupSocket() throws IOException {
		super.setSocket(new Socket("localhost", 5002));
		System.out.println("--- connecting ---");
	}

	public static void main(String[] args) {
		MyClient myClient = new MyClient();
		myClient.run();
	}

}
