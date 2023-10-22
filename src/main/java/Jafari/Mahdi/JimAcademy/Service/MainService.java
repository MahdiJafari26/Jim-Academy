package Jafari.Mahdi.JimAcademy.Service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class MainService {
    private final WebApplicationContext applicationContext;
    private Repositories repositories;

    public MainService(WebApplicationContext applicationContext) {
        repositories = new Repositories(applicationContext);
        this.applicationContext = applicationContext;
    }

    public <T> T save(T object) {
        Object repository = repositories.getRepositoryFor(object.getClass()).orElseThrow(
                () -> new IllegalStateException(
                        "Can't find repository for entity of type " + object.getClass()));
        CrudRepository<T, Long> crudRepository = (CrudRepository<T, Long>) repository;

        return crudRepository.save(object);
    }


}
