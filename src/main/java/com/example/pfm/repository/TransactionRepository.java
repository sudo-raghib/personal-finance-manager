package com.example.pfm.repository;

import com.example.pfm.entity.Category;
import com.example.pfm.entity.Transaction;
import com.example.pfm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
       select t
       from Transaction t
       join fetch t.category
       where t.deleted = false
         and t.user    = :user
       order by t.date desc
       """)
    List<Transaction> findAllForUser(@Param("user") User user);

    @Query("""
       select t
       from Transaction t
       join fetch t.category
       where t.deleted = false
         and t.user   = :user
         and (:start  is null or t.date >= :start)
         and (:end    is null or t.date <= :end)
         and (:cat    is null or lower(t.category.name)=lower(:cat))
         and (:txType is null or t.type = :txType)
       order by t.date desc
       """)
    List<Transaction> filter(@Param("user")   User          user,
                             @Param("start")  LocalDate     start,
                             @Param("end")    LocalDate     end,
                             @Param("cat")    String        cat,
                             @Param("txType") Category.Type txType);

    Optional<Transaction> findByIdAndUserAndDeletedFalse(Long id, User user);
    long countByCategoryAndDeletedFalse(Category c);

    @Query("""
           select sum(t.amount) from Transaction t
           where t.deleted = false
             and t.user  = :user
             and t.type  = :type
             and t.date between :start and :end
           """)
    BigDecimal sumAmount(@Param("user")  User user,
                         @Param("type")  Category.Type type,
                         @Param("start") LocalDate start,
                         @Param("end")   LocalDate end);
}
