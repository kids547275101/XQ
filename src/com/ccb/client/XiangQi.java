package com.ccb.client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

import java.nio.ByteBuffer;



import com.ccb.bean.QiZi;
import com.ccb.guize.QiPan;
import com.ccb.server.ServerAgenThread;
import com.ccb.server.ServerThread;

public class XiangQi extends JFrame implements ActionListener {

	public static final Color bgColor = new Color(245, 250, 160);// ���̵ı���ɫ
	public static final Color focusbg = new Color(242, 242, 242);// ����ѡ�к�ı���ɫ
	public static final Color focuschar = new Color(96, 95, 91);// ����ѡ�к���ַ���ɫ
	public static final Color color1 = new Color(249, 183, 173);// �췽����ɫ
	public static final Color color2 = Color.white;// �׷�����ɫ
	JLabel jlHost = new JLabel("������");// ������ʾ�����������ı�ǩ
	JLabel jlPort = new JLabel("�˿ں�");// ������ʾ����˿ںű�ǩ
	JLabel jlNickName = new JLabel("�� ��");// ������ʾ�����ǳƵı�ǩ
	public JTextField jtfHost = new JTextField("127.0.0.1");// �����������������ı���Ĭ��ֵ�ǡ�127.0.0.1��
	public JTextField jtfPort = new JTextField("9999");// ��������˿ںŵ��ı���Ĭ��ֵ��9999
	public JTextField jtfNickName = new JTextField("Play1");// ���������ǳƵ��ı���Ĭ��ֵ��play1
	public JButton jbConnect = new JButton("����");// ���������ӡ���ť
	public JButton jbDisconnect = new JButton("�Ͽ�");// �������Ͽ�����ť
	public JButton jbFail = new JButton("����");// ���������䡱��ť
	public JButton jbChallenge = new JButton("��ս");// ��������ս����ť
	JComboBox jcbNickList = new JComboBox();// ������ŵ�ǰ�û��������б��
	public JButton jbYChallenge = new JButton("������ս");// ������������ս����ť
	public JButton jbNChallenge = new JButton("�ܾ���ս");// �������ܾ���ս����ť
	int width = 60;// ������������֮��ľ���
	public QiZi[][] qizi = new QiZi[9][10];// ������������
	QiPan jpz =new QiPan(qizi, width, this);
//	JPanel jpz = new JPanel();// ����һ��JPanel,��ʱ��������
	JPanel jpy = new JPanel();// ����һ��JPanel
	JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);// ����һ��JSplitPane
	public boolean caiPan = false;// �ɷ�����ı�־λ
	public int color = 0;// 0������壬1���ڰ���
	Socket sc;// ����Socket������
	public ClinentAgentThread cat;


	public XiangQi() {
		this.initialComponent();// ��ʼ���ؼ�
		this.addListener();// Ϊ��Ӧ�Ŀؼ�ע���¼�������
		this.initialState();// ��ʼ��״̬
		this.initialQiZi();// ��ʼ������
		this.initialFrame();// ��ʼ������
	}

	public void initialComponent() {
		jpy.setLayout(null);// ��Ϊ�ղ���

		this.jlHost.setBounds(10, 10, 50, 20);
		jpy.add(this.jlHost);// ��ӡ�����������ǩ

		this.jtfHost.setBounds(70, 10, 80, 20);
		jpy.add(this.jtfHost);// ��������������������ı���

		this.jlPort.setBounds(10, 40, 50, 20);
		jpy.add(this.jlPort);// ���"�˿ں�"��ǩ

		this.jtfPort.setBounds(70, 40, 80, 20);
		jpy.add(this.jtfPort);// �����������˿ںŵ��ı���

		this.jlNickName.setBounds(10, 70, 50, 20);
		jpy.add(jlNickName);// ���"�ǳ�"��ǩ

		this.jtfNickName.setBounds(70, 70, 80, 20);
		jpy.add(this.jtfNickName);// ������������ǳƵ��ı���

		this.jbConnect.setBounds(10, 100, 80, 20);
		jpy.add(this.jbConnect);// ��ӡ����ӡ���ť

		this.jbDisconnect.setBounds(100, 100, 80, 20);
		jpy.add(this.jbDisconnect);// ��ӡ��Ͽ�����ť

		this.jcbNickList.setBounds(20, 130, 130, 20);
		jpy.add(this.jcbNickList);// ���������ʾ��ǰ�û��������б��

		this.jbChallenge.setBounds(10, 160, 80, 20);
		jpy.add(this.jbChallenge);// ��ӡ���ս����ť

		this.jbFail.setBounds(100, 160, 80, 20);
		jpy.add(this.jbFail);// ��ӡ����䡱��ť

		this.jbYChallenge.setBounds(5, 190, 86, 20);
		jpy.add(this.jbYChallenge);// ��ӡ�������ս����ť

		this.jbNChallenge.setBounds(100, 190, 86, 20);
		jpy.add(this.jbNChallenge);// ��ӡ��ܽ���ս����ť
	}

	public void addListener() {
		this.jbConnect.addActionListener(this);// Ϊ�����ӡ���ťע���¼�������
		this.jbDisconnect.addActionListener(this);// Ϊ���Ͽ�����ťע���¼�������
		this.jbChallenge.addActionListener(this);// Ϊ����ս����ťע���¼�������
		this.jbFail.addActionListener(this);// Ϊ�����䡱��ťע���¼�������
		this.jbYChallenge.addActionListener(this);// Ϊ��ͬ����ս����ťע���¼�������
		this.jbNChallenge.addActionListener(this);// Ϊ���ܾ���ս����ťע���¼�������
	}

	public void initialState() {
		this.jbDisconnect.setEnabled(false);// �����Ͽ�����ť��Ϊ������
		this.jbChallenge.setEnabled(false);// ������ս����ť��Ϊ������
		this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
		this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
		this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
	}

	public void initialQiZi() {
		// ��ʼ�����췽����
		qizi[0][0] = new QiZi(color1, "܇", 0, 0);
		qizi[1][0] = new QiZi(color1, "�R", 1, 0);
		qizi[2][0] = new QiZi(color1, "��", 2, 0);
		qizi[3][0] = new QiZi(color1, "��", 3, 0);
		qizi[4][0] = new QiZi(color1, "��", 4, 0);
		qizi[5][0] = new QiZi(color1, "��", 5, 0);
		qizi[6][0] = new QiZi(color1, "��", 6, 0);
		qizi[7][0] = new QiZi(color1, "�R", 7, 0);
		qizi[8][0] = new QiZi(color1, "܇", 8, 0);
		qizi[1][2] = new QiZi(color1, "�h", 1, 2);
		qizi[7][2] = new QiZi(color1, "�h", 7, 2);
		qizi[0][3] = new QiZi(color1, "��", 0, 3);
		qizi[2][3] = new QiZi(color1, "��", 2, 3);
		qizi[4][3] = new QiZi(color1, "��", 4, 3);
		qizi[6][3] = new QiZi(color1, "��", 6, 3);
		qizi[8][3] = new QiZi(color1, "��", 8, 3);
		// ��ʼ�����׷�����
		qizi[0][9] = new QiZi(color2, "܇", 0, 9);
		qizi[1][9] = new QiZi(color2, "�R", 1, 9);
		qizi[2][9] = new QiZi(color2, "��", 2, 9);
		qizi[3][9] = new QiZi(color2, "ʿ", 3, 9);
		qizi[4][9] = new QiZi(color2, "��", 4, 9);
		qizi[5][9] = new QiZi(color2, "ʿ", 5, 9);
		qizi[6][9] = new QiZi(color2, "��", 6, 9);
		qizi[7][9] = new QiZi(color2, "�R", 7, 9);
		qizi[8][9] = new QiZi(color2, "܇", 8, 9);
		qizi[1][7] = new QiZi(color2, "��", 1, 7);
		qizi[7][7] = new QiZi(color2, "��", 7, 7);
		qizi[0][6] = new QiZi(color2, "��", 0, 6);
		qizi[2][6] = new QiZi(color2, "��", 2, 6);
		qizi[4][6] = new QiZi(color2, "��", 4, 6);
		qizi[6][6] = new QiZi(color2, "��", 6, 6);
		qizi[8][6] = new QiZi(color2, "��", 8, 6);
	}

	public void initialFrame() {
		this.setTitle("�Ї�����--�͑���");//���ô������
		Image image = new ImageIcon("ico.gif").getImage();
		this.setIconImage(image);//����ͼ��
		this.add(this.jsp);//��� JSplitPane
		jsp.setDividerLocation(730);//���÷ָ���λ�ü����
		jsp.setDividerSize(4);
		this.setBounds(30, 30, 930, 730);//���ô����С
		this.setVisible(true);//���ÿɼ���
		//Ϊ������Ӽ�����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (sc == null) {
					System.exit(0);
					return;
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.jbConnect) {
			// ���������ӡ���ť
			this.jbConnect_event();
		} else if (e.getSource() == this.jbDisconnect) {
			// �������Ͽ�����ť
			this.jbDisconnect_event();
		} else if (e.getSource() == this.jbChallenge) {
			// ��������ս����ť
			this.jbChallenge_event();
		} else if (e.getSource() == this.jbYChallenge) {
			// ������������ս����ť
			this.jbYChallenge_event();
		} else if (e.getSource() == this.jbNChallenge) {
			// �������ܽ���ս����ť
			this.jbNChallenge_event();
		} else if (e.getSource() == this.jbFail) {
			// ���������䡱��ť
			this.jbFail_event();
		}
	}

	// �Ե��������ӡ���ť�¼���ҵ�������
	public void jbConnect_event() {
		int port = 0;
		try {
			port = Integer.parseInt(this.jtfPort.getText().trim());
		} catch (Exception e) {
			// ��������������������ʾ
			JOptionPane.showMessageDialog(this, "�˿ں�ֻ��������", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (port > 65535 || port < 0) {
			// �˿ںŲ��Ϸ�������������ʾ
			JOptionPane.showMessageDialog(this, "�˿ں�ֻ����0-65535������", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = this.jtfNickName.getText().trim();// ����ǳ�
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "�����������Ϊ��", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			sc = new Socket(this.jtfHost.getText().trim(), port);// ����Socket����
			if(sc == null){
				JOptionPane.showMessageDialog(this, "���ӷ�����ʧ��", "����",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			cat = new ClinentAgentThread(this);//�����������߳�
			cat.start();//�����������߳�
			this.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
			this.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ����
			this.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ����
			this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
			JOptionPane.showMessageDialog(this, "�����ӵ�������", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "���ӷ�����ʧ��", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	// �Ե������Ͽ�����ť�¼���ҵ��Ա�������
	public void jbDisconnect_event() {
		try {
			this.jtfHost.setEnabled(!false);// �������������������ı�����Ϊ����
			this.jtfPort.setEnabled(!false);// ����������˿ںŵ��ı�����Ϊ����
			this.jtfNickName.setEnabled(!false);// �����������ǳƵ��ı�����Ϊ����
			this.jbConnect.setEnabled(!false);// �������ӡ���ť��Ϊ����
			this.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť�費Ϊ����
			this.jbChallenge.setEnabled(!true);// ������ս����ť�費Ϊ����
			this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �Ե�������ս����ť�¼���ҵ��Ա�������
	public void jbChallenge_event() {
		try {
			Object o = this.jcbNickList.getSelectedItem();
			if (o == null || ((String) o).equals("")) {
				// ��δѡ����ս���󣬸�����ʾ��Ϣ
				JOptionPane.showMessageDialog(this, "��ѡ��Է�����", "����",
						JOptionPane.ERROR_MESSAGE);
			} else {
				String name2 = (String) this.jcbNickList.getSelectedItem();
				System.out.println("����ս�� :"+name2);
				this.cat.BTiaoZhanZhe = name2;
				cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2);//����ս����Ϣ���� ����ˣ��ڷ��͸� ����ս��
				this.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
				this.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
				this.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
				this.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
				this.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
				this.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
				this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
				this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
				this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
				this.caiPan = true;
				this.color = 0;// ����
				JOptionPane.showMessageDialog(this, "�������ս����ȴ��ظ�.....", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ���ͽ�����ս���¼�
	public void jbYChallenge_event() {
		System.out.println("������ս");
		try {
			this.caiPan = false;
			this.color = 1;// ����
			this.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
			this.jbDisconnect.setEnabled(!true);// �����Ͽ�����ť��Ϊ������
			this.jbChallenge.setEnabled(!true);// ������ս����ť��Ϊ������
			this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.jbFail.setEnabled(!false);// �������䡱��ť��Ϊ����
			cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe);//ͬ�����Ϣ ���� ��ս��
			System.out.println("��ս�ߣ�"+this.cat.tiaoZhanZhe);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ���;ܾ���ս���¼�
	public void jbNChallenge_event() {
		try {
			this.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
			this.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ����
			this.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ����
			this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
			cat.dout.writeUTF("<#BUTONG_YI#>"+this.cat.tiaoZhanZhe);//ͬ�����Ϣ ���� ��ս��
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����������Ϣ
	public void jbFail_event() {
		try {
			this.color = 0;// ����ɫ��Ϊ0
			this.caiPan = false;// ��caiPan��Ϊfalse
			this.next();// ��ʼ����һ��
			this.jtfHost.setEnabled(false);// �������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);// ����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);// �����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);// �������ӡ���ť��Ϊ������
			this.jbDisconnect.setEnabled(true);// �����Ͽ�����ť��Ϊ����
			this.jbChallenge.setEnabled(true);// ������ս����ť��Ϊ����
			this.jbYChallenge.setEnabled(false);// ����������ս����ť��Ϊ������
			this.jbNChallenge.setEnabled(false);// �����ܾ���ս����ť��Ϊ������
			this.jbFail.setEnabled(false);// �������䡱��ť��Ϊ������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void next() {
		for (int i = 0; i < 9; i++) {// ���������鶼�ÿ�
			for (int j = 0; j < 10; j++) {
				this.qizi[i][j] = null;
			}
		}
		this.caiPan = false;
		this.initialQiZi();// ���³�ʼ������
		this.repaint();// �ػ�
	}

	public static void main(String[] args) {
		new XiangQi();
	}

}
