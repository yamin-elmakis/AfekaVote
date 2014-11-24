package com.afekavote.objects;


public class WeightedVote {

	private int grade;
	private String appName, category, voteGrade;
	private VoteAdapter adapter;
	
	public WeightedVote(String appName, String category, int grade) {
		this.grade = grade;
		this.appName = appName;
		this.category = category;
	}

	public Integer getGrade(){
		return grade;
	}

	public String getAppName() {
		return appName;
	}

	public String getCategory() {
		return category;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public VoteAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(VoteAdapter adapter) {
		this.adapter = adapter;
	}
	
	public String getKey(){
		return appName+"_"+category;
	}
}
