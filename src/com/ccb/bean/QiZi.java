package com.ccb.bean;

import java.awt.Color;

public class QiZi {
	private Color color;//���ӵ���ɫ
	private String name;//���ӵ�����
	private int x;//���ڵ�X����λ��
	private int y;//���ڵ�Y����λ��
	private boolean focus = false;//�Ƿ�ѡ��
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
