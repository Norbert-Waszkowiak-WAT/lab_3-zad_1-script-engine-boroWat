package pl.edu.wat.knowledge.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.wat.knowledge.AbstractContainerBaseTest;


class ScriptServiceTest extends AbstractContainerBaseTest {
    @Autowired
    ScriptService scriptService;

    @Test
    public void testCalc() {
        String script = """
                   var x = 1;
                   var y = 2;
                   x + y;
                """;

        assert scriptService.exec(script).equals("3");
    }
/* 
    //@Test
    
    public void testAddAndRemoveAuthor() {
        String addScript = """
                var Article = Java.type('pl.edu.wat.knowledge.entity.Article');
                var Author = Java.type('pl.edu.wat.knowledge.entity.Author');
                var Set = Java.type('java.util.Set');

                var patrycjaAuthor = new Author();
                patrycjaAuthor.setName("Patrycja");
                patrycjaAuthor.setSurname("Woda");
                patrycjaAuthor.setPesel("123123123");
                authorRepository.save(patrycjaAuthor).getId();
                """;
        String id = scriptService.exec(addScript);
        String deleteScript = "authorRepository.deleteById(\"" + id + "\")";
        assert scriptService.exec(deleteScript) != null;
    }
*/

}