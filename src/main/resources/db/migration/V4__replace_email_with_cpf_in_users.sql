-- ============================================================
-- Workshop Management System — Migration: CPF substitui e-mail
-- como identificador de login dos funcionários (Fase 3)
-- ============================================================

-- Popular os valores de CPF antes de encolher a coluna evita truncamento
-- (os e-mails seedados em V3 são mais longos que VARCHAR(11)).
UPDATE users SET email = '11144477735' WHERE id = 'c3000000-0000-0000-0000-000000000001';
UPDATE users SET email = '22255588846' WHERE id = 'c3000000-0000-0000-0000-000000000002';
UPDATE users SET email = '33366699957' WHERE id = 'c3000000-0000-0000-0000-000000000003';
UPDATE users SET email = '44477722214' WHERE id = 'c3000000-0000-0000-0000-000000000004';

ALTER TABLE users CHANGE COLUMN email cpf VARCHAR(11) NOT NULL;
ALTER TABLE users RENAME INDEX uq_users_email TO uq_users_cpf;
