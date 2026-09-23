package entity;

import java.time.LocalDateTime;

public record Transaction(
    Long id,
    LocalDateTime date,
    double amount,
    TransactionType type,
    String location,
    Long accountId
) {
}