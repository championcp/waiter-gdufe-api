package com.waiterxiaoyy.backandroiddesign.entity;

public class Explain {
    private String studentid;
    private double totalCredit;
    private double noNeedCredit;
    private double receivedCredit;
    private double pendingCredit;
    private double majorGPA;
    private double minorGPA;

    public Explain() {
    }

    public Explain(String studentid, double totalCredit, double noNeedCredit, double receivedCredit, double pendingCredit, double majorGPA, double minorGPA) {
        this.studentid = studentid;
        this.totalCredit = totalCredit;
        this.noNeedCredit = noNeedCredit;
        this.receivedCredit = receivedCredit;
        this.pendingCredit = pendingCredit;
        this.majorGPA = majorGPA;
        this.minorGPA = minorGPA;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getNoNeedCredit() {
        return noNeedCredit;
    }

    public void setNoNeedCredit(double noNeedCredit) {
        this.noNeedCredit = noNeedCredit;
    }

    public double getReceivedCredit() {
        return receivedCredit;
    }

    public void setReceivedCredit(double receivedCredit) {
        this.receivedCredit = receivedCredit;
    }

    public double getPendingCredit() {
        return pendingCredit;
    }

    public void setPendingCredit(double pendingCredit) {
        this.pendingCredit = pendingCredit;
    }

    public double getMajorGPA() {
        return majorGPA;
    }

    public void setMajorGPA(double majorGPA) {
        this.majorGPA = majorGPA;
    }

    public double getMinorGPA() {
        return minorGPA;
    }

    public void setMinorGPA(double minorGPA) {
        this.minorGPA = minorGPA;
    }

    @Override
    public String toString() {
        return "个人说明：" +
                "一共需要修读" + totalCredit + "学分" +
                ", 免修" + noNeedCredit +"学分" +
                ", 已修读" + receivedCredit + "学分" +
                ", 还需修读" + pendingCredit + "学分" +
                ", 主修课程平均学分绩点" + majorGPA + "。" +
                ", 辅修课程平均学分绩点" + minorGPA + "。";
    }
}
