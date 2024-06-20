package pl.edu.wat.knowledge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wat.knowledge.AbstractContainerBaseTest;
import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Book;
import pl.edu.wat.knowledge.entity.Chapter;
import pl.edu.wat.knowledge.entity.Article;
import pl.edu.wat.knowledge.repository.AuthorRepository;
import pl.edu.wat.knowledge.repository.ArticleRepository;
import pl.edu.wat.knowledge.repository.BookRepository;
import pl.edu.wat.knowledge.repository.ChapterRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @BeforeEach
    public void setup() {
        setUpDatabase();
    }

    @Test
    public void testGetScore() {
        // Dodaj przykładowe dane
        Author author = new Author();
        author.setName("marek");
        author.setSurname("Zmarek");
        authorRepository.save(author);

        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setScore(10);
        article1.setYear(2023);
        article1.setAuthors(List.of(author));
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setScore(20);
        article2.setYear(2023);
        article2.setAuthors(List.of(author));
        articleRepository.save(article2);

        Book book = new Book();
        book.setTitle("Book 1");
        book.setYear(2023);
        bookRepository.save(book);

        Chapter chapter1 = new Chapter();
        chapter1.setTitle("Chapter 1");
        chapter1.setScore(15);
        chapter1.setBook(book);
        chapter1.setAuthors(List.of(author));
        chapterRepository.save(chapter1);

        Chapter chapter2 = new Chapter();
        chapter2.setTitle("Chapter 2");
        chapter2.setScore(25);
        chapter2.setBook(book);
        chapter2.setAuthors(List.of(author));
        chapterRepository.save(chapter2);

        int score = scoreService.getScore(author, 2023);
        assertEquals(70, score);
    }

    @Test
    public void testGetArticleScores() {
        // Dodaj przykładowe dane
        Author author = new Author();
        author.setName("aaa");
        author.setSurname("bbbb");
        authorRepository.save(author);

        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setScore(30);
        article1.setYear(2023);
        article1.setAuthors(List.of(author));
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setScore(50);
        article2.setYear(2023);
        article2.setAuthors(List.of(author));
        articleRepository.save(article2);

        Article article3 = new Article();
        article3.setTitle("Article 3");
        article3.setScore(40);
        article3.setYear(2023);
        article3.setAuthors(List.of(author));
        articleRepository.save(article3);

        List<Double> scores = scoreService.getArticleScores(author, 2023);
        assertEquals(3, scores.size());
        assertEquals(50.0, scores.get(0));
        assertEquals(40.0, scores.get(1));
        assertEquals(30.0, scores.get(2));
    }

    @Test
    public void testGetArticleScoresWithMultipleAuthors() {
        // Dodaj przykładowe dane
        Author author1 = new Author();
        author1.setName("aa");
        author1.setSurname("bb");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("aaaa");
        author2.setSurname("bbbbb");
        authorRepository.save(author2);

        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setScore(30);
        article1.setYear(2023);
        article1.setAuthors(List.of(author1, author2));
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setScore(50);
        article2.setYear(2023);
        article2.setAuthors(List.of(author1, author2));
        articleRepository.save(article2);

        List<Double> scores = scoreService.getArticleScores(author1, 2023);
        assertEquals(2, scores.size());
        assertEquals(25.0, scores.get(0)); // 50 / 2
        assertEquals(15.0, scores.get(1)); // 30 / 2

        scores = scoreService.getArticleScores(author2, 2023);
        assertEquals(2, scores.size());
        assertEquals(25.0, scores.get(0)); // 50 / 2
        assertEquals(15.0, scores.get(1)); // 30 / 2
    }
}