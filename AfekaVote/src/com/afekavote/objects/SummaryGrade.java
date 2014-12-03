package com.afekavote.objects;

public class SummaryGrade implements Comparable<SummaryGrade>{

	private int grade;
	private String appName;
	private GradeAdapter adapter;
	
	public SummaryGrade(int grade, String appName) {
		this.grade = grade;
		this.appName = appName;
	}
	
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setAdapter(GradeAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public int compareTo(SummaryGrade another) {
		return another.getGrade() - grade;
	}
	
}
