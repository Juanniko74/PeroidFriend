package com.example.peroidfriend;

public class ModelData {
    private String documentId;
    private String startDate;
    private String finishDate;
    private String expression;
    private String painScale;

    // Constructor
    public ModelData() {
    }

    public ModelData(String startDate, String finishDate, String expression, String painScale) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.expression = expression;
        this.painScale = painScale;
    }

    // Getters and Setters
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getPainScale() {
        return painScale;
    }

    public void setPainScale(String painScale) {
        this.painScale = painScale;
    }
}
