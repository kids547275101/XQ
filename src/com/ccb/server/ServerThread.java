package com.ccb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
	
	Server father;//声明Server的引用
	ServerSocket ss;//声明ServerSocket的引用
	boolean flag = true;
	
	public ServerThread(Server father){
		this.father = father;
		ss = father.ss;
	}
	
	@Override
	public void run(){
		while(flag){
			try {
				Socket sc = ss.accept();//等待客户端连接
				ServerAgenThread sat = new ServerAgenThread(father,sc);
				sat.start();//创建并启动服务器代理线程
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//等待客户端连接
		}
		
	}

}
