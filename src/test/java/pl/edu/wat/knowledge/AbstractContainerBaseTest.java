package pl.edu.wat.knowledge;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Article;
import pl.edu.wat.knowledge.entity.Book;
import pl.edu.wat.knowledge.entity.Journal;
import pl.edu.wat.knowledge.entity.Publisher;
import pl.edu.wat.knowledge.entity.Chapter;
import pl.edu.wat.knowledge.entity.Affiliation;
import pl.edu.wat.knowledge.repository.AuthorRepository;
import pl.edu.wat.knowledge.repository.ArticleRepository;
import pl.edu.wat.knowledge.repository.BookRepository;
import pl.edu.wat.knowledge.repository.JournalRepository;
import pl.edu.wat.knowledge.repository.PublisherRepository;
import pl.edu.wat.knowledge.repository.ChapterRepository;
import pl.edu.wat.knowledge.repository.AffiliationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Testcontainers
public abstract class AbstractContainerBaseTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected ArticleRepository articleRepository;
    @Autowired
    protected BookRepository bookRepository;
    @Autowired
    protected JournalRepository journalRepository;
    @Autowired
    protected PublisherRepository publisherRepository;
    @Autowired
    protected ChapterRepository chapterRepository;
    @Autowired
    protected AffiliationRepository affiliationRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setUpDatabase() {
        authorRepository.deleteAll();
        articleRepository.deleteAll();
        bookRepository.deleteAll();
        journalRepository.deleteAll();
        publisherRepository.deleteAll();
        chapterRepository.deleteAll();
        affiliationRepository.deleteAll();

        List<Author> authors = new ArrayList<>();
        List<Affiliation> affiliations = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<Journal> journals = new ArrayList<>();

        // Create 10 affiliations
        for (int i = 0; i < 10; i++) {
            Affiliation affiliation = new Affiliation();
            affiliation.setName("Affiliation " + i);
            affiliations.add(affiliationRepository.save(affiliation));
        }

        // Create 20 authors
        for (int i = 0; i < 20; i++) {
            Author author = new Author();
            author.setName("AuthorName" + i);
            author.setSurname("AuthorSurname" + i);
            author.setAffiliation(affiliations.get(new Random().nextInt(affiliations.size())));
            authors.add(authorRepository.save(author));
        }

        // Create 5 publishers
        for (int i = 0; i < 5; i++) {
            Publisher publisher = new Publisher();
            publisher.setName("Publisher " + i);
            publisher.setLocation("Location " + i);
            publishers.add(publisherRepository.save(publisher));
        }

        // Create 15 books
        for (int i = 0; i < 15; i++) {
            Book book = new Book();
            book.setIsbn("978-3-16-14841" + i);
            book.setYear(2020 + i);
            book.setBaseScore(10);
            book.setTitle("Book " + i);
            book.setEditor(authors.get(new Random().nextInt(authors.size())));
            book.setPublisher(publishers.get(new Random().nextInt(publishers.size())));
            books.add(bookRepository.save(book));
        }

        // Create 10 journals
        for (int i = 0; i < 10; i++) {
            Journal journal = new Journal();
            journal.setBaseScore(5);
            journal.setTitle("Journal " + i);
            journal.setIssn("1234-567" + i);
            journal.setPublisher(publishers.get(new Random().nextInt(publishers.size())));
            journals.add(journalRepository.save(journal));
        }

        // Create 20 articles
        for (int i = 0; i < 20; i++) {
            Article article = new Article();
            article.setTitle("Article " + i);
            article.setNo(i);
            article.setCollection("Collection " + i);
            article.setScore(20);
            article.setArticleNo(1000 + i);
            article.setVol(i);
            article.setJournal(journals.get(new Random().nextInt(journals.size())));
            List<Author> articleAuthors = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                articleAuthors.add(authors.get(new Random().nextInt(authors.size())));
            }
            article.setAuthors(articleAuthors);
            articleRepository.save(article);
        }

        // Create 20 chapters
        for (int i = 0; i < 20; i++) {
            Chapter chapter = new Chapter();
            chapter.setScore(10);
            chapter.setCollection("Collection " + i);
            chapter.setTitle("Chapter " + i);
            chapter.setBook(books.get(new Random().nextInt(books.size())));
            List<Author> chapterAuthors = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                chapterAuthors.add(authors.get(new Random().nextInt(authors.size())));
            }
            chapter.setAuthors(chapterAuthors);
            chapterRepository.save(chapter);
        }
    }
}
