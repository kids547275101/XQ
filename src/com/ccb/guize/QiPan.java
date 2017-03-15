package com.ccb.guize;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ccb.bean.QiZi;
import com.ccb.client.XiangQi;

public class QiPan extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;//棋盘两线之间的距离
	boolean focus = false;//棋子的状态
	int jiang1_i = 4;
	int jiang1_j = 0;//“帥”的坐标
	int jiang2_i = 4;
	int jiang2_j = 9;//“将”的坐标
	int startI = -1;
	int startJ = -1;//棋子开始的位置
	int endI = -1;
	int endJ = -1;//棋子的终止位置
	public  QiZi qiZi[][];//棋子数组的引用
	XiangQi xq ;//声明 象棋 的引用
	GuiZe guiZe;//声明规则的引用
	
	

	public QiPan(QiZi qiZi[][],int width,XiangQi xq){
		this.xq = xq;
		this.qiZi = qiZi;
		this.width = width;
		guiZe = new GuiZe(qiZi);
		this.addMouseListener(this);//为棋盘添加鼠标监听器
		this.setBounds(0,0,700,700);//为棋盘的大小
		this.setLayout(null);//空布局
		System.out.println("QiPan");
		//1.游戏 2.大量图片用空布局
	}
	
	public void paint(Graphics g1){
		Graphics2D g =(Graphics2D) g1;//获得 Graphics2D 对象
		//疑问 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//打开抗锯齿
		Color c = g.getColor();//获得画笔颜色
		g.setColor(XiangQi.bgColor);//将画笔设为背景色
		g.fill3DRect(60, 30, 580, 630, false);//绘制一个矩形棋盘，四周的边缘
		g.setColor(Color.black);//将画笔颜色设为黑色
		for(int i=80;i <= 620;i=i+60){//绘制棋盘中的横线
			g.drawLine(110,i,590,i);
		}
		g.drawLine(110, 80, 110, 620);//绘制左边线
		g.drawLine(590, 80, 590, 620);//绘制右边线
		for(int i=170;i<=530;i=i+60){//绘制棋盘中的竖线
			g.drawLine(i, 80, i, 320);
			g.drawLine(i, 380, i, 620);
		}
		g.drawLine(290, 80, 410, 200);//绘制两边的斜线
		g.drawLine(290, 200, 410, 80);
		g.drawLine(290, 500, 410, 620);
		g.drawLine(290, 620, 410, 500);
		this.smllLine(g,1,2);//绘制红炮所在位置的标志
		this.smllLine(g,7,2);//绘制红炮所在位置的标志
		
		this.smllLine(g,0,3);//绘制兵所在位置的标志
		this.smllLine(g,2,3);//绘制兵所在位置的标志
		this.smllLine(g,4,3);//绘制兵所在位置的标志
		this.smllLine(g,6,3);//绘制兵所在位置的标志
		this.smllLine(g,8,3);//绘制兵所在位置的标志
		
		this.smllLine(g,0,6);//绘制卒所在位置的标志
		this.smllLine(g,2,6);//绘制卒所在位置的标志
		this.smllLine(g,4,6);//绘制卒所在位置的标志	
		this.smllLine(g,6,6);//绘制卒所在位置的标志
		this.smllLine(g,8,6);//绘制卒所在位置的标志
		
		this.smllLine(g,1,7);//绘制白炮所在位置的标志
		this.smllLine(g,7,7);//绘制白炮所在位置的标志
		g.setColor(Color.black);
		Font font1 = new Font("宋体",Font.BOLD,50);//设置字体
		g.setFont(font1);
		g.drawString("楚河", 170, 365);
		g.drawString("汉界", 400, 365);//绘制楚河汉界
		Font font = new Font("宋体",Font.BOLD,30);//设置字体
		g.setFont(font);
		for(int i=0;i<9;i++){
			for(int j=0;j<10;j++){//绘制棋子
				if(qiZi[i][j] != null){
					if(this.qiZi[i][j].getFocus() != false){//是否被选中
						g.setColor(XiangQi.focusbg);//选中的背景色
						g.fillOval(110+i*60-25, 80+j*60-25, 50, 50);//绘制该棋子
						g.setColor(XiangQi.focuschar);//字符的颜色
					}else{
						g.fillOval(110+i*60-25, 80+j*60-25, 50, 50);//绘制该棋子
						g.setColor(qiZi[i][j].getColor());//设置画笔颜色
					}
					g.drawString(qiZi[i][j].getName(), 110+i*60-15, 80+j*60+10);
					g.setColor(Color.black);//设为黑色
				}
			}
		}
		g.setColor(c);//还原画笔颜色
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.xq.caiPan == true){//判断是否轮到该玩家走棋
			int i=-1,j=-1;
			int[] pos= getPos(e);
			i = pos[0];
			j = pos[1];
			System.out.println("x:"+i);
			System.out.println("j:"+j);
			if(i>0 && i<=8 && j>=0 && j<9){//如果在棋盘范围内
				if(focus == false){//如果没有选中棋子
					this.noFocus(i,j);
				}else{//如果是以前选中过棋子 
					if(qiZi[i][j] != null){
						if(qiZi[i][j].getColor() == qiZi[startI][startJ].getColor()){
							//如果是自己的棋子
							qiZi[startI][startJ].setFocus(false);
							qiZi[i][j].setFocus(true);//更改选中对象
							startI=i;
							startJ=j;//保存修改
						}else{//如果是对方棋子
							endI = i;//保存移动后的坐标
							endJ = j;
							String name =qiZi[startI][startJ].getName();//获得该棋子的名字
							//看是否可以移动
							boolean canMove = guiZe.canMove(startI,startJ,endI,endJ,name);
							if(canMove){
								try {
									System.out.println("走到 qipan canmove");
									this.xq.cat.dout.writeUTF("<#MOVE#>"+
									this.xq.cat.BTiaoZhanZhe+startI+startJ+endI+endJ);
									this.xq.caiPan = false;
									if(qiZi[endI][endJ].getName().equals("帥") || qiZi[endI][endJ].getName().equals("將")){
										//如果终点处是对方的“将”
										this.success();
									}else{
										this.noJiang();
									}
								} catch (Exception e2) {
									e2.printStackTrace();
									}
							}
						}
					}else{//如果没有棋子
						endI =i;
						endJ =j;//保存坐标
						String name =qiZi[startI][startJ].getName();//获得该旗的名字
						boolean canMove= guiZe.canMove(startI, startJ, endI, endJ, name);//判断是否可走
						if(canMove){//如果可以移动
							this.noQiZi();					
						}
						
					}	
				}
			}
			this.xq.repaint();//重绘
		}	
	}
	
	//获得事件触发的位置
	private int[] getPos(MouseEvent e) {
		int[] pos = new int[2];
		pos[0] = -1;
		pos[1] = -1;
		Point p =e.getPoint();//获得时间发生的坐标
		double x = p.getX();
		double y = p.getY();
		if(Math.abs((x-110)/1%60) <= 25){//获得对应数组下 x 下标的位置
			pos[0] = Math.round((float)(x-110)/60);
		}else if(Math.abs((x-110)/1%60) >= 35){
			pos[0] = Math.round((float)(x-110)/60+1);
		}
		if(Math.abs((y-80)/1%60) <= 25){//获得对应数组下 y 下标的位置
			pos[1] = Math.round((float)(y-80)/60);
		}else if(Math.abs((y-80)/1%60) >= 35){
			pos[1] = Math.round((float)(y-80)/60+1);
		}
		return pos;
	}
	
	
	private void noFocus(int i, int j) {
		if(this.qiZi[i][j] != null){//如果该位置有棋子
			if(this.xq.color ==0){//如果是红方
				if(this.qiZi[i][j].getColor().equals(XiangQi.color1)){//如果棋子是红色
					this.qiZi[i][j].setFocus(true);//将棋子设为选中状态
					focus = true;//将focus设为true
					startI = i;
					startJ = j;//保存坐标点
				}
			}else{//如果是白方
				if(this.qiZi[i][j].getColor().equals(XiangQi.color2)){//如果棋子是白色色
					this.qiZi[i][j].setFocus(true);//将棋子设为选中状态
					focus = true;//将focus设为true
					startI = i;
					startJ = j;//保存坐标点
				}
			}
		}	
	}
	
	public void success(){
		qiZi[endI][endJ] = qiZi[startI][startJ];//吃掉该棋子
		qiZi[startI][startJ] = null;//将原来的位置设为空
		this.xq.repaint();//重绘
		JOptionPane.showMessageDialog(this.xq,"恭喜您，您获胜了","提示",JOptionPane.INFORMATION_MESSAGE);
		this.xq.cat.BTiaoZhanZhe = null;
		this.xq.color = 0;//还原棋牌
		this.xq.caiPan = false;
		this.xq.next();//进入下一盘
		this.xq.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
		this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
		this.xq.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
		this.xq.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
		this.xq.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
		this.xq.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
		this.xq.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
		this.xq.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
		this.xq.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
		startI = -1;//还原保存点
		startJ = -1;
		endI = -1;
		endJ = -1;
		jiang1_i = 4;
		jiang1_j = 0;//“帥”的坐标
		jiang2_i = 4;
		jiang2_j = 9;//“将”的坐标
		focus = false;
	}
	
	public void noJiang(){
		qiZi[endI][endJ] = qiZi[startI][startJ];
		qiZi[startI][startJ] = null;//走棋
		qiZi[endI][endJ].setFocus(false);//将改期设为非选中状态
		this.xq.repaint();//重绘
		if(qiZi[endI][endJ].getName().equals("帥")){//如果移动的是“帥”
			jiang1_i = endI;//更新“帥”的位置
			jiang1_j = endJ;
		}else if(qiZi[endI][endJ].getName().equals("將")){//如果移动的是“將”
			jiang2_i = endI;//更新“將”的位置
			jiang2_j = endJ;
		}
		if(jiang1_i == jiang2_i){//如果“将”和“帥”在一条竖线上
			int count = 0;
			for(int jiang_j =jiang1_j+1;jiang_j<jiang2_j;jiang_j++){//遍历这条竖线上的旗子
				if(qiZi[jiang1_i][jiang_j] != null){
					count++;
					break;
				}
			}
			if(count == 0){//如果等于零则照将
				JOptionPane.showMessageDialog(this.xq,"将军，你失败了！","提示",JOptionPane.INFORMATION_MESSAGE);
				this.xq.cat.BTiaoZhanZhe = null;
				this.xq.color = 0;//还原棋牌
				this.xq.caiPan = false;
				this.xq.next();//进入下一盘
				this.xq.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
				this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
				this.xq.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
				this.xq.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
				this.xq.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
				this.xq.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
				this.xq.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
				this.xq.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
				this.xq.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
				jiang1_i = 4;
				jiang1_j = 0;//“帥”的坐标
				jiang2_i = 4;
				jiang2_j = 9;//“将”的坐标
			}
		}
		startI = -1;//还原保存点
		startJ = -1;
		endI = -1;
		endJ = -1;
		focus = false;
	}
	
	//没有旗子
	public void noQiZi(){
		
		try {
			//将该移动信息发送给对方		
			if( this.xq.cat.movePeople==""){//如果没有人走棋，即走棋人为被挑战者
				this.xq.cat.movePeople = this.xq.cat.BTiaoZhanZhe;
			}
			System.out.println("下一个移动的用户是 ："+this.xq.cat.movePeople);
			System.out.println("将棋子移动信息发送给另一个用户   "+this.xq.cat.movePeople+startI+startJ+endI+endJ);
			this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.movePeople+startI+startJ+endI+endJ);
			this.xq.caiPan = false;
			qiZi[endI][endJ] = qiZi[startI][startJ];
			qiZi[startI][startJ] = null;//走棋
			qiZi[endI][endJ].setFocus(false);//将改期设为非选中状态
			this.xq.repaint();//重绘
			if(qiZi[endI][endJ].getName().equals("帥")){//如果移动的是“帥”
				jiang1_i = endI;//更新“帥”的位置
				jiang1_j = endJ;
			}else if(qiZi[endI][endJ].getName().equals("將")){//如果移动的是“將”
				jiang2_i = endI;//更新“將”的位置
				jiang2_j = endJ;
			}
			if(jiang1_i == jiang2_i){//如果“将”和“帥”在一条竖线上
				int count = 0;
				for(int jiang_j =jiang1_j+1;jiang_j<jiang2_j;jiang_j++){//遍历这条竖线上的旗子
					if(qiZi[jiang1_i][jiang_j] != null){
						count++;
						break;
					}
				}
				if(count == 0){//如果等于零则照将
					JOptionPane.showMessageDialog(this.xq,"将军，你失败了！","提示",JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.BTiaoZhanZhe = null;
					this.xq.color = 0;//还原棋牌
					this.xq.caiPan = false;
					this.xq.next();//进入下一盘
					this.xq.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
					this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
					this.xq.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
					this.xq.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
					this.xq.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
					this.xq.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
					this.xq.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
					this.xq.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
					this.xq.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
					jiang1_i = 4;
					jiang1_j = 0;//“帥”的坐标
					jiang2_i = 4;
					jiang2_j = 9;//“将”的坐标
				}
			}
			startI = -1;//还原保存点
			startJ = -1;
			endI = -1;
			endJ = -1;
			focus = false;
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void move(int startI,int startJ,int endI,int endJ){
		//如果“将”被吃了
		System.out.println("============= move =============");
		System.out.println("qizi[0][0]"+qiZi[startI][startJ].getName());
		System.out.println("============= move =============");
		if(qiZi[endI][endJ] != null && (qiZi[endI][endJ].getName().equals("帥")
				|| qiZi[endI][endJ].getName().equals("將"))){
			qiZi[endI][endJ] = qiZi[startI][startJ];
			qiZi[startI][startJ] = null;//走棋
			this.xq.repaint();//重绘
			JOptionPane.showMessageDialog(this.xq,"很遗憾，您失败！！！","提示",JOptionPane.INFORMATION_MESSAGE);
			this.xq.cat.BTiaoZhanZhe = null;
			this.xq.cat.tiaoZhanZhe = null;
			this.xq.color = 0;//还原棋牌
			this.xq.caiPan = false;
			this.xq.next();//进入下一盘
			this.xq.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
			this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
			this.xq.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
			this.xq.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
			this.xq.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
			this.xq.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
			this.xq.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
			this.xq.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
			this.xq.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
			jiang1_i = 4;
			jiang1_j = 0;//“帥”的坐标
			jiang2_i = 4;
			jiang2_j = 9;//“将”的坐标
		}else{
			//如果不是“将”旗子
			System.out.println("startI:"+startI);
			System.out.println("startJ:"+startJ);
			System.out.println("qiZi[startI][startJ].getName():"+qiZi[startI][startJ].getName());
			qiZi[endI][endJ] = qiZi[startI][startJ];
			qiZi[startI][startJ] = null;//走棋
			this.xq.repaint();//重绘
			System.out.println("qiZi[endI][endJ].getName():"+qiZi[endI][endJ].getName());
			if(qiZi[endI][endJ].getName().equals("帥") ){//如果移动的是“帥”
				jiang1_i = endI;//更新“帥”的位置
				jiang1_j = endJ;
			}else if(qiZi[endI][endJ].getName().equals("將")){//如果移动的是“將”
				jiang2_i = endI;//更新“將”的位置
				jiang2_j = endJ;
			}
			if(jiang1_i == jiang2_i){//如果“将”和“帥”在一条竖线上
				int count = 0;
				for(int jiang_j =jiang1_j+1;jiang_j<jiang2_j;jiang_j++){//遍历这条竖线上的旗子
					if(qiZi[jiang1_i][jiang_j] != null){
						count++;
						break; 
					}
				}
				if(count == 0){//如果等于零则照将
					JOptionPane.showMessageDialog(this.xq,"将军，你失败了！","提示",JOptionPane.INFORMATION_MESSAGE);
					this.xq.cat.BTiaoZhanZhe = null;
					this.xq.color = 0;//还原棋牌
					this.xq.caiPan = false;
					this.xq.next();//进入下一盘
					this.xq.jtfHost.setEnabled(false);//将用于输入主机名的文本框设为不可用
					this.xq.jtfPort.setEnabled(false);// 将用于输入端口号的文本框设为不可用
					this.xq.jtfNickName.setEnabled(false);//将用于输入昵称的文本框设为不可用
					this.xq.jbConnect.setEnabled(false);// 将“连接”按钮设为不可用
					this.xq.jbDisconnect.setEnabled(true);// 将“断开”按钮设为可用
					this.xq.jbChallenge.setEnabled(true);// 将“挑战”按钮设为可用
					this.xq.jbYChallenge.setEnabled(false);// 将“接受挑战”按钮设不为可用
					this.xq.jbNChallenge.setEnabled(false);// 将“拒绝挑战”按钮设不为可用
					this.xq.jbFail.setEnabled(false);// 将“认输”按钮设为不可用
					jiang1_i = 4;
					jiang1_j = 0;//“帥”的坐标
					jiang2_i = 4;
					jiang2_j = 9;//“将”的坐标
				}
			}
		}
		this.xq.repaint();//重绘
	}

	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private void smllLine(Graphics2D g, int i, int j) {
		int x = 110+60*i;
		int y = 80+60*j;
		if(i > 0){//绘制左上方方的标志
			g.drawLine(x-3, y-3, x-20, y-3);
			g.drawLine(x-3, y-3, x-3, y-20);
		}
		if(i < 8){//绘制右上方的标志
			g.drawLine(x+3, y-3, x+20, y-3);
			g.drawLine(x+3, y-3, x+3, y-20);
		}
		if(j > 0){//绘制左下方的标志
			g.drawLine(x-3, y+3, x-20, y+3);
			g.drawLine(x-3, y+3, x-3, y+20);
		}
		if(j < 8){//绘制右下方的标志
			g.drawLine(x+3, y+3, x+20, y+3);
			g.drawLine(x+3, y+3, x+3, y+20);
		}
		
	}

	
	

}
