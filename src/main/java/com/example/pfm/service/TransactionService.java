package com.example.pfm.service;

import com.example.pfm.dto.transaction.CreateTransactionRequest;
import com.example.pfm.dto.transaction.TransactionResponse;
import com.example.pfm.dto.transaction.UpdateTransactionRequest;
import com.example.pfm.entity.Category;
import com.example.pfm.entity.Transaction;
import com.example.pfm.entity.User;
import com.example.pfm.exceptions.BadRequestException;
import com.example.pfm.exceptions.NotFoundException;
import com.example.pfm.repository.CategoryRepository;
import com.example.pfm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final CategoryRepository  catRepo;
    private final ApplicationEventPublisher publisher;

    private TransactionResponse map(Transaction t) {
        return new TransactionResponse(
                t.getId(), t.getAmount(), t.getDate(),
                t.getCategory().getName(), t.getDescription(), t.getType().name());
    }

    @Transactional
    public TransactionResponse create(CreateTransactionRequest req, User u) {

        Category cat = catRepo.findByNameAndUserOrDefault(req.category(), u)
                .orElseThrow(() -> new BadRequestException("Invalid category"));

        Transaction tx = Transaction.builder()
                .user(u).category(cat)
                .amount(req.amount())
                .date(req.date())
                .description(req.description())
                .type(cat.getType())
                .build();
        txRepo.save(tx);
        publisher.publishEvent(new TxChangeEvent(u));
        return map(tx);
    }

    public List<TransactionResponse> filter(User u,
                                            LocalDate start, LocalDate end,
                                            String category, Category.Type type) {
        return txRepo.filter(u, start, end, category, type)   // arguments unchanged
                .stream().map(this::map).toList();
    }

    public List<TransactionResponse> list(User user) {
        return txRepo.findAllForUser(user)
                .stream().map(this::map).toList();
    }


    @Transactional
    public TransactionResponse update(Long id, UpdateTransactionRequest r, User u){
        Transaction t = txRepo.findByIdAndUserAndDeletedFalse(id, u)
                .orElseThrow(() -> new NotFoundException("Transaction"));
        t.setAmount(r.amount());
        t.setDescription(r.description());
        publisher.publishEvent(new TxChangeEvent(u));
        return map(t);
    }

    @Transactional
    public void delete(Long id, User u){
        Transaction t = txRepo.findByIdAndUserAndDeletedFalse(id, u)
                .orElseThrow(() -> new NotFoundException("Transaction"));
        t.setDeleted(true);
        publisher.publishEvent(new TxChangeEvent(u));
    }

}
