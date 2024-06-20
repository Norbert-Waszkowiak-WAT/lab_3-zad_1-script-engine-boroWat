package pl.edu.wat.knowledge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.knowledge.entity.Article;
import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Book;
import pl.edu.wat.knowledge.entity.Chapter;
import pl.edu.wat.knowledge.repository.ArticleRepository;
import pl.edu.wat.knowledge.repository.BookRepository;
import pl.edu.wat.knowledge.repository.ChapterRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScoreService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Double> getArticleScores(Author author, Integer year) {
        // Artykuły autora z danego roku
        List<Article> articles = articleRepository.findByAuthorsContainingAndYear(author, year);

        // punkty z artykulow na jednego autora
        return articles.stream()
                .map(article -> article.getScore() / (double) article.getAuthors().size())
                .sorted(Comparator.reverseOrder()) // Sortowanie 
                .collect(Collectors.toList());
    }

    public List<Double> getChapterScores(Author author, Integer year) {
        // ksiazki z danego roku
        List<Book> books = bookRepository.findByYear(year);

        // Pobierz rozdziały opublikowane przez autora w książkach z odpowiednim rokiem
        List<Chapter> chapters = chapterRepository.findByAuthorsInAndBookIn(List.of(author), books);

        // punkty dla rozdzialu i posortuj 
        return chapters.stream()
                .map(chapter -> chapter.getScore() / (double) chapter.getAuthors().size())
                .sorted(Comparator.reverseOrder()) // Sortowanie 
                .collect(Collectors.toList());
    }

    public Integer getScore(Author author, Integer year) {
        // punkty razek
        List<Double> articleScores = getArticleScores(author, year);
        List<Double> chapterScores = getChapterScores(author, year);

        // polacz i posortuj
        List<Double> allScores = Stream.concat(articleScores.stream(), chapterScores.stream())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 4 najwyzsze pozycje punktowe
        return allScores.stream()
                .limit(4)
                .mapToInt(Double::intValue)
                .sum();
    }
}