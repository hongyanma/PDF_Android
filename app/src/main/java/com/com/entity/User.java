package com.com.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {

	private int user_id;
	private String account;
	private String password;
	private int sex;
	//0表示男 1表示女
	private String phone;
	private String mail;
	private String nickname;
	private int user_state;
	private String pic;
	private int role_id; //角色id
    private String color; //用户的所属颜色
	private String role_name; //角色名称

	public User(){};

	public  User(JSONObject json){
		try {
			this.user_id=json.getInt("user_id");
			this.nickname=json.getString("nickname");
            this.role_name=json.getString("role_name");
			this.color=json.getString("color");
			this.role_id=json.getInt("role_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public  User(JSONObject json,int i){
		try {
			if(i==2){
				this.user_id=json.getInt("user_id");
				this.role_name=json.getString("role_name");
				this.nickname=json.getString("nickname");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String>toLoginJson(String account, String password) {
		// TODO Auto-generated method stub
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("method","login");
		map.put("account", account);
		map.put("password",password);

		return map;
	}




	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPhone() {
		return phone;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", account=" + account + ", password=" + password + ", sex=" + sex
				+ ", phone=" + phone + ", nickname=" + nickname + ", user_state=" + user_state + ", pic=" + pic
				+ ", role_id=" + role_id + "]";
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getUser_state() {
		return user_state;
	}
	public void setUser_state(int user_state) {
		this.user_state = user_state;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


}
