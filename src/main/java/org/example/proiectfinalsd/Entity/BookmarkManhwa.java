package org.example.proiectfinalsd.Entity;

import javax.persistence.*;
@Entity
@Table(name = "bookmark_manhwa")
public class BookmarkManhwa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_manhwa", nullable = false)
    private Manhwa novel;

    @Column(name = "user_last_chapter")
    private Integer userLastChapter;

    @Column(name = "user_reading_status")
    private String userReadingStatus;

    @Column(name = "user_score")
    private Float userScore;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Manhwa getManhwa() {
        return novel;
    }

    public void setManhwa(Manhwa manhwa) {
        this.novel = manhwa;
    }

    public Integer getUserLastChapter() {
        return userLastChapter;
    }

    public void setUserLastChapter(Integer userLastChapter) {
        this.userLastChapter = userLastChapter;
    }

    public String getUserReadingStatus() {
        return userReadingStatus;
    }

    public void setUserReadingStatus(String userReadingStatus) {
        this.userReadingStatus = userReadingStatus;
    }

    public Float getUserScore() {
        return userScore;
    }

    public void setUserScore(Float userScore) {
        this.userScore = userScore;
    }

    public BookmarkToPrint getBookmarkToPrint() {
        return new BookmarkToPrint(this);
    }

    public BookmarkManhwa(BookmarkToPrint bookmarkToPrint, User user) {
        this.user = user;
        this.userLastChapter = bookmarkToPrint.userLastChapter;
        this.userReadingStatus = bookmarkToPrint.userReadingStatus;
        this.userScore = bookmarkToPrint.userScore;
        ///TODO: set manhwa


    }
}
