package testtask.exptracker.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;

import java.util.List;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    List<Expense> findByTextOrComment(String text, String comment);

    Iterable<Expense> findByAuthor(User currentUser);

    Iterable<Expense> findByAuthorAndText(User currentUser, String filter);

//    @Query("select e from Expense e where e.author = :currentUser and (e.text Containing :filter or e.comment Containing :filter1)")
//    Iterable<Expense> findByAuthorAndTextOrComment(
//            @Param("currentUser") User currentUser,
//            @Param("filter") String filter,
//            @Param("filter1") String filter);
}
