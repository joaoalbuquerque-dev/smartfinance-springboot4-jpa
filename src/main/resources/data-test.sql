INSERT INTO tb_user (id, name, email, password) VALUES
(1, 'Maria Brown', 'maria@gmail.com', '$2a$10$ewVYUW6jHI685AMn9O/Aee3DjAcLSCMHiLeOAjK5.B/au3UiMf5.W'),
(2, 'Alex Green', 'alex@gmail.com', '$2a$10$ewVYUW6jHI685AMn9O/Aee3DjAcLSCMHiLeOAjK5.B/au3UiMf5.W'),
(3, 'Jacob Black', 'jacob@gmail.com', '$2a$10$ewVYUW6jHI685AMn9O/Aee3DjAcLSCMHiLeOAjK5.B/au3UiMf5.W');

INSERT INTO tb_account (id, bank, balance, account_type, user_id) VALUES
(1, 'Nubank', 2100.00, 1, 1),
(2, 'Bradesco', 8500.00, 2, 1),
(3, 'Carteira', 350.00, 3, 2),
(4, 'Inter', 1000.00, 2, 3);

INSERT INTO tb_category (id, name) VALUES
(1, 'Alimentação'),
(2, 'Saúde'),
(3, 'Financeiro');

INSERT INTO tb_movement
(id, description, amount, transaction_date, transaction_type, account_id)
VALUES
(1, 'Supermercado', 800.00, CURRENT_TIMESTAMP, 'EXPENSE', 1),
(2, 'Farmácia', 100.00, CURRENT_TIMESTAMP, 'EXPENSE', 3),
(3, 'Restaurante', 300.00, CURRENT_TIMESTAMP, 'EXPENSE', 1),
(4, 'Médico', 400.00, CURRENT_TIMESTAMP, 'EXPENSE', 3),
(5, 'Pagamento', 1000.00, CURRENT_TIMESTAMP, 'INCOME', 4);

INSERT INTO tb_movement_category (movement_id, category_id, amount)
VALUES
(1, 1, 500.00),
(1, 2, 300.00),
(2, 2, 70.00),
(2, 1, 30.00),
(3, 1, 300.00),
(4, 2, 400.00),
(5, 3, 1000.00);

INSERT INTO tb_installment
(id, number, amount, due_date, status, movement_id)
VALUES
(1, 1, 400.00, '2026-10-10', 'PENDING', 1),
(2, 2, 400.00, '2026-11-10', 'PENDING', 1);

INSERT INTO tb_role (id, authority) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_OPERATOR');

INSERT INTO tb_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 2);

-- Ajuste dos contadores de IDENTITY

ALTER TABLE tb_user
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE tb_account
    ALTER COLUMN id RESTART WITH 5;

ALTER TABLE tb_category
    ALTER COLUMN id RESTART WITH 4;

ALTER TABLE tb_movement
    ALTER COLUMN id RESTART WITH 6;

ALTER TABLE tb_installment
    ALTER COLUMN id RESTART WITH 3;

ALTER TABLE tb_role
    ALTER COLUMN id RESTART WITH 3;