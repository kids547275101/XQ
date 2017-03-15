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
	XiangQi father ;//����XiangQi������
	boolean flag = true;//�����̵߳ı�־λ
	public DataInputStream din;
	public DataOutputStream dout;
	public String BTiaoZhanZhe = null;//���ڼ�¼������ս�Ķ���
	public String tiaoZhanZhe = null;//���ڼ�¼��ս��
	public String movePeople = "";//��һ���ƶ����ӵ���
	public QiPan qiPan;
	public QiZi[][] qiZi;
	public GuiZe guiZe;
	
	
	public ClinentAgentThread(XiangQi father){
		this.father = father;
		try {
			din = new DataInputStream(father.sc.getInputStream());//�����������������
			dout = new DataOutputStream(father.sc.getOutputStream());
			String name = father.jtfNickName.getText().trim();//����ǳ�
			dout.writeUTF("<#NICK_NAME#>"+name);//�����ǳƵ�������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(flag){
			try {
				String msg = din.readUTF().trim();//��÷�������������Ϣ
				qiPan = new QiPan(this.father.qizi, this.father.width, father);
				System.out.println("Server msg:"+msg);
				if (msg.startsWith("<#NAME_CHONGMING#>")) {// �յ���������Ϣ
					this.name_chongming();
				} else if (msg.startsWith("<#NICK_LIST#>")) {// �յ��ǳ��б�
					this.nick_list(msg);
				} else if (msg.startsWith("<#SERVER_DOWN#>")) {// ���յ��������뿪����Ϣ
					this.server_down();
				} else if (msg.startsWith("<#TIAO_ZHAN#>")) {// �յ�������ս����Ϣ
					this.tiao_zhan(msg);
				}else if (msg.startsWith("<#TONG_YI#>")) {// �����û��յ��Է�������ս����Ϣʱ
					this.tong_yi(msg);
				}else if (msg.startsWith("<#BUTONG_YI#>")) {// �����û��յ��Է��ܽ���ս����Ϣʱ
					this.butong_yi(msg);
				} else if (msg.startsWith("<#BUSY#>")) {// ���յ��Է���æ����Ϣʱ
					this.busy(msg);
				} else if (msg.startsWith("<#MOVE#>")) {// �յ��������Ϣ
					this.move(msg);
				}else if (msg.startsWith("<#RENSHU#>")) {// �յ�ĳ�û��������Ϣ
					this.renshu(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void name_chongming(){
		try {
			JOptionPane.showMessageDialog(this.father, "����������Ѿ���ռ��,��������д","����",JOptionPane.ERROR_MESSAGE);
			din.close();
			dout.close();			
			this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.father.jtfNickName.setEnabled(!false);//�����������ǳƵ��ı�����Ϊ����
			this.father.jbConnect.setEnabled(!false);// �������ӡ���ť��Ϊ����
			this.father.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
			this.father.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
			this.father.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.father.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
			father.sc.close();
			father.sc=null;
			father.cat = null;
			flag = false;//��ֹ�ÿͷ������߳�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void nick_list(String msg){
		String s = msg.substring(13);//�ֽⲢ�õ��û���Ϣ
		String[] na = s.split("\\|");
		Vector v = new Vector();
		for(int i=0;i<na.length;i++){
			//�û���Ϣ����Ϊ�գ��в����ظ�
			if(na[i].trim().length() !=0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))){
				v.add(na[i]);
			}
		}
		father.jcbNickList.setModel(new DefaultComboBoxModel(v));//���������б��ֵ
	}
	
	public void server_down(){
		this.father.jtfHost.setEnabled(!false);//�������������������ı�����Ϊ����
		this.father.jtfPort.setEnabled(!false);// ����������˿ںŵ��ı�����Ϊ����
		this.father.jtfNickName.setEnabled(!false);//�����������ǳƵ��ı�����Ϊ����
		this.father.jbConnect.setEnabled(!false);// �������ӡ���ť��Ϊ����
		this.father.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
		this.father.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
		this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
		this.father.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
		flag = false;//��ֹ�ÿͷ������߳�
		father.cat = null;
		//�����������뿪����ʾ��Ϣ
		JOptionPane.showMessageDialog(this.father, "������ֹͣ������","��ʾ",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void tiao_zhan(String msg){
		try {
			String name = msg.substring(13);
			if(this.BTiaoZhanZhe==null){//�����ҿ���
				String[] s = msg.substring(13).split("\\|");
				this.BTiaoZhanZhe = s[0];
				this.tiaoZhanZhe = s[1];
				System.out.println(this.tiaoZhanZhe+" ��  "+this.BTiaoZhanZhe+" �����ս");
				this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
				this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
				this.father.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
				this.father.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
				this.father.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
				this.father.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
				this.father.jbYChallenge.setEnabled(!false);// ����������ս����ť��Ϊ����
				this.father.jbNChallenge.setEnabled(!false);// �����ܾ���ս����ť��Ϊ����
				this.father.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
				JOptionPane.showMessageDialog(this.father, this.tiaoZhanZhe+"���������ս����","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			}else{
				this.dout.writeUTF("<#BUSY#>"+name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//����ս��ѡ�� ͬ�� 
	public void tong_yi(String msg){
		this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
		this.father.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
		this.father.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ����������ս����ť�費Ϊ����
		this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť�費Ϊ����
		this.father.jbFail.setEnabled(!false);// �������䡱��ť��Ϊ����
		JOptionPane.showMessageDialog(this.father, "�Է�����������ս���������壨���죩","��ʾ",JOptionPane.INFORMATION_MESSAGE);		
		this.tiaoZhanZhe = msg.substring(11);
		System.out.println(this.BTiaoZhanZhe+" ͬ��  "+this.tiaoZhanZhe+" ��ս");
	}
	
	public void butong_yi(String msg){
		this.father.caiPan= false;//��caiPan��Ϊfalse
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ������
		this.father.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ����������ս����ť�費Ϊ����
		this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť�費Ϊ����
		this.father.jbFail.setEnabled(!false);// �������䡱��ť��Ϊ������
		JOptionPane.showMessageDialog(this.father, "�Է�������������ս��","��ʾ",JOptionPane.INFORMATION_MESSAGE);		
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
		this.father.caiPan= false;//��caiPan��Ϊfalse
		this.father.color = 0;
		this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ������
		this.father.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ������
		this.father.jbYChallenge.setEnabled(false);// ����������ս����ť�費Ϊ����
		this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť�費Ϊ����
		this.father.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
		JOptionPane.showMessageDialog(this.father, "�Է�æµ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);		
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
		//��ʼ��λ��
		int startI =Integer.parseInt(msg.substring(length-4,length-3));
		int startJ =Integer.parseInt(msg.substring(length-3,length-2));
		//�ߺ��λ��
		int endI =Integer.parseInt(msg.substring(length-2,length-1));
		int endJ =Integer.parseInt(msg.substring(length-1));
		this.qiPan.move(startI,startJ,endI,endJ);//���÷�������
		this.father.caiPan =true;	
	}
	
	public void renshu(String msg){
		JOptionPane.showMessageDialog(this.father, "��ϲ�㣬���ʤ�ˣ��Է�����","��ʾ",JOptionPane.INFORMATION_MESSAGE);		
		this.BTiaoZhanZhe = null;//����ս����Ϊ��
		this.father.caiPan= false;//��caiPan��Ϊfalse
		this.father.color = 0;
		this.father.next();//������̣�������һ��
		this.father.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
		this.father.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
		this.father.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
		this.father.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
		this.father.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ����
		this.father.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ����
		this.father.jbYChallenge.setEnabled(false);// ����������ս����ť�費Ϊ����
		this.father.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť�費Ϊ����
		this.father.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
	}

}
