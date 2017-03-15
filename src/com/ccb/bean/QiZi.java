package com.ccb.bean;

import java.awt.Color;

public class QiZi {
	private Color color;//旗子的颜色
	private String name;//旗子的名字
	private int x;//所在的X方向位置
	private int y;//所在的Y方向位置
	private boolean focus = false;//是否被选中
	public QiZi(){
	}
	public QiZi(Color color,String name,int x ,int y){
		this.color = color;
		this.name = name;
		this.x = x;
		this.y = y;
		this.focus = focus;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void setFocus(boolean focus) {
		this.focus = focus;
	}
	public boolean getFocus(){
		return focus;
	}
	
	
	
}
