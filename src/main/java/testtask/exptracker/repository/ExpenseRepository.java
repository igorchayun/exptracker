package testtask.exptracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findById(Long id);

    @Query("select e from Expense e " +
            "where ((:user is null) or e.author = :user) " +
            "and ((:search is null) or (e.text like %:search%) or (e.comment like %:search%)) " +
            "and ( ((:dateFrom is null) and (:dateTo is null)) or " +
                  "((:dateFrom is null) and (e.date <= :dateTo)) or " +
                  "((:dateTo is null) and (e.date >= :dateFrom)) or " +
                  "(e.date between :dateFrom and :dateTo) )")
    List<Expense> filterByAllParams(
            @Param("user") User user,
            @Param("search")String search,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);


    @Query("select sum(e.cost) from Expense e " +
            "where ((:user is null) or e.author = :user) " +
            "and ((:search is null) or (e.text like %:search%) or (e.comment like %:search%)) " +
            "and ( ((:dateFrom is null) and (:dateTo is null)) or " +
                  "((:dateFrom is null) and (e.date <= :dateTo)) or " +
                  "((:dateTo is null) and (e.date >= :dateFrom)) or " +
                  "(e.date between :dateFrom and :dateTo) )")
    Double sumByAllParams(
            @Param("user") User user,
            @Param("search")String search,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);


    @Query("select min (e.date) from Expense e " +
            "where ((:user is null) or e.author = :user)")
    LocalDate minDate(@Param("user") User user);

}
