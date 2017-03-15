package com.ccb.client;


import java.io.*;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.ccb.bean.QiZi;
import com.ccb.guize.GuiZe;
import com.ccb.guize.QiPan;

public class ClinentAgentThread extends Thread{
	XiangQi father ;//声明XiangQi的引用
	boolean flag = true;//控制线程的标志位
	public DataInputStream din;
	public DataOutputStream dout;
	public String BTiaoZhanZhe = null;//用于记录正在挑战的对手
	public String tiaoZhanZhe = null;//用于记录挑战者
	public String movePeople = "";//下一个移动棋子的人
	public QiPan qiPan;
	public QiZi[][] qiZi;
	public GuiZe guiZe;
	
	
	public ClinentAgentThread(XiangQi father){
		this.father = father;
		try {
			din = new DataInputStream(father.sc.getInputStream());//创建数据输入输出流
			dout = new DataOutputStream(father.sc.getOutputStream());
			String name = father.jtfNickName.getText().trim();//获得昵称
			dout.writeUTF("<#NICK_NAME#>"+name);//发送昵称到服务器
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(flag){
			try {
				String msg = din.readUTF().trim();//获得服务器发来的信息
				qiPan = new QiPan(this.father.qizi, this.father.width, father);
				System.out.println("Server msg:"+msg);
				if (msg.startsWith("<#NAME_CHONGMING#>")) {// 收到重名的信息
					this.name_chongming();
				} else if (msg.startsWith("<#NICK_LIST#>")) {// 收到昵称列表
					this.nick_list(msg);
				} else if (msg.startsWith("<#SERVER_DOWN#>")) {// 当收到服务器离开的消息
					this.server_down();
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {// 收到接收挑战的信息
					this.tiao_zhan(msg);
				}else if (msg.startsWith("<#TONG_YI#>")) {// 当该用户收到对方接受挑战的信息时
					this.tong_yi(msg);
				}else if (msg.startsWith("<#BUTONG_YI#>")) {// 当该用户收到对方拒接挑战的信息时
					this.butong_yi(msg);
				} else if (msg.startsWith("<#BUSY#>")) {// 当收到对方繁忙的信息时
					this.busy(msg);
				} else if (msg.startsWith("<#MOVE#>")) {// 收到走棋的信息
					this.move(msg);
				}else if (msg.startsWith("<#RENSHU#>")) {// 收到某用户认输的信息
					this.renshu(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void name_chongming(){
		try {
			JOptionPane.showMessageDialog(this.father, "该玩家名称已经被占用,请重新填写","错误",JOptionPane.ERROR_MESSAGE);
			din.close();
			dout.close();			
			this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.father.jtfNickName.setEnabled(!false);//将用于输入昵称的文本框设为可用
			this.father.jbConnect.setEnabled(!false);// 将“连接”按钮设为可用
			this.father.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
			this.father.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
			this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.father.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
			father.sc.close();
			father.sc=null;
			father.cat = null;
			flag = false;//终止该客服代理线程
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void nick_list(String msg){
		String s = msg.substring(13);//分解并得到用户信息
		String[] na = s.split("\\|");
		Vector v = new Vector();
		for(int i=0;i<na.length;i++){
			//用户信息不能为空，切不能重复
			if(na[i].trim().length() !=0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))){
				v.add(na[i]);
			}
		}
		father.jcbNickList.setModel(new DefaultComboBoxModel(v));//设置下拉列表的值
	}
	
	public void server_down(){
		this.father.jtfHost.setEnabled(!false);//将用于输入主机名的文本框设为可用
		this.father.jtfPort.setEnabled(!false);// 将用于输入端口号的文本框设为可用
		this.father.jtfNickName.setEnabled(!false);//将用于输入昵称的文本框设为可用
		this.father.jbConnect.setEnabled(!false);// 将“连接”按钮设为可用
		this.father.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
		this.father.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
		this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
		this.father.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
		flag = false;//终止该客服代理线程
		father.cat = null;
		//给出服务器离开的提示信息
		JOptionPane.showMessageDialog(this.father, "服务器停止！！！","提示",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void tiao_zhan(String msg){
		try {
			String name = msg.substring(13);
			if(this.BTiaoZhanZhe==null){//如果玩家空闲
				String[] s = msg.substring(13).split("\\|");
				this.BTiaoZhanZhe = s[0];
				this.tiaoZhanZhe = s[1];
				System.out.println(this.tiaoZhanZhe+" 向  "+this.BTiaoZhanZhe+" 提出挑战");
				this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
				this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.father.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
				this.father.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
				this.father.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
				this.father.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
				this.father.jbYChallenge.setEnabled(!false);// 将“接受挑战”按钮设为可用
				this.father.jbNChallenge.setEnabled(!false);// 将“拒绝挑战”按钮设为可用
				this.father.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
				JOptionPane.showMessageDialog(this.father, this.tiaoZhanZhe+"向你提出挑战！！","提示",JOptionPane.INFORMATION_MESSAGE);
			}else{
				this.dout.writeUTF("<#BUSY#>"+name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//被挑战者选择 同意 
	public void tong_yi(String msg){
		this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
		this.father.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
		this.father.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
		this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
		this.father.jbFail.setEnabled(!false);// 将“认输”按钮设为可用
		JOptionPane.showMessageDialog(this.father, "对方接受您的挑战！您先走棋（红旗）","提示",JOptionPane.INFORMATION_MESSAGE);		
		this.tiaoZhanZhe = msg.substring(11);
		System.out.println(this.BTiaoZhanZhe+" 同意  "+this.tiaoZhanZhe+" 挑战");
	}
	
	public void butong_yi(String msg){
		this.father.caiPan= false;//将caiPan设为false
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将“断开”按钮设为不可用
		this.father.jbChallenge.setEnabled(true);// 将“挑战”按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
		this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
		this.father.jbFail.setEnabled(!false);// 将“认输”按钮设为不可用
		JOptionPane.showMessageDialog(this.father, "对方不接受您的挑战！","提示",JOptionPane.INFORMATION_MESSAGE);		
		this.BTiaoZhanZhe = null;
		this.tiaoZhanZhe = null;
		String name = msg.substring(11);
		System.out.println("butong_yi name:"+name);
		System.out.println("butong_yi msg:"+msg);
		try {
			this.dout.writeUTF("<#BUTONG_YI#>"+name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void busy(String msg){
		this.father.caiPan= false;//将caiPan设为false
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将“断开”按钮设为不可用
		this.father.jbChallenge.setEnabled(true);// 将“挑战”按钮设为不可用
		this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
		this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
		this.father.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
		JOptionPane.showMessageDialog(this.father, "对方忙碌中","提示",JOptionPane.INFORMATION_MESSAGE);		
		this.BTiaoZhanZhe = null;
	}
	
	//
	public void move(String msg){	
		int length = msg.length();
		System.out.println("MOVE msg:"+msg);
		if(msg.subSequence(8,length-4).equals(BTiaoZhanZhe)){
			this.movePeople = this.tiaoZhanZhe;
		}else if(msg.subSequence(8, length-4).equals(tiaoZhanZhe)){
			this.movePeople = this.BTiaoZhanZhe;
		}
		System.out.println("cat tiaoZhanZhe:"+this.tiaoZhanZhe);
		System.out.println("cat BTiaoZhanZhe:"+this.BTiaoZhanZhe);
		System.out.println("cat movePeople:"+this.movePeople);
		//初始化位置
		int startI =Integer.parseInt(msg.substring(length-4,length-3));
		int startJ =Integer.parseInt(msg.substring(length-3,length-2));
		//走后的位置
		int endI =Integer.parseInt(msg.substring(length-2,length-1));
		int endJ =Integer.parseInt(msg.substring(length-1));
		this.qiPan.move(startI,startJ,endI,endJ);//调用方法走棋
		this.father.caiPan =true;	
	}
	
	public void renshu(String msg){
		JOptionPane.showMessageDialog(this.father, "恭喜你，你获胜了，对方认输","提示",JOptionPane.INFORMATION_MESSAGE);		
		this.BTiaoZhanZhe = null;//将挑战者设为空
		this.father.caiPan= false;//将caiPan设为false
		this.father.color = 0;
		this.father.next();//清空棋盘，进入下一盘
		this.father.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
		this.father.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.father.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
		this.father.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
		this.father.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
		this.father.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
		this.father.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
		this.father.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
		this.father.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
	}

}
