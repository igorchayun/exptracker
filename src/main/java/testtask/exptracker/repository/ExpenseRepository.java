package testtask.exptracker.repository;
import org.springframework.data.repository.CrudRepository;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;

import java.util.List;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    List<Expense> findByTextOrComment(String text, String comment);

    Iterable<Expense> findByAuthor(User currentUser);

    Iterable<Expense> findByAuthorAndTextOrComment(User currentUser, String filter, String filter1);
}
