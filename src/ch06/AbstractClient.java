package ch06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractClient {

	private String name;
	private Socket socket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
	private BufferedReader keyboradReader;

	public AbstractClient(String name) {
		this.name = name;
	}

	// 외부에서 나의 멤버 변수에 참조변수를 주입 받을수 있도록 setter 메소드 설계
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public final void run() {
		try {
			connectToSever();
			setupStream();
			startService(); // join 걸어둔 상태
		} catch (IOException e) {
			System.out.println("접속 종료");
		} finally {
			cleanup();
		}
	}

	protected abstract void connectToSever() throws IOException;

	private void setupStream() throws IOException {
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
		keyboradReader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void startService() throws IOException {

		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();

		// 스레드 시작
		readThread.start();
		writeThread.start();
		// 메인 스레드 대기 처리
		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
		}
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboradReader.readLine()) != null) {
					socketWriter.println("[" + name + "]" + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = socketReader.readLine()) != null) {
					System.out.println("방송옴 : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void cleanup() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
