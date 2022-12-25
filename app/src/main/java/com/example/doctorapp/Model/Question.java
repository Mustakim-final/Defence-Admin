package com.example.doctorapp.Model;

public class Question {
    private String id,question,questionType,questionImage,imageUrl,username;

    public Question() {
    }

    public Question(String id, String question, String questionType, String questionImage, String imageUrl, String username) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.questionImage = questionImage;
        this.imageUrl=imageUrl;
        this.username=username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
