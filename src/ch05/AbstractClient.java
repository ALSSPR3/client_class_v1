package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용한 리팩토링
public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readStream;
	private PrintWriter writeStream;
	private BufferedReader keyboardReader;

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public final void run() {
		try {
			setupSocket();
			setupStream();
			startThread();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socketClear();
		}
	}

	// 1. 아이피 및 포트 할당
	protected abstract void setupSocket() throws IOException;

	// 2. 스트림 초기화
	private void setupStream() throws IOException {
		readStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writeStream = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 3. 서비스 시작
	private void startThread() {
		Thread readThread = readThread(readStream);
		Thread writeThread = writeThread(writeStream, keyboardReader);

		readThread.start();
		writeThread.start();
		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 서버 inputData
	private Thread readThread(BufferedReader bufferedReader) {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = bufferedReader.readLine()) != null) {
					System.out.println("서버 msg : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 클라이언트 outputData
	private Thread writeThread(PrintWriter printWriter, BufferedReader keyboardReader) {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					printWriter.println(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 소켓 자원 close
	private void socketClear() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
