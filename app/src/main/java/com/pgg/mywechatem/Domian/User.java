package com.pgg.mywechatem.Domian;

import java.io.Serializable;

public class User implements Serializable{
	private String telephone;// 手机号
	private String id;//id号
	private String password;//密码
	private String userName;// 用户名
	private String headUrl;// 头像保存路径
	private String signature;// 个性签名
	private int sex;// 性别: M男士，W女士
	private String location;// 位置信息
	private String birthday;// 生日
	private String type;// N-正常用户，P-公众账号
	private String backgroundUrl;//背景图的Url

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	@Override
	public String toString() {
		return "User{" +
				"telephone='" + telephone + '\'' +
				", id='" + id + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", headUrl='" + headUrl + '\'' +
				", signature='" + signature + '\'' +
				", sex=" + sex +
				", location='" + location + '\'' +
				", birthday='" + birthday + '\'' +
				", type='" + type + '\'' +
				", backgroundUrl='" + backgroundUrl + '\'' +
				'}';
	}
}
