package testtask.exptracker.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    @Query("select e from Expense e " +
            "where ((:user is null) or e.author = :user) " +
            "and ((:search is null) or (e.text like %:search%) or (e.comment like %:search%)) " +
            "and ((:dateFrom is null) or (:dateTo is null) or (e.date between :dateFrom and :dateTo))")
    List<Expense> filterByAllParams(@Param("user") User user,
                             @Param("search")String search,
                             @Param("dateFrom") LocalDate dateFrom,
                             @Param("dateTo") LocalDate dateTo);
}
