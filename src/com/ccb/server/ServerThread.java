package com.ccb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
	
	Server father;//����Server������
	ServerSocket ss;//����ServerSocket������
	boolean flag = true;
	
	public ServerThread(Server father){
		this.father = father;
		ss = father.ss;
	}
	
	@Override
	public void run(){
		while(flag){
			try {
				Socket sc = ss.accept();//�ȴ��ͻ�������
				ServerAgenThread sat = new ServerAgenThread(father,sc);
				sat.start();//���������������������߳�
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//�ȴ��ͻ�������
		}
		
	}

}
