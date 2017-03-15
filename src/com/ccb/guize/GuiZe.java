package com.ccb.guize;

import com.ccb.bean.QiZi;

public class GuiZe {
	QiZi[][] qiZi;//声明棋子的数组
	boolean canMove = false;
	int i;
	int j;
	
	public GuiZe(QiZi[][] qiZi){
		this.qiZi = qiZi;
	}
	
	public boolean canMove(int startI,int startJ,int endI,int endJ,String name){
		//I为横坐标，J为纵坐标
		int maxI;//定义一些辅助变量
		int minI;
		int maxJ;
		int minJ;
		canMove = true;
		//有疑问？
		if(startI>=endI){//确定起始坐标的大小关系
			maxI = startI;
			minI = endI;
		}else{//确定maxI和minI
			maxI = endI;
			minI = startI;
		}
		if(startJ>=endJ){
			maxJ = startJ;
			minJ = endJ;
		}else{
			maxJ = endJ;
			minJ = startJ;
		}
		
		if(name.equals("車")){
			this.ju(maxI,minI,maxJ,minJ);
		}else if(name.equals("馬")){
			this.ma(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("相")){
			this.xiang1(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("象")){
			this.xiang2(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("仕")||name.equals("士")){
			this.shi(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("帥")||name.equals("將")){
			this.jiang(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("砲")||name.equals("炮")){
			this.pao(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("兵")){
			this.bing(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("卒")){
			System.out.println("卒开始走");
			this.zu(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		
		return canMove;
	}
	
	//对“車”的处理方法
	private void ju(int maxI, int minI, int maxJ, int minJ) {
		if(maxI==minI){//如果在一条横线
			for(j=minJ+1;j<maxJ;j++){
				if(qiZi[maxI][j]!=null){//如果中间有棋子
					canMove = false;//不可以走棋
					break;
				}
			}
		}else if(maxJ == minJ){//如果在一条竖线上
			for(i=minJ+1;i<maxJ;i++){
				if(qiZi[i][maxJ] != null){
					canMove = false;//不可以走棋
					break;
				}
			}
		}else if(maxI!=minI&&maxJ!=minJ){//如果不在同一条直线上
			canMove = false;
		}
	}
	
	//对“马”的处理方法
	private void ma(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI-minI;//获得两坐标点之间的差
		int b = maxJ-maxJ;
		//如果是竖着的“日”字
		if(a==1 && b==2){
			if(startJ > endJ){//如果从 下向上走
				if(qiZi[startI][startJ-1] != null){//如果马腿处有棋子
					canMove = false;
				}
			}else{
				if(qiZi[startI][startJ+1] != null){//如果马腿处有棋子
					canMove = false;
				}
			}
		}else if(a==2 && b==1){
			//如果是横着的“日”
			if(startJ > endJ){//如果从 右往左走
				if(qiZi[startI-1][startJ] != null){//如果马腿处有棋子
					canMove = false;
				}
			}else{
				if(qiZi[startI+1][startJ] != null){//如果马腿处有棋子
					canMove = false;
				}
			}
			
		}else{
			canMove = false;
		}
	}
	
	//红旗 “相”
	private void xiang1(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI-minI;//获得两坐标点之间的差
		int b = maxJ-maxJ;
		if(a==2 && b==2){//如果是“田"字
			if(endJ > 4){//如果过河
				canMove = false;
			}
			if(qiZi[(maxI+minI)/2][(maxJ+minJ)/2] != null){
				canMove = false;
			}
		}else{
			canMove = false;
		}	
	}
	
	//白棋“象”
	private void xiang2(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI-minI;//获得两坐标点之间的差
		int b = maxJ-maxJ;
		if(a==2 && b==2){//如果是“田"字
			if(endJ < 5){//如果过河
				canMove = false;
			}
			if(qiZi[(maxI+minI)/2][(maxJ+minJ)/2] != null){
				canMove = false;
			}
		}else{
			canMove = false;
		}
		
	}
	
	private void shi(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI-minI;//获得两坐标点之间的差
		int b = maxJ-maxJ;
		if(a==1 && b==1){//如果是小斜线
			if(startJ < 4){//如果是下方的士
				if(endJ < 7){
					canMove = false;//如果下方的士越界，不可以走
				}
			}else{//如果是上方的士
				if(endJ > 2){//如果上方的仕越界，不可以走
					canMove = false;
				}
			}
			if(endI > 5 || endI < 3){//如果左右越界，则不可以走
				canMove = false;
			}
		}else{//如果不是小斜线
			canMove = false;
		}		
	}
	
	//对“将，帥”的处理
	private void jiang(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI-minI;//获得两坐标点之间的差
		int b = maxJ-maxJ;
		if(a==1 && b==0 || a==0 && b==1){//如果走的是一小格
			if(startJ > 4){//如果是下方的将
				if(endJ < 7){
					canMove = false;//如果越界，不可以走
				}
			}else{//如果是上方的帥
				if(endJ > 2){//如果越界，不可以走
					canMove = false;
				}
			}
			if(endI > 5 || endI < 3){//如果左右越界，则不可以走
				canMove = false;
			}
		}else{//如果走的不是一格，不可以走
			canMove = false;
		}
	}

	//对“炮，砲”的处理
	private void pao(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(maxI == minI){
			if(qiZi[endI][endJ] != null){//如果终点有棋子
				int count = 0;
				for(j=minJ+1;j<maxJ;j++){
					//判断起点与终点之间有几个棋子
					if(qiZi[minI][j] != null){
						count++;
					}
				}
				if(count != 1){
					//如果不是一个棋子，不可以走
					canMove = false;
				}
			}else{//如果终点没有棋子
				for(j=minJ+1;j<maxJ;j++){
					//判断起点与终点之间有几个棋子
					if(qiZi[minI][j] != null){
						canMove = false;
						break;
					}
				}
			}
		}else if(maxI == minI){//如果走的是横线
			if(qiZi[endI][endJ] != null){//如果终点有棋子
				int count = 0;
				for(i=minJ+1;i<maxJ;i++){
					//判断起点与终点之间有几个棋子
					if(qiZi[i][minJ] != null){
						count++;
					}
				}
				if(count != 1){
					//如果不是一个棋子，不可以走
					canMove = false;
				}
			}else{//如果终点没有棋子
				for(i=minJ+1;i<maxJ;i++){
					//判断起点与终点之间有几个棋子
					if(qiZi[i][minJ] != null){
						canMove = false;
						break;
					}
				}
			}
		}else{//既不走横线，也不走竖线
			canMove = false;
		}
		
	}
	
	//对"兵"的处理
	private void bing(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(startJ < 5){//如果还没有过河
			if(startI  != endI){//如果不是向前走
				canMove = false;
			}
			if(endJ-startJ != 1){//如果走的不是一格
				canMove = false;
			}
		}else{//如果已经过河
			if(startI == endI){//如果走的是竖线
				if(endJ-startJ != 1){//如果走的不是一格
					canMove = false;
				}
			}else if(startJ == endJ){
				//如果走的是横线
				if(maxI-minI != 1){
					//如果走的不是一格
					canMove = false;
				}
			}else{//既不走横线，也不走竖线
				canMove = false;
			}
		}
		System.out.println("兵 开始走");
	}
	
	//对"卒"的处理
	private void zu(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(startJ > 4){//如果还没有过河
			if(startI != endI){//如果不是向前走
				canMove = false;
			}
			if(startJ - endJ != 1){//如果走的不是一格
				canMove = false;
			}
		}else{//如果已经过河
			if(startI == endI){//如果走的是竖线
				if(startJ - endJ != 1){//如果走的不是一格
					canMove = false;
				}
			}else if(startJ == endJ){
				//如果走的是横线
				if(maxI-minI != 1){
					//如果走的不是一格
					canMove = false;
				}
			}else{//既不走横线，也不走竖线
				canMove = false;
			}
		}
		System.out.println("卒 开始走");
	}


}
