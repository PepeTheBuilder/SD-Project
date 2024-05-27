package org.example.proiectfinalsd.Entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class NovelToPrint {

    public String author;

    public String genre;

    public int chapters;

    public String name;

    public NovelToPrint(Long id, String author, String genre, int chapters, String name) {
        this.author = author;
        this.genre = genre;
        this.chapters = chapters;
        this.name = name;
    }

    public NovelToPrint(Manga novel) {
        this.author = novel.getAuthor().getName();
        this.genre = novel.getGenre();
        this.chapters = novel.getChapters();
        this.name = novel.getName();
    }

    public NovelToPrint(Manhwa novel) {
        this.author = novel.getAuthor().getName();
        this.genre = novel.getGenre();
        this.chapters = novel.getChapters();
        this.name = novel.getName();
    }

    public NovelToPrint(LightNovel novel) {
        this.author = novel.getAuthor().getName();
        this.genre = novel.getGenre();
        this.chapters = novel.getChapters();
        this.name = novel.getName();
    }
 }
