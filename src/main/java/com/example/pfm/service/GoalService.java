package com.example.pfm.service;

import com.example.pfm.dto.goal.GoalRequest;
import com.example.pfm.dto.goal.GoalResponse;
import com.example.pfm.entity.Category;
import com.example.pfm.entity.Goal;
import com.example.pfm.entity.User;
import com.example.pfm.exceptions.BadRequestException;
import com.example.pfm.exceptions.NotFoundException;
import com.example.pfm.repository.GoalRepository;
import com.example.pfm.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class GoalService {

    private final GoalRepository repo;
    private final TransactionRepository txRepo;

    public List<GoalResponse> list(User u){
        return repo.findByUser(u).stream().map(this::map).toList();
    }

    @Transactional
    public GoalResponse create(GoalRequest r, User u){

        if (r.startDate()!=null && r.startDate().isAfter(r.targetDate()))
            throw new BadRequestException("startDate after targetDate");

        Goal g = Goal.builder()
                .user(u)
                .goalName(r.goalName())
                .targetAmount(r.targetAmount())
                .targetDate(r.targetDate())
                .startDate(r.startDate()!=null ? r.startDate() : LocalDate.now())
                .build();
        repo.save(g);
        return map(g);
    }

    @EventListener
    public void txChanged(TxChangeEvent ev){
        repo.findByUser(ev.user()).forEach(this::map);
    }


    public GoalResponse get(Long id, User u){
        return repo.findByIdAndUser(id,u).map(this::map)
                .orElseThrow(() -> new NotFoundException("Goal"));
    }

    @Transactional
    public GoalResponse update(Long id, BigDecimal target, LocalDate date, User u){
        Goal g = repo.findByIdAndUser(id,u)
                .orElseThrow(() -> new NotFoundException("Goal"));
        if(target!=null) g.setTargetAmount(target);
        if(date!=null)   g.setTargetDate(date);
        return map(g);
    }

    @Transactional
    public void delete(Long id, User u){
        Goal g = repo.findByIdAndUser(id,u)
                .orElseThrow(() -> new NotFoundException("Goal"));
        repo.delete(g);
    }


    private GoalResponse map(Goal g){

        BigDecimal income  = nz(txRepo.sumAmount(g.getUser(), Category.Type.INCOME,
                g.getStartDate(), LocalDate.now()));
        BigDecimal expense = nz(txRepo.sumAmount(g.getUser(), Category.Type.EXPENSE,
                g.getStartDate(), LocalDate.now()));

        BigDecimal progress   = income.subtract(expense);
        BigDecimal remaining  = g.getTargetAmount().subtract(progress).max(BigDecimal.ZERO);

        BigDecimal pct = progress.multiply(BigDecimal.valueOf(100))
                .divide(g.getTargetAmount(), 2, RoundingMode.HALF_UP);

        return new GoalResponse(
                g.getId(),
                g.getGoalName(),
                g.getTargetAmount(),
                g.getTargetDate(),
                g.getStartDate(),
                progress,
                pct.doubleValue(),
                remaining);
    }


    private static BigDecimal nz(BigDecimal v){ return v==null ? BigDecimal.ZERO : v; }
}
