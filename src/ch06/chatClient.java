package ch06;

import java.io.IOException;
import java.net.Socket;

public class chatClient extends AbstractClient {

	public chatClient(String name) {
		super(name);
	}

	@Override
	protected void connectToSever() throws IOException {
		// AbstractClient --> 부모 클래스 --> 서버측과 연결된 소켓을 주입해주어야 한다.
		super.setSocket(new Socket("192.168.0.48", 5000));
	}

	public static void main(String[] args) {
		chatClient chatClient = new chatClient("ㅇㅇ");
		chatClient.run();
	}
}
