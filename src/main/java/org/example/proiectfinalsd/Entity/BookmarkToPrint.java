package org.example.proiectfinalsd.Entity;

import javax.persistence.*;

public class BookmarkToPrint {

    public String name;

    public Integer userLastChapter;

    public String userReadingStatus;

    public Float userScore;

    public BookmarkToPrint(String name, Integer userLastChapter, String userReadingStatus, Float userScore) {
        this.name = name;
        this.userLastChapter = userLastChapter;
        this.userReadingStatus = userReadingStatus;
        this.userScore = userScore;
    }

    public BookmarkToPrint(BookmarkManhwa bookmarkManhwa) {
        this.name = bookmarkManhwa.getManhwa().getName();
        this.userLastChapter = bookmarkManhwa.getUserLastChapter();
        this.userReadingStatus = bookmarkManhwa.getUserReadingStatus();
        this.userScore = bookmarkManhwa.getUserScore();
    }

    public BookmarkToPrint(BookmarkManga bookmarkManga) {
        this.name = bookmarkManga.getManga().getName();
        this.userLastChapter = bookmarkManga.getUserLastChapter();
        this.userReadingStatus = bookmarkManga.getUserReadingStatus();
        this.userScore = bookmarkManga.getUserScore();
    }

    public BookmarkToPrint(BookmarkLightNovel bookmarkLightNovel) {
        this.name = bookmarkLightNovel.getLightNovel().getName();
        this.userLastChapter = bookmarkLightNovel.getUserLastChapter();
        this.userReadingStatus = bookmarkLightNovel.getUserReadingStatus();
        this.userScore = bookmarkLightNovel.getUserScore();
    }

    @Override
    public String toString() {
        return  "{name='" + name + '\'' +
                ", userLastChapter=" + userLastChapter +
                ", userReadingStatus='" + userReadingStatus + '\'' +
                ", userScore=" + userScore+"}";
    }
}
