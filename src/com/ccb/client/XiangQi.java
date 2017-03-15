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

	public static final Color bgColor = new Color(245, 250, 160);// 棋盘的背景色
	public static final Color focusbg = new Color(242, 242, 242);// 棋子选中后的背景色
	public static final Color focuschar = new Color(96, 95, 91);// 棋子选中后的字符颜色
	public static final Color color1 = new Color(249, 183, 173);// 红方的颜色
	public static final Color color2 = Color.white;// 白方的颜色
	JLabel jlHost = new JLabel("主机名");// 创建提示输入主机名的标签
	JLabel jlPort = new JLabel("端口号");// 创建提示输入端口号标签
	JLabel jlNickName = new JLabel("昵 称");// 创建提示输入昵称的标签
	public JTextField jtfHost = new JTextField("127.0.0.1");// 创建输入主机名的文本框，默认值是“127.0.0.1”
	public JTextField jtfPort = new JTextField("9999");// 创建输入端口号的文本框，默认值是9999
	public JTextField jtfNickName = new JTextField("Play1");// 创建输入昵称的文本框，默认值是play1
	public JButton jbConnect = new JButton("连接");// 创建“连接”按钮
	public JButton jbDisconnect = new JButton("断开");// 创建“断开”按钮
	public JButton jbFail = new JButton("认输");// 创建“认输”按钮
	public JButton jbChallenge = new JButton("挑战");// 创建“挑战”按钮
	JComboBox jcbNickList = new JComboBox();// 创建存放当前用户的下拉列表框
	public JButton jbYChallenge = new JButton("接受挑战");// 创建“接受挑战”按钮
	public JButton jbNChallenge = new JButton("拒绝挑战");// 创建“拒绝挑战”按钮
	int width = 60;// 设置棋盘两线之间的距离
	public QiZi[][] qizi = new QiZi[9][10];// 创建棋子数组
	QiPan jpz =new QiPan(qizi, width, this);
//	JPanel jpz = new JPanel();// 创建一个JPanel,暂时代替棋盘
	JPanel jpy = new JPanel();// 创建一个JPanel
	JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);// 创建一个JSplitPane
	public boolean caiPan = false;// 可否走棋的标志位
	public int color = 0;// 0代表红棋，1带镖白棋
	Socket sc;// 声明Socket的引用
	public ClinentAgentThread cat;


	public XiangQi() {
		this.initialComponent();// 初始化控件
		this.addListener();// 为相应的控件注册事件监听器
		this.initialState();// 初始化状态
		this.initialQiZi();// 初始化棋子
		this.initialFrame();// 初始化窗体
	}

	public void initialComponent() {
		jpy.setLayout(null);// 设为空布局

		this.jlHost.setBounds(10, 10, 50, 20);
		jpy.add(this.jlHost);// 添加“主机名”标签

		this.jtfHost.setBounds(70, 10, 80, 20);
		jpy.add(this.jtfHost);// 添加用于输入主机名的文本框

		this.jlPort.setBounds(10, 40, 50, 20);
		jpy.add(this.jlPort);// 添加"端口号"标签

		this.jtfPort.setBounds(70, 40, 80, 20);
		jpy.add(this.jtfPort);// 添加用于输入端口号的文本框

		this.jlNickName.setBounds(10, 70, 50, 20);
		jpy.add(jlNickName);// 添加"昵称"标签

		this.jtfNickName.setBounds(70, 70, 80, 20);
		jpy.add(this.jtfNickName);// 添加用于输入昵称的文本框

		this.jbConnect.setBounds(10, 100, 80, 20);
		jpy.add(this.jbConnect);// 添加“连接”按钮

		this.jbDisconnect.setBounds(100, 100, 80, 20);
		jpy.add(this.jbDisconnect);// 添加“断开”按钮

		this.jcbNickList.setBounds(20, 130, 130, 20);
		jpy.add(this.jcbNickList);// 添加用于显示当前用户的下拉列表框

		this.jbChallenge.setBounds(10, 160, 80, 20);
		jpy.add(this.jbChallenge);// 添加“挑战”按钮

		this.jbFail.setBounds(100, 160, 80, 20);
		jpy.add(this.jbFail);// 添加“认输”按钮

		this.jbYChallenge.setBounds(5, 190, 86, 20);
		jpy.add(this.jbYChallenge);// 添加“接受挑战”按钮

		this.jbNChallenge.setBounds(100, 190, 86, 20);
		jpy.add(this.jbNChallenge);// 添加“拒接挑战”按钮
	}

	public void addListener() {
		this.jbConnect.addActionListener(this);// 为“连接”按钮注册事件监听器
		this.jbDisconnect.addActionListener(this);// 为“断开”按钮注册事件监听器
		this.jbChallenge.addActionListener(this);// 为“挑战”按钮注册事件监听器
		this.jbFail.addActionListener(this);// 为“认输”按钮注册事件监听器
		this.jbYChallenge.addActionListener(this);// 为“同意挑战”按钮注册事件监听器
		this.jbNChallenge.addActionListener(this);// 为“拒绝挑战”按钮注册事件监听器
	}

	public void initialState() {
		this.jbDisconnect.setEnabled(false);// 将“断开”按钮设为不可用
		this.jbChallenge.setEnabled(false);// 将“挑战”按钮设为不可用
		this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
		this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
		this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
	}

	public void initialQiZi() {
		// 初始化各红方棋子
		qizi[0][0] = new QiZi(color1, "車", 0, 0);
		qizi[1][0] = new QiZi(color1, "馬", 1, 0);
		qizi[2][0] = new QiZi(color1, "相", 2, 0);
		qizi[3][0] = new QiZi(color1, "仕", 3, 0);
		qizi[4][0] = new QiZi(color1, "帥", 4, 0);
		qizi[5][0] = new QiZi(color1, "仕", 5, 0);
		qizi[6][0] = new QiZi(color1, "相", 6, 0);
		qizi[7][0] = new QiZi(color1, "馬", 7, 0);
		qizi[8][0] = new QiZi(color1, "車", 8, 0);
		qizi[1][2] = new QiZi(color1, "砲", 1, 2);
		qizi[7][2] = new QiZi(color1, "砲", 7, 2);
		qizi[0][3] = new QiZi(color1, "兵", 0, 3);
		qizi[2][3] = new QiZi(color1, "兵", 2, 3);
		qizi[4][3] = new QiZi(color1, "兵", 4, 3);
		qizi[6][3] = new QiZi(color1, "兵", 6, 3);
		qizi[8][3] = new QiZi(color1, "兵", 8, 3);
		// 初始化各白方棋子
		qizi[0][9] = new QiZi(color2, "車", 0, 9);
		qizi[1][9] = new QiZi(color2, "馬", 1, 9);
		qizi[2][9] = new QiZi(color2, "象", 2, 9);
		qizi[3][9] = new QiZi(color2, "士", 3, 9);
		qizi[4][9] = new QiZi(color2, "將", 4, 9);
		qizi[5][9] = new QiZi(color2, "士", 5, 9);
		qizi[6][9] = new QiZi(color2, "象", 6, 9);
		qizi[7][9] = new QiZi(color2, "馬", 7, 9);
		qizi[8][9] = new QiZi(color2, "車", 8, 9);
		qizi[1][7] = new QiZi(color2, "炮", 1, 7);
		qizi[7][7] = new QiZi(color2, "炮", 7, 7);
		qizi[0][6] = new QiZi(color2, "卒", 0, 6);
		qizi[2][6] = new QiZi(color2, "卒", 2, 6);
		qizi[4][6] = new QiZi(color2, "卒", 4, 6);
		qizi[6][6] = new QiZi(color2, "卒", 6, 6);
		qizi[8][6] = new QiZi(color2, "卒", 8, 6);
	}

	public void initialFrame() {
		this.setTitle("中國象棋--客戶端");//设置窗体标题
		Image image = new ImageIcon("ico.gif").getImage();
		this.setIconImage(image);//设置图标
		this.add(this.jsp);//添加 JSplitPane
		jsp.setDividerLocation(730);//设置分割线位置及宽度
		jsp.setDividerSize(4);
		this.setBounds(30, 30, 930, 730);//设置窗体大小
		this.setVisible(true);//设置可见性
		//为窗体添加监听器
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
			// 单击“连接”按钮
			this.jbConnect_event();
		} else if (e.getSource() == this.jbDisconnect) {
			// 单击“断开”按钮
			this.jbDisconnect_event();
		} else if (e.getSource() == this.jbChallenge) {
			// 单击“挑战”按钮
			this.jbChallenge_event();
		} else if (e.getSource() == this.jbYChallenge) {
			// 单击“接受挑战”按钮
			this.jbYChallenge_event();
		} else if (e.getSource() == this.jbNChallenge) {
			// 单击“拒接挑战”按钮
			this.jbNChallenge_event();
		} else if (e.getSource() == this.jbFail) {
			// 单击“认输”按钮
			this.jbFail_event();
		}
	}

	// 对单击“连接”按钮事件的业务处理代码
	public void jbConnect_event() {
		int port = 0;
		try {
			port = Integer.parseInt(this.jtfPort.getText().trim());
		} catch (Exception e) {
			// 不是整数，给出错误提示
			JOptionPane.showMessageDialog(this, "端口号只能是整数", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (port > 65535 || port < 0) {
			// 端口号不合法，给出错误提示
			JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = this.jtfNickName.getText().trim();// 获得昵称
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			sc = new Socket(this.jtfHost.getText().trim(), port);// 创建Socket对象
			if(sc == null){
				JOptionPane.showMessageDialog(this, "连接服务器失败", "错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			cat = new ClinentAgentThread(this);//创建服务器线程
			cat.start();//启动服务器线程
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
			this.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
			JOptionPane.showMessageDialog(this, "已连接到服务器", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "连接服务器失败", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	// 对单击“断开”按钮事件的业务员处理代码
	public void jbDisconnect_event() {
		try {
			this.jtfHost.setEnabled(!false);// 将用于输入主机名的文本框设为可用
			this.jtfPort.setEnabled(!false);// 将用于输入端口号的文本框设为可用
			this.jtfNickName.setEnabled(!false);// 将用于输入昵称的文本框设为可用
			this.jbConnect.setEnabled(!false);// 将“连接”按钮设为可用
			this.jbDisconnect.setEnabled(!true);// 将“断开”按钮设不为可用
			this.jbChallenge.setEnabled(!true);// 将“挑战”按钮设不为可用
			this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 对单击“挑战”按钮事件的业务员处理代码
	public void jbChallenge_event() {
		try {
			Object o = this.jcbNickList.getSelectedItem();
			if (o == null || ((String) o).equals("")) {
				// 当未选中挑战对象，给出提示信息
				JOptionPane.showMessageDialog(this, "请选择对方名字", "错误",
						JOptionPane.ERROR_MESSAGE);
			} else {
				String name2 = (String) this.jcbNickList.getSelectedItem();
				System.out.println("被挑战者 :"+name2);
				this.cat.BTiaoZhanZhe = name2;
				cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2);//将挑战的信息发给 服务端，在发送给 被挑战者
				this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
				this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
				this.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
				this.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
				this.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
				this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
				this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
				this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
				this.caiPan = true;
				this.color = 0;// 红旗
				JOptionPane.showMessageDialog(this, "已提出挑战，请等待回复.....", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 发送接受挑战的事件
	public void jbYChallenge_event() {
		System.out.println("接受挑战");
		try {
			this.caiPan = false;
			this.color = 1;// 白棋
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
			this.jbDisconnect.setEnabled(!true);// 将“断开”按钮设为不可用
			this.jbChallenge.setEnabled(!true);// 将“挑战”按钮设为不可用
			this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.jbFail.setEnabled(!false);// 将“认输”按钮设为可用
			cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe);//同意的信息 发给 挑战者
			System.out.println("挑战者："+this.cat.tiaoZhanZhe);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 发送拒绝挑战的事件
	public void jbNChallenge_event() {
		try {
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
			this.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
			cat.dout.writeUTF("<#BUTONG_YI#>"+this.cat.tiaoZhanZhe);//同意的信息 发给 挑战者
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送认输的信息
	public void jbFail_event() {
		try {
			this.color = 0;// 将颜色设为0
			this.caiPan = false;// 将caiPan设为false
			this.next();// 初始化下一局
			this.jtfHost.setEnabled(false);// 将用于输入主机名的文本框设为不可用
			this.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.jtfNickName.setEnabled(false);// 将用于输入昵称的文本框设为不可用
			this.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
			this.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
			this.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
			this.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设为不可用
			this.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设为不可用
			this.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void next() {
		for (int i = 0; i < 9; i++) {// 将棋子数组都置空
			for (int j = 0; j < 10; j++) {
				this.qizi[i][j] = null;
			}
		}
		this.caiPan = false;
		this.initialQiZi();// 重新初始化棋子
		this.repaint();// 重绘
	}

	public static void main(String[] args) {
		new XiangQi();
	}

}
