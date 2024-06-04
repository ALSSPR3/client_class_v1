package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 1단계 - 함수로 분리 해서 리팩토링 진행
public class MultiThreadClient {

	// 메인 함수
	public static void main(String[] args) {

		try (Socket socket = new Socket("localhost", 5101)) {
			// 서버 통신을 위한 스트림 초기화
			BufferedReader readStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writeStream = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardStream = new BufferedReader(new InputStreamReader(System.in));

			startReadThread(readStream);
			startWriteThread(writeStream, keyboardStream);

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// 1. 클라이언트로 부터 데이터를 읽는 스레드 시작 메서드 생성
	private static void startReadThread(BufferedReader reader) {
		Thread readThread = new Thread(() -> {
			try {
				String servermsg;
				while ((servermsg = reader.readLine()) != null) {
					System.out.println("서버 msg : " + servermsg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		readThread.start();
	}

	// 2. 키보드에서 입력을 받아 클라이언트 측으로 데이터를 전송하는 스레드
	private static void startWriteThread(PrintWriter writer, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String clientmsg;
				while ((clientmsg = keyboardReader.readLine()) != null) {
					writer.println(clientmsg);
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
		try {
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
